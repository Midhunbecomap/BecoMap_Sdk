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

import com.becomap.sdk.Config.BecoWebInterface;
import com.becomap.sdk.ViewModel.BecomapViewModel;
import com.becomap.sdk.model.LocationModel;
import com.becomap.sdk.model.SearchResult;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Becomap {
    private final Context context;
    private WebView webView;
    private ViewGroup rootContainer;
    private BecomapViewModel viewModel;
    private static final String TAG = "Becomap";
    private BecomapCallback callback;
    private BecoWebInterface jsConfig;
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
        jsConfig = new BecoWebInterface(clientId, clientSecret, siteIdentifier);
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
    public void searchLocation( String value) {
        Log.d(TAG, "searchLocation: "+value);
        jsConfig.execute_search_all_location(webView,value);
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

                if ( jsConfig != null) {
                    jsConfig.executeInit(webView);
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

    public void setCallback(BecomapCallback callback) {
        this.callback = callback;
    }
    private class WebAppInterface {
        Context mContext;

        WebAppInterface(Context context) {
            this.mContext = context;
        }

        @JavascriptInterface
        public void postMessage(String messageJson) {
            Log.d(TAG, "Received from JS: " + messageJson);

            try {
                JSONObject message = new JSONObject(messageJson);
                String types = message.getString("type");

                switch (types) {
                    case "onRenderComplete":
                        if (mContext instanceof Activity) {
                            ((Activity) mContext).runOnUiThread(() -> {
                                Log.d(TAG, "Map Render Complete");
                                // Call getLocations() from Android after render is complete
                                if (webView != null) {
                                    jsConfig.executegetalllocation(webView);
                                }
                            });
                        }
                        break;

                    case "getLocations":
                        JSONArray locations = message.getJSONArray("payload");
                        List<LocationModel> locationList = new ArrayList<>();

                        for (int i = 0; i < locations.length(); i++) {
                            JSONObject loc = locations.getJSONObject(i);

                            String id = loc.optString("id");
                            String type = loc.optString("type");
                            String amenity = loc.optString("amenity");

                            JSONObject floor = loc.optJSONObject("floor");
                            double elevation = floor != null ? floor.optDouble("elevation", 0.0) : 0.0;

                            List<String> categories = new ArrayList<>();
                            JSONArray cats = loc.optJSONArray("categories");
                            if (cats != null) {
                                for (int j = 0; j < cats.length(); j++) {
                                    JSONObject cat = cats.getJSONObject(j);
                                    categories.add(cat.optString("id"));
                                }
                            }

                            locationList.add(new LocationModel(id, type, amenity, categories, elevation));
                        }

                        Log.d(TAG, "Locations received: " + locations.getString(0).toString());

                        // Optional: Process locations or pass to ViewModel here
                        break;
                    case "searchForLocations":
                        JSONObject payload = message.getJSONObject("payload");
                        JSONArray resultsArray = payload.getJSONArray("results");

                        Gson gson = new Gson();
                        List<SearchResult> searchResults = new ArrayList<>();

                        for (int i = 0; i < resultsArray.length(); i++) {
                            JSONObject obj = resultsArray.getJSONObject(i);
                            Log.e( "postMessage: ",resultsArray.toString() );
                            SearchResult result = gson.fromJson(obj.toString(), SearchResult.class);
                            searchResults.add(result);
                        }
                        // Send the result to the Fragment via callback
                        if (callback != null) {
                            callback.onSearchResultsReceived(searchResults);
                        }
                        break;
                    case "initError":
                        JSONObject error = message.getJSONObject("payload");
                        Log.e(TAG, "Init error: " + error.optString("message"));
                        break;

                    default:
                        Log.w(TAG, "Unhandled message type: " + types);
                        break;
                }
            } catch (JSONException e) {
                Log.e(TAG, "Invalid JSON received: " + messageJson, e);
            }
        }
    }

    public interface BecomapCallback {
        void onSearchResultsReceived(List<SearchResult> searchResults);
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
            webView.setWebChromeClient(null);
            webView.setWebViewClient(null);
            webView.removeJavascriptInterface("jsHandler");
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