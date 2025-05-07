package com.becomap.sdk;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.view.View;
import android.widget.TextView;
import android.graphics.Color;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.beco.geoshapes.Shape;
import com.becomap.sdk.Model.FloorData;
import com.becomap.sdk.Model.ShapeJsonData;
import com.becomap.sdk.Model.ShapeModel;
import com.becomap.sdk.Viewmodel.SdkViewModel;
import com.becomap.sdk.util.TokenCallback;
import com.google.gson.Gson;
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
import org.maplibre.android.style.expressions.Expression;
import org.maplibre.android.style.layers.FillLayer;
import org.maplibre.android.style.layers.PropertyFactory;
import org.maplibre.android.style.layers.SymbolLayer;
import org.maplibre.android.style.sources.GeoJsonSource;
import org.maplibre.geojson.Feature;
import org.maplibre.geojson.FeatureCollection;
import org.maplibre.geojson.Point;
import org.maplibre.geojson.Polygon;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class Becomap {
    private final Context context;
    private MapView mapView;
    private MapLibreMap mapLibreMap;
    private static final String DEFAULT_STYLE_URL = "https://demotiles.maplibre.org/style.json";
    private String accessToken;
    private SdkViewModel sdkViewModel;
    private List<FloorData> floorDataList = new ArrayList<>();
    private List<ShapeModel> shapeModelList = new ArrayList<>();
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
        MapLibre.getInstance(context);
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
        
        // Add a temporary item to make the spinner visible
        List<String> tempList = new ArrayList<>();
        tempList.add("Loading floors...");
        
        // Create custom adapter with black text color and white background
        ArrayAdapter<String> tempAdapter = new ArrayAdapter<String>(
                context,
                android.R.layout.simple_spinner_item,
                tempList
        ) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.BLACK);
                view.setBackgroundColor(Color.WHITE);
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.BLACK);
                view.setBackgroundColor(Color.WHITE);
                return view;
            }
        };
        
        tempAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        floorSpinner.setAdapter(tempAdapter);
        
        Log.d("Becomap", "Spinner created and added to container");
    }

    private void drawShapesOnMap(List<ShapeModel> shapes) {
        if (mapLibreMap == null) {
            Log.e("Becomap", "Map is not initialized");
            return;
        }

        if (shapes == null || shapes.isEmpty()) {
            Log.e("Becomap", "No shapes provided to draw");
            return;
        }

        mapLibreMap.getStyle(style -> {
            // Remove existing shape layers if any
            if (style.getLayer("shape-layer") != null) {
                style.removeLayer("shape-layer");
            }
            if (style.getSource("shape-source") != null) {
                style.removeSource("shape-source");
            }

            // Create GeoJSON features from shapes
            List<Feature> features = new ArrayList<>();
            int validShapes = 0;
            int invalidShapes = 0;

            for (ShapeModel shape : shapes) {
                try {
                    // Get the JSON data from ShapeJsonData
                    ShapeJsonData jsonData = shape.getJsonData();
                    if (jsonData != null && jsonData.getGeometry() != null) {
                        Feature feature = null;
                        String geometryType = jsonData.getGeometry().getType();
                        
                        if ("Point".equals(geometryType)) {
                            // Handle Point geometry
                            Object coords = jsonData.getGeometry().getCoordinates();
                            if (coords instanceof List) {
                                List<?> pointCoords = (List<?>) coords;
                                if (!pointCoords.isEmpty() && pointCoords.get(0) instanceof Number) {
                                    // For Point geometry, coordinates are [longitude, latitude]
                                    double longitude = ((Number) pointCoords.get(0)).doubleValue();
                                    double latitude = ((Number) pointCoords.get(1)).doubleValue();
                                    
                                    // Create a circle around the point
                                    double radius = 0.0001; // Adjust this value to change circle size
                                    List<List<Point>> circlePoints = createCircle(longitude, latitude, radius);
                                    feature = Feature.fromGeometry(Polygon.fromLngLats(circlePoints));
                                }
                            }
                        } else if ("Polygon".equals(geometryType)) {
                            // Handle Polygon geometry
                            Object coords = jsonData.getGeometry().getCoordinates();
                            if (coords instanceof List) {
                                List<?> allRings = (List<?>) coords;
                                if (!allRings.isEmpty() && allRings.get(0) instanceof List) {
                                    List<?> ring = (List<?>) allRings.get(0);
                                    List<List<Point>> polygonPoints = new ArrayList<>();
                                    List<Point> points = new ArrayList<>();
                                    
                                    for (Object coord : ring) {
                                        if (coord instanceof List) {
                                            List<?> point = (List<?>) coord;
                                            if (point.size() >= 2 && point.get(0) instanceof Number && point.get(1) instanceof Number) {
                                                double longitude = ((Number) point.get(0)).doubleValue();
                                                double latitude = ((Number) point.get(1)).doubleValue();
                                                points.add(Point.fromLngLat(longitude, latitude));
                                            }
                                        }
                                    }
                                    
                                    if (!points.isEmpty()) {
                                        polygonPoints.add(points);
                                        feature = Feature.fromGeometry(Polygon.fromLngLats(polygonPoints));
                                    }
                                }
                            }
                        }

                        if (feature != null) {
                            // Add properties to the feature
                            feature.addStringProperty("shapeId", shape.getShapeId());
                            feature.addStringProperty("type", shape.getType());
                            feature.addStringProperty("floorId", shape.getFloorId());
                            feature.addStringProperty("layerId", shape.getLayerId());

                            // Add color and height properties from shapeProperties
                            if (jsonData.getProperties() != null && 
                                jsonData.getProperties().getShapeProperties() != null) {
                                ShapeJsonData.ShapeProperties shapeProps = jsonData.getProperties().getShapeProperties();
                                double height = shapeProps.getHeight();
                                String color = shapeProps.getColor();
                                double opacity = shapeProps.getOpacity();
                                
                                // Scale up the height for better visibility (multiply by 10)
                                height = Math.max(height * 10, 1.0); // Ensure minimum height of 1.0
                                
                                feature.addNumberProperty("height", height);
                                feature.addStringProperty("color", color);
                                feature.addNumberProperty("opacity", opacity);
                                
                                Log.d("Becomap", "Shape " + shape.getType() + " height: " + height);
                            } else {
                                Log.w("Becomap", "Shape " + shape.getShapeId() + " has no height properties");
                            }
                            
                            features.add(feature);
                            validShapes++;
                            Log.d("Becomap", "Added shape: " + shape.getShapeId() + " of type: " + geometryType);
                        } else {
                            invalidShapes++;
                            Log.w("Becomap", "Could not create feature for shape: " + shape.getShapeId());
                        }
                    } else {
                        invalidShapes++;
                        Log.w("Becomap", "Invalid JSON data for shape: " + shape.getShapeId());
                    }
                } catch (Exception e) {
                    invalidShapes++;
                    Log.e("Becomap", "Error converting shape to GeoJSON: " + e.getMessage());
                }
            }

            Log.d("Becomap", "Processed shapes - Valid: " + validShapes + ", Invalid: " + invalidShapes);

            if (!features.isEmpty()) {
                // Create FeatureCollection
                FeatureCollection featureCollection = FeatureCollection.fromFeatures(features);
                String geoJson = featureCollection.toJson();

                // Add source
                style.addSource(new GeoJsonSource("shape-source", geoJson));

                // Add 3D fill layer with extrusion
                FillLayer shapeLayer = new FillLayer("shape-layer", "shape-source")
                        .withProperties(
                                PropertyFactory.fillColor(Expression.get("color")),
                                PropertyFactory.fillOpacity(Expression.get("opacity")),
                                PropertyFactory.fillOutlineColor(Expression.get("color")),
                                PropertyFactory.fillExtrusionHeight(Expression.get("height")),
                                PropertyFactory.fillExtrusionBase(0f),
                                PropertyFactory.fillExtrusionOpacity(Expression.get("opacity")),
                                PropertyFactory.fillExtrusionVerticalGradient(Expression.literal(new Float[]{0.0f, 1.0f})),
                                PropertyFactory.fillExtrusionTranslate(new Float[]{0f, 0f, 0f})
                        );

                style.addLayer(shapeLayer);
                Log.d("Becomap", "Added " + features.size() + " 3D shapes to the map");
            } else {
                Log.e("Becomap", "No valid features to add to the map");
            }
        });
    }

    private List<List<Point>> createCircle(double centerLng, double centerLat, double radius) {
        List<Point> points = new ArrayList<>();
        int segments = 32; // Number of points in the circle
        
        for (int i = 0; i <= segments; i++) {
            double angle = (2 * Math.PI * i) / segments;
            double x = centerLng + (radius * Math.cos(angle));
            double y = centerLat + (radius * Math.sin(angle));
            points.add(Point.fromLngLat(x, y));
        }
        
        List<List<Point>> result = new ArrayList<>();
        result.add(points);
        return result;
    }

    private void updateFloorSpinner() {
        if (floorSpinner == null) return;

        List<String> shortNames = new ArrayList<>();
        for (FloorData floor : floorDataList) {
            shortNames.add(floor.getShortName());
        }

        // Create custom adapter with black text color and white background
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                context,
                android.R.layout.simple_spinner_item,
                shortNames
        ) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.BLACK);
                view.setBackgroundColor(Color.WHITE);
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.BLACK);
                view.setBackgroundColor(Color.WHITE);
                return view;
            }
        };
        
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        floorSpinner.setAdapter(adapter);

        // Set listener for floor selection
        floorSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                if (floorDataList.isEmpty() || position >= floorDataList.size()) {
                    Log.e("onItemSelected: ", "floorDataList is empty");
                    return;
                }

                FloorData selectedFloor = floorDataList.get(position);
                if (onFloorSelectedListener != null) {
                    onFloorSelectedListener.onFloorSelected(selectedFloor);
                }
                
                // Filter shapes for the selected floor
                String selectedFloorId = selectedFloor.getFloorId();
                List<ShapeModel> floorShapes = new ArrayList<>();
                for (ShapeModel shape : shapeModelList) {
                    if (shape.getFloorId().equals(selectedFloorId)) {
                        floorShapes.add(shape);
                        Log.d("FloorShapes", "Found shape for floor " + selectedFloorId + 
                            ": Shape ID=" + shape.getShapeId() + 
                            ", Type=" + shape.getType() + 
                            ", Layer ID=" + shape.getLayerId());
                    }
                }
                
                Log.d("FloorSelection", "Selected floor: " + selectedFloor.getShortName() + 
                    ", Found " + floorShapes.size() + " shapes for this floor");

                // Draw the shapes on the map
                drawShapesOnMap(floorShapes);

                // Reset the initial selection flag after first selection
                if (isInitialSelection) {
                    isInitialSelection = false;
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                // Handle no selection
            }
        });

        // Force initial selection
        if (!floorDataList.isEmpty()) {
            Log.d("FloorSelection", "Forcing initial selection");
            isInitialSelection = true;
            floorSpinner.setSelection(0, true);
        }
    }

    public void fetchFloorData(String siteId, String buildingId) {
        if (accessToken == null) {
            if (onFloorDataListener != null) {
                onFloorDataListener.onFloorDataError("Not authenticated. Please authenticate first.");
            }
            return;
        }

        sdkViewModel.getBuildingResponse().observe((LifecycleOwner) context, floors -> {
            if (floors != null && !floors.isEmpty()) {
                floorDataList = floors;
                updateFloorSpinner();
                if (onFloorDataListener != null) {
                    onFloorDataListener.onFloorDataReceived(floors);
                }
            } else {
                if (onFloorDataListener != null) {
                    onFloorDataListener.onFloorDataError("Failed to fetch floor data");
                }
            }
        });

        // Trigger the building data fetch
        sdkViewModel.fetchBuildingData(accessToken, siteId, buildingId);
    }

    public List<FloorData> getFloorDataList() {
        return floorDataList;
    }

    public void authenticate(String clientId, String clientSecret, LifecycleOwner lifecycleOwner, TokenCallback callback) {
        sdkViewModel.generateSdkToken(clientId, clientSecret);

        // Observe the SDK token response
        sdkViewModel.getSdkTokenResponse().observe(lifecycleOwner, response -> {
            if (response != null && response.getAccessToken() != null) {
                accessToken = response.getAccessToken();
                callback.onSuccess(accessToken);
            } else {
                callback.onFailure("Failed to retrieve token");
            }
        });

        // Observe the site response and initialize map with latitude and longitude
        sdkViewModel.getSiteResponse().observe(lifecycleOwner, siteResponse -> {
            if (siteResponse != null) {
                double latitude = siteResponse.getLatitude();
                double longitude = siteResponse.getLongitude();
                initializeMapWithCoordinates(latitude, longitude);
                
                Executors.newSingleThreadExecutor().execute(() -> {
                    try {
                        URL url = new URL(siteResponse.getBinaryUrl().getShape());
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true);
                        conn.connect();

                        InputStream inputStream = conn.getInputStream();

                        Shape.GeoShapesList shapesList = Shape.GeoShapesList.parseFrom(inputStream);

                        shapeModelList.clear(); // Clear existing shapes
                        Gson gson = new Gson();

                        for (Shape.GeoShapes shape : shapesList.getShapesList()) {
                            Log.d("Shape", "Shape ID: " + shape.getShapeId());
                            Log.d("Shape", "Type: " + shape.getType());
                            Log.d("Shape", "JSON Data: " + shape.getJsonData());

                            ShapeJsonData jsonData = gson.fromJson(shape.getJsonData(), ShapeJsonData.class);

                            ShapeModel shapeModel = new ShapeModel(
                                    shape.getShapeId(),
                                    shape.getType(),
                                    jsonData,
                                    shape.getFloorId(),
                                    shape.getSiteId(),
                                    shape.getLayerId()
                            );

                            shapeModelList.add(shapeModel);
                        }

                        inputStream.close();
                        conn.disconnect();

                        // After shapes are loaded, trigger floor selection on main thread
                        new Handler(Looper.getMainLooper()).post(() -> {
                            if (floorSpinner != null && !floorDataList.isEmpty()) {
                                Log.d("FloorSelection", "Triggering selection after shapes loaded");
                                updateFloorSpinner(); // This will handle the initial selection
                            }
                        });

                    } catch (Exception e) {
                        Log.e("ShapeDownloader", "Error downloading or parsing shapes.bin", e);
                    }
                });

            } else {
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
                    .zoom(17)
                    .tilt(45) // Add tilt for 3D view
                    .bearing(0) // Add bearing for orientation
                    .build());

            // Ensure style is loaded before adding symbol
            mapLibreMap.getStyle(style -> {
                Log.d("Becomap", "Map initialized at location: " + latitude + "," + longitude);
            });
        }
    }

    // Method to initialize map with default style
    public void initializeMap(ViewGroup container) {
        if (container == null) {
            Log.e("Becomap", "Container is null in initializeMap");
            return;
        }
        
        Log.d("Becomap", "Initializing map with container: " + container.getClass().getSimpleName());
        this.rootContainer = container;
        
        // Initialize the map view first
        mapView = new MapView(context);
        mapView.setId(ViewGroup.generateViewId());
        
        // Set map view layout parameters
        ConstraintLayout.LayoutParams mapParams = new ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        mapView.setLayoutParams(mapParams);
        
        // Add map view to container
        container.addView(mapView);
        
        // Set up map view constraints
        if (container instanceof ConstraintLayout) {
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone((ConstraintLayout) container);
            
            // Connect map view to parent
            constraintSet.connect(mapView.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
            constraintSet.connect(mapView.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
            constraintSet.connect(mapView.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
            constraintSet.connect(mapView.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
            
            // Apply constraints
            constraintSet.applyTo((ConstraintLayout) container);
        }
        
        // Initialize the map with the default style
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapLibreMap map) {
                mapLibreMap = map;
                map.setStyle(DEFAULT_STYLE_URL, style -> {
                    // Set initial camera position with tilt
                    map.setCameraPosition(new CameraPosition.Builder()
                        .zoom(17)
                        .tilt(45)
                        .bearing(0)
                        .build());
                });
                
                Log.d("Becomap", "Map initialized with style: " + DEFAULT_STYLE_URL);
            }
        });
        
        // Create the spinner after the map is added
        createFloorSpinner();
        
        Log.d("Becomap", "Map view initialized and added to container");
    }

    // Method to initialize map with a custom style
//    public void initializeMap(ViewGroup container, String styleUrl) {
//        if (container == null) throw new IllegalArgumentException("Container cannot be null");
//
//        if (styleUrl == null || styleUrl.trim().isEmpty()) {
//            styleUrl = DEFAULT_STYLE_URL;
//        }
//
//        final String finalStyleUrl = styleUrl;
//
//        mapView = new MapView(context);
//        container.addView(mapView);
//
//        mapView.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(MapLibreMap map) {
//                mapLibreMap = map;
//                map.setStyle(finalStyleUrl);
//            }
//        });
//    }

    // Lifecycle methods
    public void onStart() { if (mapView != null) mapView.onStart(); }
    public void onResume() { if (mapView != null) mapView.onResume(); }
    public void onPause() { if (mapView != null) mapView.onPause(); }
    public void onStop() { if (mapView != null) mapView.onStop(); }
    public void onDestroy() { if (mapView != null) mapView.onDestroy(); }
    public void onLowMemory() { if (mapView != null) mapView.onLowMemory(); }
    public void onSaveInstanceState(Bundle outState) { if (mapView != null) mapView.onSaveInstanceState(outState); }
}