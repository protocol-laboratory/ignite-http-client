package io.github.protocol.ignite.http.client.common;

public class IgniteUrlUtil {
    private static final String DEFAULT_PREFIX = "/ignite";

    public static String probePath() {
        return DEFAULT_PREFIX + "?cmd=probe";
    }
}
