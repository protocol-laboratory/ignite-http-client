package io.github.protocol.ignite.http.client.jdk;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
public class Configuration {
    private String host = "localhost";

    private int port;

    public boolean useTls = false;

    public String keyStorePath;

    @ToString.Exclude
    public String keyStorePassword;

    public String trustStorePath;

    @ToString.Exclude
    public String trustStorePassword;

    public boolean disableTlsVerify;

    public String[] tlsProtocols;

    public String[] tlsCiphers;

    public Configuration() {
    }
}
