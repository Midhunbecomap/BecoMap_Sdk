package com.becomap.sdk;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.graphics.Color;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.becomap.sdk.Model.FloorData;
import com.becomap.sdk.Viewmodel.SdkViewModel;
import com.becomap.sdk.util.TokenCallback;

import java.util.ArrayList;
import java.util.List;

public class Becomap {
    private final Context context;
    private WebView webView;
    private SdkViewModel sdkViewModel;
    private List<FloorData> floorDataList = new ArrayList<>();
    private OnFloorDataListener onFloorDataListener;
    private Spinner floorSpinner;
    private OnFloorSelectedListener onFloorSelectedListener;
    private ViewGroup rootContainer;
    private boolean isInitialSelection = true;

    public interface OnFloorDataListener {
        void onFloorDataReceived(List<FloorData> floors);
        void onFloorDataError(String error);
    }

    public interface OnFloorSelectedListener {
        void onFloorSelected(FloorData floor);
    }

    public Becomap(Context context) {
        if (context == null) throw new IllegalArgumentException("Context cannot be null");
        this.context = context;
        this.sdkViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(SdkViewModel.class);
    }

    public void setOnFloorDataListener(OnFloorDataListener listener) {
        this.onFloorDataListener = listener;
    }

    public void setOnFloorSelectedListener(OnFloorSelectedListener listener) {
        this.onFloorSelectedListener = listener;
    }

    private void createFloorSpinner() {
        if (rootContainer == null) {
            Log.e("Becomap", "Root container is null, cannot create spinner");
            return;
        }

        // Create the spinner
        floorSpinner = new Spinner(context);
        floorSpinner.setId(ViewGroup.generateViewId());

        // Set spinner layout parameters
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(16, 16, 16, 16);
        floorSpinner.setLayoutParams(params);

        // Set background and padding for better visibility
        floorSpinner.setBackgroundColor(Color.WHITE);
        floorSpinner.setPadding(16, 8, 16, 8);

        // Add spinner to the container
        rootContainer.addView(floorSpinner);

        // Set constraints
        if (rootContainer instanceof ConstraintLayout) {
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone((ConstraintLayout) rootContainer);

            // Connect spinner to parent
            constraintSet.connect(floorSpinner.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
            constraintSet.connect(floorSpinner.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);

            // Apply constraints
            constraintSet.applyTo((ConstraintLayout) rootContainer);
        } else {
            Log.e("Becomap", "Root container is not a ConstraintLayout");
        }

        // Make sure spinner is visible
        floorSpinner.setVisibility(View.VISIBLE);
    }

    public void initializeMap(ViewGroup container) {
        this.rootContainer = container;
        
        // Create and setup WebView
        webView = new WebView(context);
        container.addView(webView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        
        setupWebView();
        loadLocalHtml();
        createFloorSpinner();
    }

    private void setupWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        
        webView.setWebViewClient(new WebViewClient());
    }

    private void loadLocalHtml() {
        if (webView != null) {
            // Load the HTML file from the SDK's assets folder
            webView.loadUrl("file:///android_asset/esm-build/index.html");
        }
    }

    public void authenticate(String clientId, String clientSecret, LifecycleOwner lifecycleOwner, TokenCallback callback) {
        // Since we're using local HTML, we don't need authentication
        if (callback != null) {
            callback.onSuccess("local-mode");
        }
    }

    public void fetchFloorData(String siteId, String buildingId) {
        // Since we're using local HTML, we don't need to fetch floor data
        if (onFloorDataListener != null) {
            onFloorDataListener.onFloorDataReceived(new ArrayList<>());
        }
    }

    public List<FloorData> getFloorDataList() {
        return floorDataList;
    }

    public boolean canGoBack() {
        return webView != null && webView.canGoBack();
    }

    public void goBack() {
        if (webView != null) {
            webView.goBack();
        }
    }

    public void onStart() {}
    public void onResume() {}
    public void onPause() {}
    public void onStop() {}
    public void onDestroy() {
        if (webView != null) {
            webView.destroy();
            webView = null;
        }
    }
    public void onLowMemory() {}
    public void onSaveInstanceState(Bundle outState) {}
}