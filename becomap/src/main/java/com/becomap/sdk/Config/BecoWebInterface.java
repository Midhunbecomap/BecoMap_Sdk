package com.becomap.sdk.Config;


import android.util.Log;
import android.webkit.WebView;

public class BecoWebInterface {
    private static final String TAG = "BecomapJSConfig";
    private final String clientId;
    private final String clientSecret;
    private final String siteIdentifier;
    private String initOptions;

    public BecoWebInterface(String clientId, String clientSecret, String siteIdentifier) {
        if (clientId == null || clientSecret == null || siteIdentifier == null) {
            throw new IllegalArgumentException("Credentials cannot be null");
        }
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.siteIdentifier = siteIdentifier;
    }

    /**
     * Set custom initialization options in JSON format
     * @param options JSON string with additional init parameters
     */
    public BecoWebInterface setInitOptions(String options) {
        this.initOptions = options;
        return this;
    }

    public String getInitCommand() {
        if (initOptions != null && !initOptions.isEmpty()) {
            return String.format("init({ clientId: '%s', clientSecret: '%s', siteIdentifier: '%s', %s })",
                    clientId, clientSecret, siteIdentifier,
                    initOptions.startsWith("{") ? initOptions.substring(1) : initOptions);
        } else {
            return String.format("init({ clientId: '%s', clientSecret: '%s', siteIdentifier: '%s' })",
                    clientId, clientSecret, siteIdentifier);
        }
    }

    public void executeInit(WebView webView) {
        if (webView == null) {
            Log.e(TAG, "WebView is null, cannot execute init");
            return;
        }

        String script = String.format("javascript:(function() { %s })()", getInitCommand());
        webView.evaluateJavascript(script, value ->
                Log.d(TAG, "Initialization executed: " + getInitCommand()));
    }

    public void executegetalllocation(WebView webView) {
        if (webView == null) {
            Log.e(TAG, "WebView is null, cannot execute getalllocation");
            return;
        }

        webView.evaluateJavascript("javascript:getLocations()", null);
    }
}
