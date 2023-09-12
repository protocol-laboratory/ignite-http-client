package io.github.protocol.ignite.http.client.reactor;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.protocol.ignite.http.client.api.IgniteException;
import io.github.protocol.ignite.http.client.common.JacksonService;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.ssl.SslContext;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufMono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.http.client.HttpClientSecurityUtils;

public class ReactorHttpClient {
    private final HttpClient httpClient;

    private final String httpPrefix;

    public ReactorHttpClient(Configuration conf) {
        HttpClient client = HttpClient.create();

        if (conf.isUseTls()) {
            client = client.secure(spec -> {
                SslContext context = SslContextUtil.buildFromJks(
                        conf.keyStorePath, conf.keyStorePassword,
                        conf.trustStorePath, conf.trustStorePassword,
                        conf.disableTlsVerify, conf.tlsProtocols, conf.tlsCiphers);
                if (conf.disableTlsHostnameVerification) {
                    spec.sslContext(context)
                            .handlerConfigurator(HttpClientSecurityUtils.HOSTNAME_VERIFICATION_CONFIGURER);
                } else {
                    spec.sslContext(context);
                }
            });
            this.httpPrefix = "https://" + conf.getHost() + ":" + conf.getPort();
        } else {
            this.httpPrefix = "http://" + conf.getHost() + ":" + conf.getPort();
        }

        client = client.headers(headers ->
                headers.add(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON));

        this.httpClient = client;
    }

    public <T> Mono<T> get(String path, Class<T> type) {
        return handleResponse(httpClient.get().uri(httpPrefix + path))
                .flatMap(jsonString -> {
                    try {
                        return Mono.just(JacksonService.toObject(jsonString, type));
                    } catch (JsonProcessingException e) {
                        return Mono.error(e);
                    }
                });
    }

    public Mono<String> get(String path) {
        return handleResponse(httpClient.get().uri(httpPrefix + path));
    }

    public <T> Mono<T> post(String path, String requestBody, Class<T> type) {
        return handleResponse(httpClient.post()
                .uri(httpPrefix + path)
                .send(ByteBufMono.fromString(Mono.just(requestBody))))
                .flatMap(jsonString -> {
                    try {
                        return Mono.just(JacksonService.toObject(jsonString, type));
                    } catch (JsonProcessingException e) {
                        return Mono.error(e);
                    }
                });
    }

    public Mono<String> post(String path, String requestBody) {
        return handleResponse(httpClient.post()
                .uri(httpPrefix + path)
                .send(ByteBufMono.fromString(Mono.just(requestBody))));
    }

    public <T> Mono<T> put(String path, String requestBody, Class<T> type) {
        return handleResponse(httpClient.put()
                .uri(httpPrefix + path)
                .send(ByteBufMono.fromString(Mono.just(requestBody))))
                .flatMap(jsonString -> {
                    try {
                        return Mono.just(JacksonService.toObject(jsonString, type));
                    } catch (JsonProcessingException e) {
                        return Mono.error(e);
                    }
                });
    }

    public Mono<String> put(String path, String requestBody) {
        return handleResponse(httpClient.put()
                .uri(httpPrefix + path)
                .send(ByteBufMono.fromString(Mono.just(requestBody))));
    }

    public <T> Mono<T> delete(String path, Class<T> type) {
        return handleResponse(httpClient.delete().uri(httpPrefix + path))
                .flatMap(jsonString -> {
                    try {
                        return Mono.just(JacksonService.toObject(jsonString, type));
                    } catch (JsonProcessingException e) {
                        return Mono.error(e);
                    }
                });
    }

    public Mono<String> delete(String path) {
        return handleResponse(httpClient.delete().uri(httpPrefix + path));
    }

    private Mono<String> handleResponse(HttpClient.ResponseReceiver<?> responseReceiver) {
        return responseReceiver.responseSingle((response, content) -> {
            int code = response.status().code();
            if (code >= 200 && code < 300) {
                return content.asString();
            } else {
                return content.asString()
                        .flatMap(body -> Mono.error(new IgniteException(body, code)));
            }
        });
    }

}
