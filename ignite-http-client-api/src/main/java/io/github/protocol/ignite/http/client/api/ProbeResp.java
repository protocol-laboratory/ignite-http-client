package io.github.protocol.ignite.http.client.api;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProbeResp {
    private int successStatus;

    private String error;

    private String sessionToken;

    private String response;
}
