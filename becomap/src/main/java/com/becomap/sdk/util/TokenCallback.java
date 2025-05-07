package com.becomap.sdk.util;

public interface TokenCallback {
    void onSuccess(String accessToken);
    void onFailure(String error);
}
