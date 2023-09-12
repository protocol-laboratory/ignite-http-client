package io.github.protocol.ignite.http.client.api;

public class IgniteException extends Exception {
    private static final int DEFAULT_STATUS_CODE = 500;

    private final int statusCode;

    public IgniteException(Throwable t) {
        super(t);
        statusCode = DEFAULT_STATUS_CODE;
    }

    public IgniteException(String message) {
        this(message, DEFAULT_STATUS_CODE);
    }

    public IgniteException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}
