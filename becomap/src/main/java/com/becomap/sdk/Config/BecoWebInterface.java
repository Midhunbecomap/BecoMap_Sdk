package com.becomap.sdk.Config;


import android.util.Log;
import android.webkit.WebView;

import androidx.annotation.Nullable;

import org.json.JSONObject;

import java.util.List;

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
     *
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

    public void execute_search_all_location(WebView webView, String value) {
        if (webView == null) {
            Log.e(TAG, "WebView is null, cannot execute searchlocation");
            return;
        }
        String callbackId = "search_callback_1";

        String jsCall = String.format("javascript:searchForLocations('%s', '%s')", value, callbackId);
        webView.post(() -> webView.evaluateJavascript(jsCall, null));
    }

    public void injectGetSiteIdFunction(WebView webView) {
        if (webView == null) {
            Log.e(TAG, "WebView is null, cannot inject getSiteId");
            return;
        }

        webView.evaluateJavascript("javascript:getSiteId()", null);
    }

    public void injectGetSiteNameFunction(WebView webView) {
        if (webView == null) {
            Log.e(TAG, "WebView is null, cannot inject getSitename");
            return;
        }

        webView.evaluateJavascript("javascript:getSiteName()", null);
    }

    public void GetBuildinsFunction(WebView webView) {
        if (webView == null) {
            Log.e(TAG, "WebView is null, cannot inject GetBuildins");
            return;
        }

        webView.evaluateJavascript("javascript:getBuildings()", null);
    }

    public void GetDefaultFloor(WebView webView) {
        if (webView == null) {
            Log.e(TAG, "WebView is null, cannot inject getDefaultFloor");
            return;
        }

        webView.evaluateJavascript("javascript:getDefaultFloor()", null);
    }

    public void GetLanguages(WebView webView) {
        if (webView == null) {
            Log.e(TAG, "WebView is null, cannot inject getLanguages");
            return;
        }

        webView.evaluateJavascript("javascript:getLanguages()", null);
    }

    public void GetCurrentFloor(WebView webView) {
        if (webView == null) {
            Log.e(TAG, "WebView is null, cannot inject getCurrentFloor");
            return;
        }

        webView.evaluateJavascript("javascript:getCurrentFloor()", null);
    }

    public void GetCategories(WebView webView) {
        if (webView == null) {
            Log.e(TAG, "WebView is null, cannot inject getCategories");
            return;
        }

        webView.evaluateJavascript("javascript:getCategories()", null);
    }

    public void GetAllAmenities(WebView webView) {
        if (webView == null) {
            Log.e(TAG, "WebView is null, cannot inject getAllAmenities");
            return;
        }

        webView.evaluateJavascript("javascript:getAllAmenities()", null);
    }

    public void GetAmenities(WebView webView) {
        if (webView == null) {
            Log.e(TAG, "WebView is null, cannot inject getAmenities");
            return;
        }

        webView.evaluateJavascript("javascript:getAmenities()", null);
    }

    public void GetQuestions(WebView webView) {
        if (webView == null) {
            Log.e(TAG, "WebView is null, cannot inject getQuestions");
            return;
        }

        webView.evaluateJavascript("javascript:getQuestions()", null);
    }

    public void GetSessionId(WebView webView) {
        if (webView == null) {
            Log.e(TAG, "WebView is null, cannot inject getSessionId");
            return;
        }

        // This will trigger the JS async function that calls into native via _mapView.getSessionId()
        webView.evaluateJavascript("javascript:globalThis.getSessionId()", null);
    }

    public void GetHappenings(WebView webView, String value) {
        if (webView == null) {
            Log.e(TAG, "WebView is null, cannot execute searchlocation");
            return;
        }

        String jsCall = String.format("javascript:getHappenings('%s')", value);
        webView.post(() -> webView.evaluateJavascript(jsCall, null));
    }

    public void selectFloor(WebView webView, String rawFloorJson) {
        if (webView == null) {
            Log.e(TAG, "WebView is null, cannot execute selectFloor");
            return;
        }

        // Make sure to escape any double quotes or backslashes in the JSON string
        String jsCall = String.format("javascript:selectFloorWithId('%s')", rawFloorJson);
        Log.e(TAG, "jsCall: " + jsCall);
        webView.post(() -> webView.evaluateJavascript(jsCall, null));
    }

    public void selectLocationWithId(WebView webView, String id) {
        if (webView == null) {
            Log.e(TAG, "WebView is null, cannot execute selectFloor");
            return;
        }

        // Make sure to escape any double quotes or backslashes in the JSON string
        String jsCall = String.format("javascript:selectLocationWithId('%s')", id);
        Log.e(TAG, "jsCall: " + jsCall);
        webView.post(() -> webView.evaluateJavascript(jsCall, null));
    }

    public void getRouteById(WebView webView, String startID, String goalID,List<String> waypoints) {
        if (webView == null) {
            Log.e(TAG, "WebView is null, cannot execute getRouteById");
            return;
        }

        // Convert waypoints list to a JSON array string
        StringBuilder waypointsJson = new StringBuilder("[");
        if (waypoints != null && !waypoints.isEmpty()) {
            for (int i = 0; i < waypoints.size(); i++) {
                waypointsJson.append("\"").append(escapeJsString(waypoints.get(i))).append("\"");
                if (i < waypoints.size() - 1) {
                    waypointsJson.append(",");
                }
            }
        }
        waypointsJson.append("]");

        // routeOptions could be null
        // Build JS function call
        String jsCall = String.format("javascript:globalThis.getRoute('%s', '%s', %s, %s)",
                escapeJsString(startID),
                escapeJsString(goalID),
                waypointsJson.toString(),
                null);

        Log.d("JSCall", "Calling JS: " + jsCall);

        // Execute in WebView
        webView.post(() -> webView.evaluateJavascript(jsCall, null));
    }

    public void showRoute(WebView webView) {
        if (webView == null) {
            Log.e(TAG, "WebView is null, cannot inject showRoute");
            return;
        }

        // Triggers JavaScript function in the WebView
        webView.evaluateJavascript("javascript:showRoute()", null);
    }
    private String escapeJsString(String input) {
        if (input == null) return "";
        return input.replace("\\", "\\\\").replace("'", "\\'");
    }
}
