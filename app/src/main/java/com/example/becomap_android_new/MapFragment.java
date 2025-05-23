package com.example.becomap_android_new;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.becomap.sdk.UI.Becomap;
import com.becomap.sdk.model.BuildingsModels.BuildingModel;
import com.becomap.sdk.model.BuildingsModels.FloorModel;
import com.becomap.sdk.model.Category;
import com.becomap.sdk.model.Language.LanguageModel;
import com.becomap.sdk.model.LocationModel;
import com.becomap.sdk.model.Questions.BCQuestion;
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
    private TextInputLayout fromLayout;
    private TextInputLayout toLayout;
    FrameLayout mapContainer;
    private Becomap becomap;
    private boolean isProgrammaticChange = false;
    ProgressBar progressBar;
    LinearLayout ButtonLayout;
    Button btn_initialize_map,btn_floors,btn_draw_map;
    boolean btn_inizilize_clicked=false;
    boolean btn_floor_clicked=false;
    boolean btn_draw_clicked=false;
    Spinner floor_spinner;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);

        initializeViews(root);
        requireActivity().getOnBackPressedDispatcher().addCallback(
                getViewLifecycleOwner(),
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        // Custom back press logic here
                        if (btn_floor_clicked || btn_draw_clicked || btn_inizilize_clicked) {
                            btn_floor_clicked=false;
                            btn_draw_clicked=false;
                            btn_inizilize_clicked=false;
                          mapContainer.setVisibility(View.GONE);
                          searchLayout.setVisibility(View.GONE);
                          fromToLayout.setVisibility(View.GONE);
                          floor_spinner.setVisibility(View.GONE);
                          ButtonLayout.setVisibility(View.VISIBLE);
                            return; // Don't pop the fragment
                        }


                        // If nothing handled, allow default back behavior
                        setEnabled(false); // Let the system handle back press
                        requireActivity().onBackPressed();
                    }
                }
        );

        return root;
    }

    private void initializeViews(View root) {
        searchLayout = root.findViewById(R.id.searchLayout);
        searchEditText = root.findViewById(R.id.searchEditText);
        fromToLayout = root.findViewById(R.id.fromToLayout);
        fromEditText = root.findViewById(R.id.fromEditText);
        fromLayout = root.findViewById(R.id.fromLayout);
        toEditText = root.findViewById(R.id.toEditText);
        toLayout = root.findViewById(R.id.toLayout);
        locationsText = root.findViewById(R.id.locationsText);
        addStopText = root.findViewById(R.id.addStopText);
        stopsContainer = root.findViewById(R.id.stopsContainer);
        locationsRecyclerView = root.findViewById(R.id.locationsRecyclerView);
        progressBar=root.findViewById(R.id.progressbar);
        mapContainer = root.findViewById(R.id.map_container);
        ButtonLayout=root.findViewById(R.id.ButtonLayout);
        btn_initialize_map=root.findViewById(R.id.btn_initialize_map);
        btn_floors=root.findViewById(R.id.btn_floors);
        btn_draw_map=root.findViewById(R.id.btn_draw_map);
        floor_spinner = root.findViewById(R.id.spinner_floors);

        btn_initialize_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initializeBecomap();
                searchEditText.setText("");
                toEditText.setText("");
                fromEditText.setText("");
                btn_inizilize_clicked=true;
                mapContainer.setVisibility(View.VISIBLE);
                ButtonLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            }
        });

        btn_floors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initializeBecomap();
                searchEditText.setText("");
                toEditText.setText("");
                fromEditText.setText("");
                btn_floor_clicked=true;
                mapContainer.setVisibility(View.VISIBLE);
                ButtonLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            }
        });
        btn_draw_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initializeBecomap();
                btn_draw_clicked=true;
                mapContainer.setVisibility(View.VISIBLE);
                ButtonLayout.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                setupRecyclerView();
                setupSearchListeners();
                setupFromToLayout();
            }
        });


    }

    private void initializeBecomap() {
        if (getContext() == null) return;

        becomap = new Becomap(requireContext());

        // Initialize map with WebView
        becomap.initializeMap(mapContainer, "c079dfa3a77dad13351cfacd95841c2c2780fe08",
                "f62a59675b2a47ddb75f1f994d88e653",
                "67dcf5dd2f21c64e3225254f");

        setupBecomapCallback();
    }

    private void setupBecomapCallback() {
        becomap.setCallback(new Becomap.BecomapCallback() {
            @Override
            public void onSearchResultsReceived(List<SearchResult> searchResults) {
                if (getActivity() == null) return;

                getActivity().runOnUiThread(() -> {
                    Log.d("MyActivity", "Received " + searchResults.size() + " search results");
                    if (currentSelectedField == searchEditText) {
                        showsearchList(searchResults);
                    } else if (currentSelectedField == fromEditText) {
                        showforList(searchResults);
                    } else {
                        showforList(searchResults);
                    }
                    if (!searchResults.isEmpty()) {
                        Log.d(TAG, "onSearchResultsReceived: " + searchResults.get(0).id);
                    }
                });
            }

            @Override
            public void onMapRenderComplete() {
//
                if (btn_floor_clicked){
                    becomap.getFloors();
                    progressBar.setVisibility(View.GONE);
                }else if(btn_draw_clicked) {
                    searchLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }else {
                    progressBar.setVisibility(View.GONE);
                }

            }
            @Override
            public void onFloors_Received(List<FloorModel> floors) {
                if (floors == null || floors.isEmpty()) {
                    Log.e("onFloors_Received", "Received null or empty floor list");
                    floor_spinner.setVisibility(View.GONE);
                    return;
                }

                Log.e("onFloors_Received: ", floors.get(0).shortName);
                getActivity().runOnUiThread(() -> {
                    floor_spinner.setVisibility(View.VISIBLE);

                List<String> shortNames = new ArrayList<>();
                for (FloorModel floor : floors) {
                    shortNames.add(floor.getShortName());
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        getContext(),
                        R.layout.spinner_item,
                        shortNames
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                floor_spinner.setAdapter(adapter);

                floor_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        FloorModel selectedFloor = floors.get(position);
                        Log.e("onFloors_Received: sa", selectedFloor.id);
                        becomap.selectFloor(selectedFloor.id);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Optional
                    }
                });
                });
            }
            @Override
            public void onSiteIdAvailable(String siteId) {
                Log.e( "onSiteIdAvailable: ",siteId );
            }

            @Override
            public void onSiteNameAvailable(String sitename) {

            }

            @Override
            public void onBuildingsReceived(List<BuildingModel> buildingModels) {
            }

            @Override
            public void onDefaultFloorReceived(FloorModel defaultFloor) {

            }

            @Override
            public void onLanguagesReceived(List<LanguageModel> languagesa) {

            }

            @Override
            public void onCurrentFloorReceived(FloorModel current) {

            }

            @Override
            public void onCategoriesReceived(List<Category> categories) {

            }

            @Override
            public void onLocationsReceived(List<LocationModel> locations) {

            }

            @Override
            public void onAllAmenitiesReceived(List<LocationModel> amenities) {

            }

            @Override
            public void onAmenityTypesReceived(List<String> amenityTypes) {

            }

            @Override
            public void onSurveyQuestionsReceived(List<BCQuestion> questions) {

            }

            @Override
            public void onSessionIdReceived(String sessionId) {

            }



        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (becomap != null) {
            becomap.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (becomap != null) {
            becomap.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (becomap != null) {
            becomap.onDestroy();
        }
    }


    private void setupRecyclerView() {
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
                    isProgrammaticChange=true;
                    searchEditText.setText("");
                    isProgrammaticChange=false;
                    fromToLayout.setVisibility(View.VISIBLE);
                    addStopText.setVisibility(View.VISIBLE);
                    fromLayout.setVisibility(View.VISIBLE );
                    toLayout.setVisibility(View.VISIBLE);
                    toEditText.setText(location.getName());
                } else if (currentSelectedField == fromEditText) {
                    fromEditText.setText(location.getName());
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
    }

    private void setupSearchListeners() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No-op
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isProgrammaticChange) {
                    return; // Ignore programmatic changes
                }
                String query = s.toString().trim();
                currentSelectedField = searchEditText;
                if (query!=null || !query.isEmpty()) {
                    becomap.searchLocation(query); // Or debounce this if needed
                }
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
                if (isProgrammaticChange) {
                    return; // Ignore programmatic changes
                }

                String query = s.toString().trim();
                currentSelectedField = fromEditText;

                if (!query.isEmpty()) {
                    becomap.searchLocation(query);
                }

                locationsText.setVisibility(View.VISIBLE);
                // Or debounce this if needed
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Optional
            }
        });
    }

    private void setupFromToLayout() {
        // Set close icon for from field
        fromLayout.setBoxBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.white));
        fromLayout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
        fromLayout.setEndIconDrawable(android.R.drawable.ic_menu_close_clear_cancel);
        fromLayout.setEndIconOnClickListener(v -> {
            locationsRecyclerView.setVisibility(View.GONE);
            fromLayout.setVisibility(View.GONE);
            currentSelectedField=null;
            isProgrammaticChange = true;
            fromEditText.setText("");
            isProgrammaticChange = false;
            checkAndShowSearchField();
        });

        // Set close icon for to field
        toLayout.setBoxBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.white));
        toLayout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
        toLayout.setEndIconDrawable(android.R.drawable.ic_menu_close_clear_cancel);
        toLayout.setEndIconOnClickListener(v -> {
            locationsRecyclerView.setVisibility(View.GONE);
            toLayout.setVisibility(View.GONE);
            currentSelectedField=null;
            isProgrammaticChange = true;
            toEditText.setText("");
            isProgrammaticChange = false;
            checkAndShowSearchField();
        });

        // Setup add stop click listener
        addStopText.setOnClickListener(v -> addStopField());
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
            locationsRecyclerView.setVisibility(View.GONE);
            currentSelectedField=null;
            isProgrammaticChange = true;
            stopsContainer.removeView(stopLayout);
            isProgrammaticChange = false;
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
                    if (isProgrammaticChange) {
                        return; // Ignore programmatic changes
                    }
                    String query = s.toString().trim();
                    currentSelectedField = stopEditText;
                    if (query!=null || !query.isEmpty()) {
                        becomap.searchLocation(query); // Or debounce this if needed
                    }
                      locationsText.setVisibility(View.VISIBLE);
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // Optional
                }
            });
        }
        stopsContainer.addView(stopLayout);
    }

    private void showsearchList(List<SearchResult> searchResult) {
        if (searchResult != null && !searchResult.isEmpty()) {
            SearchResult firstResult = searchResult.get(0);

            List<Location> newlocation = new ArrayList<>();
            for (SearchResult store : searchResult) {
                Location location = new Location();
                location.setName(store.getName());
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
                location.setName(store.getName());
                location.setType(store.getType());
                newlocation.add(location);
            }
            Log.e( "showsearchList: ", String.valueOf(newlocation.size()));
            locationsRecyclerView.setVisibility(View.VISIBLE);
            locationAdapter.updateLocations(newlocation);
        } else {
            locationsRecyclerView.setVisibility(View.GONE);
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