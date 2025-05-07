package com.becomap.sdk.Model;

import com.google.gson.annotations.SerializedName;

public class SdkTokenResponse {
    @SerializedName("accessToken")
    private String accessToken;

    @SerializedName("tokenType")
    private String tokenType;

    @SerializedName("expiresAt")
    private String expiresAt;

    public String getAccessToken() { return accessToken; }
    public String getTokenType() { return tokenType; }
    public String getExpiresAt() { return expiresAt; }
}
