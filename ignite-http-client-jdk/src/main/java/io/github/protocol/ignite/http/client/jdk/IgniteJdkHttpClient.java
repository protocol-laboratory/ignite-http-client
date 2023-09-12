package io.github.protocol.ignite.http.client.jdk;

import io.github.protocol.ignite.http.client.api.IgniteException;
import io.github.protocol.ignite.http.client.api.ProbeResp;
import io.github.protocol.ignite.http.client.common.IgniteUrlUtil;
import io.github.protocol.ignite.http.client.common.JacksonService;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class IgniteJdkHttpClient {
    private final JdkHttpClient jdkHttpClient;

    public IgniteJdkHttpClient(Configuration conf) {
        this.jdkHttpClient = new JdkHttpClient(conf);
    }

    public CompletableFuture<ProbeResp> probe() {
        return jdkHttpClient.get(IgniteUrlUtil.probePath()).thenCompose(response -> {
            if (response.statusCode() != 200) {
                String errorMsg = String.format("Unexpected status code %d body %s",
                        response.statusCode(), response.body());
                return CompletableFuture.failedFuture(new IgniteException(errorMsg));
            }
            try {
                ProbeResp probeResp = JacksonService.toObject(response.body(), ProbeResp.class);
                return CompletableFuture.completedFuture(probeResp);
            } catch (IOException e) {
                return CompletableFuture.failedFuture(new IgniteException(e));
            }
        });
    }

    public ProbeResp probeSync() throws IOException, InterruptedException, IgniteException {
        HttpResponse<String> httpResponse = jdkHttpClient.getSync(IgniteUrlUtil.probePath());
        if (httpResponse.statusCode() != 200) {
            throw new IgniteException(String.format("Unexpected status code %d body %s",
                    httpResponse.statusCode(), httpResponse.body()));
        }
        return JacksonService.toObject(httpResponse.body(), ProbeResp.class);
    }
}
