package io.github.protocol.ignite.http.client.jdk;

import io.github.embedded.ignite.core.EmbeddedIgniteServer;
import io.github.protocol.ignite.http.client.api.ProbeResp;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

public class JdkHttpClientProbeTest {
    private static final EmbeddedIgniteServer SERVER = new EmbeddedIgniteServer();

    private static IgniteJdkHttpClient igniteJdkHttpClient;

    @BeforeAll
    public static void setup() throws Exception {
        SERVER.start();
        Configuration conf = new Configuration();
        conf.setPort(SERVER.httpPort());
        igniteJdkHttpClient = new IgniteJdkHttpClient(conf);
    }

    @AfterAll
    public static void teardown() throws Exception {
        SERVER.close();
    }

    @Test
    public void probeTest() throws Exception {
        CompletableFuture<ProbeResp> result = igniteJdkHttpClient.probe();
        ProbeResp probeResp = result.get();
        Assertions.assertEquals(0, probeResp.getSuccessStatus());
    }

    @Test
    public void probeSyncTest() throws Exception {
        ProbeResp probeResp = igniteJdkHttpClient.probeSync();
        Assertions.assertEquals(0, probeResp.getSuccessStatus());
    }
}
