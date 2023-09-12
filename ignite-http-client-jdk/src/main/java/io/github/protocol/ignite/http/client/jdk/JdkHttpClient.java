package io.github.protocol.ignite.http.client.jdk;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class JdkHttpClient {

    private final HttpClient client;

    private final String httpPrefix;

    public JdkHttpClient(Configuration conf) {
        HttpClient.Builder builder = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1);
        if (conf.isUseTls()) {
            builder = builder
                    .sslContext(SslContextUtil.buildFromJks(conf.keyStorePath, conf.keyStorePassword,
                            conf.trustStorePath, conf.trustStorePassword, conf.disableTlsVerify,
                            conf.tlsProtocols, conf.tlsCiphers));
            this.httpPrefix = "https://" + conf.getHost() + ":" + conf.getPort();
        } else {
            this.httpPrefix = "http://" + conf.getHost() + ":" + conf.getPort();
        }
        this.client = builder.build();
    }

    public CompletableFuture<HttpResponse<String>> get(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(getUri(url))
                .GET()
                .build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> getSync(String url)
            throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(getUri(url))
                .GET()
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public CompletableFuture<HttpResponse<String>> post(String url, String body) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(getUri(url))
                .POST(HttpRequest.BodyPublishers.ofString(body == null ? "" : body))
                .setHeader("Content-Type", "application/json")
                .build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> postSync(String url, String body)
            throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(getUri(url))
                .POST(HttpRequest.BodyPublishers.ofString(body == null ? "" : body))
                .setHeader("Content-Type", "application/json")
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public CompletableFuture<HttpResponse<String>> put(String url, String body) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(getUri(url))
                .PUT(HttpRequest.BodyPublishers.ofString(body == null ? "" : body))
                .setHeader("Content-Type", "application/json")
                .build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> putSync(String url, String body)
            throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(getUri(url))
                .PUT(HttpRequest.BodyPublishers.ofString(body == null ? "" : body))
                .setHeader("Content-Type", "application/json")
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public CompletableFuture<HttpResponse<String>> delete(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(getUri(url))
                .DELETE()
                .build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> deleteSync(String url)
            throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(getUri(url))
                .DELETE()
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private URI getUri(String url) {
        return URI.create(this.httpPrefix + url);
    }
}
