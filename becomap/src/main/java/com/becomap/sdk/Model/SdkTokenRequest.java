package com.becomap.sdk.Model;

public class SdkTokenRequest {
    private String clientId;
    private String clientSecret;

    public SdkTokenRequest(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }
}