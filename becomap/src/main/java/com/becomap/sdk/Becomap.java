package com.becomap.sdk;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;

import org.maplibre.android.MapLibre;
import org.maplibre.android.maps.MapLibreMap;
import org.maplibre.android.maps.MapView;
import org.maplibre.android.maps.OnMapReadyCallback;

public class Becomap {
    private final Context context;
    private MapView mapView;
    private MapLibreMap mapLibreMap;
    private static final String DEFAULT_STYLE_URL = "https://demotiles.maplibre.org/style.json";

    public Becomap(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        this.context = context;
        MapLibre.getInstance(context);
    }

    public void initializeMap(ViewGroup container) {
        initializeMap(container, DEFAULT_STYLE_URL);
    }

    public void initializeMap(ViewGroup container, String styleUrl) {
        if (container == null) {
            throw new IllegalArgumentException("Container cannot be null");
        }

        if (styleUrl == null || styleUrl.trim().isEmpty()) {
            styleUrl = DEFAULT_STYLE_URL;
        }

        final String finalStyleUrl = styleUrl; // <- this line fixes the issue

        mapView = new MapView(context);
        container.addView(mapView);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapLibreMap map) {
                mapLibreMap = map;
                map.setStyle(finalStyleUrl); // use the final variable here
            }
        });
    }

    public MapLibreMap getMap() {
        return mapLibreMap;
    }

    public void onStart() {
        if (mapView != null) mapView.onStart();
    }

    public void onResume() {
        if (mapView != null) mapView.onResume();
    }

    public void onPause() {
        if (mapView != null) mapView.onPause();
    }

    public void onStop() {
        if (mapView != null) mapView.onStop();
    }

    public void onDestroy() {
        if (mapView != null) mapView.onDestroy();
    }

    public void onLowMemory() {
        if (mapView != null) mapView.onLowMemory();
    }

    public void onSaveInstanceState(Bundle outState) {
        if (mapView != null) mapView.onSaveInstanceState(outState);
    }
}