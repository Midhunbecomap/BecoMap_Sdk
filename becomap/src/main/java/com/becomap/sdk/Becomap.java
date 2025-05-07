package com.becomap.sdk;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.becomap.sdk.Viewmodel.SdkViewModel;
import com.becomap.sdk.util.TokenCallback;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;

import org.maplibre.android.MapLibre;
import org.maplibre.android.annotations.Icon;
import org.maplibre.android.annotations.IconFactory;
import org.maplibre.android.camera.CameraPosition;
import org.maplibre.android.geometry.LatLng;
import org.maplibre.android.maps.MapLibreMap;
import org.maplibre.android.maps.MapView;
import org.maplibre.android.maps.OnMapReadyCallback;
import org.maplibre.android.style.layers.PropertyFactory;
import org.maplibre.android.style.layers.SymbolLayer;
import org.maplibre.android.style.sources.GeoJsonSource;

public class Becomap {
    private final Context context;
    private MapView mapView;
    private MapLibreMap mapLibreMap;
    private static final String DEFAULT_STYLE_URL = "https://demotiles.maplibre.org/style.json";
    private String accessToken;

    public Becomap(Context context) {
        if (context == null) throw new IllegalArgumentException("Context cannot be null");
        this.context = context;
        MapLibre.getInstance(context);
    }

    public void authenticate(String clientId, String clientSecret, LifecycleOwner lifecycleOwner, TokenCallback callback) {
        SdkViewModel viewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(SdkViewModel.class);
        viewModel.generateSdkToken(clientId, clientSecret);

        // Observe the SDK token response
        viewModel.getSdkTokenResponse().observe(lifecycleOwner, response -> {
            if (response != null && response.getAccessToken() != null) {
                accessToken = response.getAccessToken();
                callback.onSuccess(accessToken);
            } else {
                callback.onFailure("Failed to retrieve token");
            }
        });

        // Observe the site response and initialize map with latitude and longitude
        viewModel.getSiteResponse().observe(lifecycleOwner, siteResponse -> {
            if (siteResponse != null) {
                double latitude = siteResponse.getLatitude();
                double longitude = siteResponse.getLongitude();
                initializeMapWithCoordinates(latitude, longitude);
            } else {
                // Handle error when site data is not fetched
                callback.onFailure("Failed to fetch site data");
            }
        });
    }

    // Initialize the map with latitude and longitude
    private void initializeMapWithCoordinates(double latitude, double longitude) {
        if (mapView != null && mapLibreMap != null) {
            LatLng location = new LatLng(latitude, longitude);

            mapLibreMap.setCameraPosition(new CameraPosition.Builder()
                    .target(location)
                    .zoom(13)
                    .build());

            // Ensure style is loaded before adding symbol
            mapLibreMap.getStyle(style -> {
                // Add the marker icon to the style
                style.addImage("marker-icon-id", BitmapFactory.decodeResource(context.getResources(), org.maplibre.android.R.drawable.maplibre_marker_icon_default));

                // Create a GeoJSON source for the marker
                String geoJson = "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[" + longitude + "," + latitude + "]}}]}";

                // Add the GeoJSON source to the style
                style.addSource(new GeoJsonSource("marker-source", geoJson));

                // Create a SymbolLayer to display the GeoJSON source
                SymbolLayer symbolLayer = new SymbolLayer("marker-layer", "marker-source")
                        .withProperties(
                                PropertyFactory.iconImage("marker-icon-id"), // Icon for the marker
                                PropertyFactory.iconSize(1.0f) // Icon size
                        );

                // Add the SymbolLayer to the map
                style.addLayer(symbolLayer);
            });
        }
    }

    // Method to initialize map with default style
    public void initializeMap(ViewGroup container) {
        initializeMap(container, DEFAULT_STYLE_URL);
    }

    // Method to initialize map with a custom style
    public void initializeMap(ViewGroup container, String styleUrl) {
        if (container == null) throw new IllegalArgumentException("Container cannot be null");

        if (styleUrl == null || styleUrl.trim().isEmpty()) {
            styleUrl = DEFAULT_STYLE_URL;
        }

        final String finalStyleUrl = styleUrl;

        mapView = new MapView(context);
        container.addView(mapView);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapLibreMap map) {
                mapLibreMap = map;
                map.setStyle(finalStyleUrl);
            }
        });
    }

    // Lifecycle methods
    public void onStart() { if (mapView != null) mapView.onStart(); }
    public void onResume() { if (mapView != null) mapView.onResume(); }
    public void onPause() { if (mapView != null) mapView.onPause(); }
    public void onStop() { if (mapView != null) mapView.onStop(); }
    public void onDestroy() { if (mapView != null) mapView.onDestroy(); }
    public void onLowMemory() { if (mapView != null) mapView.onLowMemory(); }
    public void onSaveInstanceState(Bundle outState) { if (mapView != null) mapView.onSaveInstanceState(outState); }
}