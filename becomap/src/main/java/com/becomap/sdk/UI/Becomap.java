package com.becomap.sdk.UI;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;
import android.webkit.WebChromeClient;

import android.widget.Toast;
import android.webkit.JavascriptInterface;

import android.webkit.ConsoleMessage;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.becomap.sdk.ViewModel.BecomapViewModel;


public class Becomap {
    private final Context context;
    private WebView webView;
    private ViewGroup rootContainer;
    private BecomapViewModel viewModel;
    private static final String TAG = "Becomap";

    public Becomap(Context context) {
        if (context == null) throw new IllegalArgumentException("Context cannot be null");
        this.context = context;

        // Initialize ViewModel internally
        if (context instanceof ViewModelStoreOwner) {
            viewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(BecomapViewModel.class);
        } else {
            throw new IllegalStateException("Context must implement ViewModelStoreOwner (e.g., be a FragmentActivity or ComponentActivity)");
        }
    }

    public void initializeMap(ViewGroup container, String clientId, String clientSecret, String siteIdentifier) {
        this.rootContainer = container;

        // Save credentials in ViewModel
        viewModel.setCredentials(clientId, clientSecret, siteIdentifier);

        webView = new WebView(context);
        container.addView(webView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));

        setupWebView();
        loadLocalHtml();
    }

    private void setupWebView() {
        if (webView == null) return;

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);

        webView.addJavascriptInterface(new WebAppInterface(context), "jsHandler");

        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDatabaseEnabled(true);
        webSettings.setGeolocationEnabled(false);
        webSettings.setSaveFormData(false);
        webSettings.setSavePassword(false);

        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                String clientId = viewModel.getClientId().getValue();
                String clientSecret = viewModel.getClientSecret().getValue();
                String siteId = viewModel.getSiteIdentifier().getValue();

                if (clientId != null && clientSecret != null && siteId != null) {
                    publishMessage("init({ clientId: '" + clientId + "', clientSecret: '" + clientSecret + "', siteIdentifier: '" + siteId + "' })");
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e(TAG, "WebView error: " + description);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d(TAG, "Console: " + consoleMessage.message());
                return true;
            }
        });
    }

    private void loadLocalHtml() {
        if (webView != null) {
            webView.loadUrl("file:///android_asset/esm-build/index.html");
        }
    }

    public void publishMessage(String jsMessage) {
        if (webView == null) {
            Log.e(TAG, "WebView is null, cannot publish message");
            return;
        }

        String script = "javascript:(function() { " + jsMessage + " })()";
        webView.evaluateJavascript(script, value ->
                Log.d(TAG, "JavaScript message published: " + jsMessage));
    }

    private class WebAppInterface {
        Context mContext;

        WebAppInterface(Context context) {
            this.mContext = context;
        }

        @JavascriptInterface
        public void postMessage(String message) {
            Log.d(TAG, "Received from JS: " + message);
            if ("onRenderComplete".equals(message)) {
                if (mContext instanceof Activity) {
                    ((Activity) mContext).runOnUiThread(() ->
                            Toast.makeText(mContext, "Map Render Complete", Toast.LENGTH_SHORT).show());
                }
            }
        }
    }

    public void searchAndRoute(String firstLocation, String secondLocation) {
        if (webView == null) return;

        String script = "javascript:(function() {" +
                "if (!window._mapView || !window._mapView.eventsHandler) return;" +
                "window._mapView.eventsHandler.on('load', () => {" +
                "searchForLocations('" + firstLocation + "', function(tMatches) {" +
                "searchForLocations('" + secondLocation + "', function(kMatches) {" +
                "if (tMatches.length && kMatches.length) {" +
                "const segments = getRoute(tMatches[0], kMatches[0], [], { maxDistanceThreshold: 30 });" +
                "if (segments) { showRoute(segments, null); }" +
                "}});});});})()";

        webView.evaluateJavascript(script, value -> {
            Log.d(TAG, "Route search called: " + firstLocation + " -> " + secondLocation);
        });
    }

    // Lifecycle hooks
    public void onStart() { if (webView != null) webView.onResume(); }
    public void onResume() { if (webView != null) webView.onResume(); }
    public void onPause() { if (webView != null) webView.onPause(); }
    public void onStop() { if (webView != null) webView.onPause(); }
    public void onDestroy() {
        if (webView != null) {
            webView.stopLoading();
            webView.clearHistory();
            webView.clearCache(true);
            webView.loadUrl("about:blank");
            webView.onPause();
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        if (webView != null) {
            webView.saveState(outState);
        }
    }

    public void onLowMemory() {
        if (webView != null) {
            webView.clearCache(true);
        }
    }

    public boolean canGoBack() {
        return webView != null && webView.canGoBack();
    }

    public void goBack() {
        if (webView != null) {
            webView.goBack();
        }
    }
}