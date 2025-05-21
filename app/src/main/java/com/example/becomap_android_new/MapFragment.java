package com.example.becomap_android_new;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.becomap.sdk.UI.Becomap;
import com.becomap.sdk.model.SearchResult;
import com.example.becomap_android_new.adapter.LocationAdapter;
import com.example.becomap_android_new.model.Location;
import com.example.becomap_android_new.model.Store;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment {
    private static final String TAG = "MapFragment";
    private TextInputLayout searchLayout;
    private TextInputEditText searchEditText;
    private View fromToLayout;
    private TextInputEditText fromEditText;
    private TextInputEditText toEditText;
    private TextView locationsText;
    private TextView addStopText;
    private LinearLayout stopsContainer;
    private RecyclerView locationsRecyclerView;
    private LocationAdapter locationAdapter;
    private List<Location> allLocations = new ArrayList<>();
    private List<Store> allStores = new ArrayList<>();
    private TextInputEditText currentSelectedField;
    private int stopCount = 0;
    private TextInputLayout lastSelectedLayout;
    FrameLayout mapContainer;
    private Becomap becomap;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);

        searchLayout = root.findViewById(R.id.searchLayout);
        searchEditText = root.findViewById(R.id.searchEditText);
        fromToLayout = root.findViewById(R.id.fromToLayout);
        fromEditText = root.findViewById(R.id.fromEditText);
        TextInputLayout fromLayout = root.findViewById(R.id.fromLayout);
        toEditText = root.findViewById(R.id.toEditText);
        TextInputLayout toLayout = root.findViewById(R.id.toLayout);
        locationsText = root.findViewById(R.id.locationsText);
        addStopText = root.findViewById(R.id.addStopText);
        stopsContainer = root.findViewById(R.id.stopsContainer);
        locationsRecyclerView = root.findViewById(R.id.locationsRecyclerView);
// Initialize views
        mapContainer = root.findViewById(R.id.map_container);

        // Initialize Becomap
        becomap = new Becomap(getContext());

        // Initialize map with WebView
        becomap.initializeMap(mapContainer, "c079dfa3a77dad13351cfacd95841c2c2780fe08",
                "f62a59675b2a47ddb75f1f994d88e653",
                "67dcf5dd2f21c64e3225254f");


        // Setup RecyclerView
        locationsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        locationAdapter = new LocationAdapter(new ArrayList<>(), location -> {
            Log.e( "currentSelectedField: ", String.valueOf(currentSelectedField));
            if (location != null) {
                locationsText.setVisibility(View.GONE);
                if (isDuplicateEntry(location.getName(), location.getType())) {
                    Toast.makeText(requireContext(), "You selected duplicate entry", Toast.LENGTH_SHORT).show();
                    locationsRecyclerView.setVisibility(View.GONE);
                    locationsText.setVisibility(View.GONE);
                    return;
                }
                if (currentSelectedField == searchEditText) {
                    searchLayout.setVisibility(View.GONE);
                    fromToLayout.setVisibility(View.VISIBLE);
                    addStopText.setVisibility(View.VISIBLE);
                    fromLayout.setVisibility(View.VISIBLE );
                    toLayout.setVisibility(View.VISIBLE);
                    toEditText.setText(location.getName());
                } else if (currentSelectedField == fromEditText) {
                    fromEditText.setText(location.getName() + " (" + location.getType() + ")");
                    locationsText.setVisibility(View.GONE);
                    addStopText.setVisibility(View.VISIBLE);
                    Log.e( "addStopText: ","shown" );

                } else if (currentSelectedField == toEditText) {
                    toEditText.setText(location.getName());
                } else {
                    // Handle stop field selection
                    currentSelectedField.setText(location.getName() + " (" + location.getType() + ")");
                }
                locationsRecyclerView.setVisibility(View.GONE);
            }
        });
        locationsRecyclerView.setAdapter(locationAdapter);
        becomap.setCallback(new Becomap.BecomapCallback() {
            @Override
            public void onSearchResultsReceived(List<SearchResult> searchResults) {
                Log.d("MyActivity", "Received " + searchResults.size() + " search results");
                if (currentSelectedField == searchEditText) {
                    showsearchList(searchResults);
                }
                else if (currentSelectedField == fromEditText)
                {
                    showforList(searchResults);
                }
                else {
                    showforList(searchResults);
                }
                Log.d(TAG, "onSearchResultsReceived: "+searchResults.get(0).id);
            }
        });
        // Load locations and stores
//        loadLocations();
//        loadStores();
//         Setup search field click listener
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No-op
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                currentSelectedField = searchEditText;
                becomap.searchLocation(query); // Or debounce this if needed
//                showLocationList();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Optional
            }
        });

        fromEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No-op
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                currentSelectedField = fromEditText;
                becomap.searchLocation(query);
                locationsText.setVisibility(View.VISIBLE);
                // Or debounce this if needed
//                showLocationList();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Optional
            }
        });
        // Setup from field click listener
//        fromEditText.setOnClickListener(v -> {
//
//        });

        // Setup to field click listener
//        toEditText.setOnClickListener(v -> {
//            currentSelectedField = toEditText;
//            showLocationList();
//            locationsText.setVisibility(View.VISIBLE);
//        });

        // Setup add stop click listener
        addStopText.setOnClickListener(v -> addStopField());

        // Set close icon for from field
        fromLayout.setBoxBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.white));
        fromLayout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
        fromLayout.setEndIconDrawable(android.R.drawable.ic_menu_close_clear_cancel);
        fromLayout.setEndIconOnClickListener(v -> {
            fromLayout.setVisibility(View.GONE);
            fromEditText.setText("");
            checkAndShowSearchField();
        });

        // Set close icon for to field\
        toLayout.setBoxBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.white));
        toLayout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
        toLayout.setEndIconDrawable(android.R.drawable.ic_menu_close_clear_cancel);
        toLayout.setEndIconOnClickListener(v -> {
            toLayout.setVisibility(View.GONE);
            toEditText.setText("");
            checkAndShowSearchField();
        });

        return root;
    }

    private void addStopField() {
        stopCount++;
        String stopName = "Stop " + stopCount;

        LayoutInflater inflater = LayoutInflater.from(requireContext());
        TextInputLayout stopLayout = (TextInputLayout) inflater.inflate(R.layout.stop_field_layout, stopsContainer, false);
        stopLayout.setHint(stopName);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 8);
        stopLayout.setLayoutParams(layoutParams);
        stopLayout.setHintEnabled(false);

        // Add close icon to stop field
        stopLayout.setBoxBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.white));
        stopLayout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
        stopLayout.setEndIconDrawable(android.R.drawable.ic_menu_close_clear_cancel);
        stopLayout.setEndIconOnClickListener(v -> {
            stopsContainer.removeView(stopLayout);
            checkAndShowSearchField();
        });

        TextInputEditText stopEditText = (TextInputEditText) stopLayout.getEditText();

        if (stopEditText != null) {
            stopEditText.setHint(stopName);
            stopEditText.setId(View.generateViewId());
            stopEditText.setFocusable(true);
            stopEditText.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
            stopEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // No-op
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String query = s.toString().trim();
                    currentSelectedField = stopEditText;
                     becomap.searchLocation(query);
                      locationsText.setVisibility(View.VISIBLE);
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // Optional
                }
            });
//            stopEditText.setOnClickListener(v -> {
//                currentSelectedField = stopEditText;
//                becomap.searchLocation(query);
//                locationsText.setVisibility(View.VISIBLE);
//            });
        }
        stopsContainer.addView(stopLayout);
    }
    private void showsearchList(List<SearchResult> searchResult) {
        if (searchResult != null && !searchResult.isEmpty()) {
            SearchResult firstResult = searchResult.get(0);

            List<Location> newlocation = new ArrayList<>();
            for (SearchResult store : searchResult) {
                Location location = new Location();
                location.setName(store.getId());
                location.setType(store.getType());
                newlocation.add(location);
            }
            Log.e( "showsearchList: ", String.valueOf(newlocation.size()));
            locationsRecyclerView.setVisibility(View.VISIBLE);
            locationAdapter.updateLocations(newlocation);
        } else {
            Log.e("showsearchList", "Empty or null search result");
            locationsRecyclerView.setVisibility(View.GONE);
            Toast.makeText(requireContext(), "No locations available", Toast.LENGTH_SHORT).show();
        }
    }
    private void showforList(List<SearchResult> searchResult) {
        if (searchResult != null && !searchResult.isEmpty()) {
            SearchResult firstResult = searchResult.get(0);

            List<Location> newlocation = new ArrayList<>();
            for (SearchResult store : searchResult) {
                Location location = new Location();
                location.setName(store.getId());
                location.setType(store.getType());
                newlocation.add(location);
            }
            Log.e( "showsearchList: ", String.valueOf(newlocation.size()));
            locationsRecyclerView.setVisibility(View.VISIBLE);
            locationAdapter.updateLocations(newlocation);
        } else {
//            Log.e("showsearchList", "Empty or null search result");
            locationsRecyclerView.setVisibility(View.GONE);
//            Toast.makeText(requireContext(), "No locations available", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean isDuplicateEntry(String storeName, String storeType) {
        String fullText = (storeName.trim() + " (" + storeType.trim() + ")").toLowerCase();
        if (fromEditText.getText() != null && fromEditText.getText().toString().trim().toLowerCase().equals(fullText)) {
            return true;
        }
        if (toEditText.getText() != null && toEditText.getText().toString().trim().toLowerCase().equals(fullText)) {
            return true;
        }
        for (int i = 0; i < stopsContainer.getChildCount(); i++) {
            View child = stopsContainer.getChildAt(i);
            if (child instanceof TextInputLayout) {
                TextInputEditText editText = (TextInputEditText) ((TextInputLayout) child).getEditText();
                if (editText != null && editText.getText() != null && editText.getText().toString().trim().toLowerCase().equals(fullText)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void checkAndShowSearchField() {
        boolean fromGone = getView() != null && getView().findViewById(R.id.fromLayout) != null && getView().findViewById(R.id.fromLayout).getVisibility() != View.VISIBLE;
        boolean toGone = getView() != null && getView().findViewById(R.id.toLayout) != null && getView().findViewById(R.id.toLayout).getVisibility() != View.VISIBLE;
        if (fromGone && toGone )
        {
            addStopText.setVisibility(View.VISIBLE);
            Log.e( "addStopText: ","shown c" );
        }else {
            addStopText.setVisibility(View.GONE);
            Log.e( "addStopText: ","hide c" );
        }


        boolean stopsGone = stopsContainer.getChildCount() == 0;
        if (fromGone && toGone && stopsGone) {
            searchLayout.setVisibility(View.VISIBLE);
            addStopText.setVisibility(View.GONE);
            Log.e( "addStopText: ","shown c s" );
            locationsText.setVisibility(View.GONE);
        }
    }

}