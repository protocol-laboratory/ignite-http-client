package io.github.protocol.ignite.http.client.reactor;

import io.github.protocol.ignite.http.client.api.ProbeResp;
import io.github.protocol.ignite.http.client.common.IgniteUrlUtil;
import reactor.core.publisher.Mono;

public class IgniteReactorHttpClient {
    private final ReactorHttpClient reactorHttpClient;

    public IgniteReactorHttpClient(Configuration conf) {
        this.reactorHttpClient = new ReactorHttpClient(conf);
    }

    public Mono<ProbeResp> probe() {
        return reactorHttpClient.get(IgniteUrlUtil.probePath(), ProbeResp.class);
    }

    public ProbeResp probeSync() {
        return probe().block();
    }
}
