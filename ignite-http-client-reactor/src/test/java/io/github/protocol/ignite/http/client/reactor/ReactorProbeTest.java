package io.github.protocol.ignite.http.client.reactor;

import io.github.embedded.ignite.core.EmbeddedIgniteServer;
import io.github.protocol.ignite.http.client.api.ProbeResp;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ReactorProbeTest {
    private static final EmbeddedIgniteServer SERVER = new EmbeddedIgniteServer();

    private static IgniteReactorHttpClient igniteReactorHttpClient;

    @BeforeAll
    public static void setup() throws Exception {
        SERVER.start();
        Configuration conf = new Configuration();
        conf.setPort(SERVER.httpPort());
        igniteReactorHttpClient = new IgniteReactorHttpClient(conf);
    }

    @AfterAll
    public static void teardown() throws Exception {
        SERVER.close();
    }

    @Test
    public void probeSyncTest() {
        ProbeResp probeResp = igniteReactorHttpClient.probeSync();
        Assertions.assertEquals(0, probeResp.getSuccessStatus());
    }
}
