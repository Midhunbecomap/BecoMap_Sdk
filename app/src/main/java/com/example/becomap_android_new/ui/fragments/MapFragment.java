package com.example.becomap_android_new.ui.fragments;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.becomap.sdk.model.Route;
import com.becomap.sdk.model.SearchResult;
import com.becomap.sdk.model.Step;
import com.bumptech.glide.Glide;
import com.example.becomap_android_new.R;
import com.example.becomap_android_new.adapter.CategoryAdapter;
import com.example.becomap_android_new.adapter.FloorAdapter;
import com.example.becomap_android_new.adapter.LocationAdapter;
import com.example.becomap_android_new.adapter.SearchResultAdapter;
import com.example.becomap_android_new.model.Location;
import com.example.becomap_android_new.model.Store;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


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

    ConstraintLayout add_stop_button;

    private LinearLayout stopsContainer;
    private RecyclerView locationsRecyclerView;
    private LocationAdapter locationAdapter;
    private SearchResultAdapter searchResultAdapter;
    private List<Location> allLocations = new ArrayList<>();
    private List<Store> allStores = new ArrayList<>();
    private TextInputEditText currentSelectedField;
    private int stopCount = 0;
    private TextInputLayout lastSelectedLayout;
    private LinearLayout fromLayout;
    private LinearLayout toLayout;
    FrameLayout mapContainer;
    private Becomap becomap;
    private boolean isProgrammaticChange = false;
    ImageView progressBar;
//    Spinner floor_spinner;
    MaterialButton floor,start,end_button;
    private RecyclerView floorRecycler;
    private FloorAdapter adapter;
    private CategoryAdapter categoryAdapter;
    String startid="";
    String toid="";
    List<String> waypoints=new ArrayList<>();
    ImageButton go;
    ImageButton stepback;
    int Total_index=0;
    int current_index=0;
    private List<Route> routeLists = new ArrayList<>();
    private List<Step> allSteps = new ArrayList<>();
    boolean location_selected=true;
    int inside=0;
    ImageView fromEditText_close,toEditText_close;
    RecyclerView searchRecyclerView;
    CardView searchEditText_layout;
    ConstraintLayout direction_layout,step_direction_layout,Start_layout;


    TextView most_popular_text,locationtext_search;

    private BottomSheetBehavior<View> bottomSheetBehavior;

    boolean first_load=true;
    ImageView default_icon,escalator,left,lift,right,slight_left,slight_right,stright,stair;
    TextView distance_text,direction_text,estimated_time,estimated_time_start,estimated_distance_start;
    ImageView close_fromto_layout;
    LocationModel storelocation;
    List<LocationModel> getalllocation;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);
        if (getArguments() != null) {
            storelocation = (LocationModel) getArguments().getSerializable("location");
            getArguments().clear();
            // Now you can use this location
        }else {
            storelocation=null;
        }
        initializeViews(root);
        return root;
    }

    private void initializeViews(View root) {
//        searchLayout = root.findViewById(R.id.searchLayout);
        searchEditText = root.findViewById(R.id.searchEditText);
        most_popular_text=root.findViewById(R.id.most_popular_text);
        locationtext_search=root.findViewById(R.id.locationtext);
        direction_layout=root.findViewById(R.id.direction_layout);
        default_icon=root.findViewById(R.id.default_icon);
        close_fromto_layout=root.findViewById(R.id.close_fromto_layout);
        escalator = root.findViewById(R.id.escalator);
        left=root.findViewById(R.id.left);
        lift=root.findViewById(R.id.lift);
        right=root.findViewById(R.id.right);
        slight_left=root.findViewById(R.id.slight_left);
        slight_right=root.findViewById(R.id.slight_right);
        stright=root.findViewById(R.id.stright);
        stair=root.findViewById(R.id.stair);
        distance_text=root.findViewById(R.id.distance_text);
        estimated_distance_start=root.findViewById(R.id.estimated_distance_start);
        direction_text=root.findViewById(R.id.direction_text);
        estimated_time=root.findViewById(R.id.estimated_time);
        estimated_time_start=root.findViewById(R.id.estimated_time_start);
        step_direction_layout=root.findViewById(R.id.step_direction_layout);
        most_popular_text.setVisibility(View.GONE);
        locationtext_search.setVisibility(View.GONE);
        Log.e( "locationtext_search: 0","gone" );
        searchEditText_layout=root.findViewById(R.id.searchEditText_layout);
        fromToLayout = root.findViewById(R.id.fromToLayout);
        fromEditText = root.findViewById(R.id.fromEditText);
        fromLayout = root.findViewById(R.id.fromLayout);
        Start_layout = root.findViewById(R.id.Start_layout);
        toEditText = root.findViewById(R.id.toEditText);
        toLayout = root.findViewById(R.id.toLayout);
        locationsText = root.findViewById(R.id.locationsText_from);
        addStopText = root.findViewById(R.id.addStopText);
        add_stop_button=root.findViewById(R.id.add_stop_button);
        stopsContainer = root.findViewById(R.id.stopsContainer);
        locationsRecyclerView = root.findViewById(R.id.locationsRecyclerView);
        searchRecyclerView=root.findViewById(R.id.searchRecyclerView);
        progressBar=root.findViewById(R.id.progressbar);
        Glide.with(this)
                .asGif()
                .load(R.drawable.becomaploader) // your loading.gif in res/raw
                .into(progressBar);
        mapContainer = root.findViewById(R.id.map_container);
        fromEditText_close=root.findViewById(R.id.fromEditText_close);
        toEditText_close=root.findViewById(R.id.toEditText_close);
//        floor_spinner = root.findViewById(R.id.spinner_floors);
        floor=root.findViewById(R.id.floor);
        start=root.findViewById(R.id.start);
        end_button=root.findViewById(R.id.end_button);
        floorRecycler = root.findViewById(R.id.floor_recycler);
        go=root.findViewById(R.id.go_button);
        stepback=root.findViewById(R.id.back_button);
        floor.setVisibility(View.GONE);
        floorRecycler.setVisibility(View.GONE);
        initializeBecomap();
        setupRecyclerView();
        setupsearchRecyclerView();
        setupSearchListeners();
        setupFromToLayout();
        mapContainer.setVisibility(View.VISIBLE);
        floor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                floorRecycler.setVisibility(View.VISIBLE);
                Log.e(TAG, "onClick: visible" );
            }
        });
        close_fromto_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromToLayout.setVisibility(View.GONE);
                Start_layout.setVisibility(View.GONE);
                searchEditText_layout.setVisibility(View.VISIBLE);
                isProgrammaticChange=true;
                fromEditText.setText("");
                toEditText.setText("");
                stopsContainer.removeAllViews();
                startid="";
                toid="";
                waypoints.clear();
                isProgrammaticChange=false;
                getActivity().runOnUiThread(() -> {
                    becomap.clearallroutes();
                });
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                step_direction_layout.setVisibility(View.VISIBLE);
                go.setVisibility(View.VISIBLE);
                stepback.setVisibility(View.VISIBLE);
                fromToLayout.setVisibility(View.GONE);
                floor.setVisibility(View.GONE);
                end_button.setVisibility(View.VISIBLE);
                direction_layout.setVisibility(View.VISIBLE);
            }
        });
        end_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                step_direction_layout.setVisibility(View.GONE);
                fromToLayout.setVisibility(View.VISIBLE);
                direction_layout.setVisibility(View.GONE);
                floor.setVisibility(View.VISIBLE);
                end_button.setVisibility(View.GONE);
                getActivity().runOnUiThread(() -> {
                    becomap.showStep(0);
                    current_index=0;
//                    becomap.clearallroutes();
                });
            }
        });
        go.setOnClickListener(view -> {
            getActivity().runOnUiThread(() -> {
                if (current_index < allSteps.size()) {
                    Step currentStep = allSteps.get(current_index);
                    default_icon.setVisibility(View.GONE);
                    escalator.setVisibility(View.GONE);
                    left.setVisibility(View.GONE);
                    lift.setVisibility(View.GONE);
                    right.setVisibility(View.GONE);
                    slight_left.setVisibility(View.GONE);
                    slight_right.setVisibility(View.GONE);
                    stright.setVisibility(View.GONE);
                    stair.setVisibility(View.GONE);
                    becomap.showStep(current_index);

                    // Create toast message
                    String action = currentStep.getAction();
                    String direction = currentStep.getDirection();
                    double distanceInFeet = currentStep.getDistance() * 3.28084;
                    String formattedDistance = String.format("%.1f", distanceInFeet);
                    String toastMessage = action;
                    distance_text.setText(formattedDistance+"ft");
                    Log.e( "direction: ",direction );
                    if (direction.equals("Right"))
                    {
                        right.setVisibility(view.VISIBLE);
                    } else if (direction.equals("Left")) {
                        left.setVisibility(view.VISIBLE);
                    }
                    else if (direction.equals("SlightRight")) {
                        slight_right.setVisibility(view.VISIBLE);
                    }
                    else if (direction.equals("SlightLeft")) {
                        slight_left.setVisibility(view.VISIBLE);
                    }else if (direction.equals("Straight")) {
                        stright.setVisibility(view.VISIBLE);
                    }else if (direction.equals("Up")||direction.equals("Down")) {
                        String Amenities="";
                        for (int i=0;i<getalllocation.size();i++){
                            if(currentStep.getReferenceLocationId().equals(getalllocation.get(i).getId())){
                                Amenities=getalllocation.get(i).getAmenity().toString();
                            }
                        }
                        if (Amenities.equals("ESCALATOR") || Amenities.equals("ELEVATOR")) {
                            escalator.setVisibility(view.VISIBLE);
                        }else if (Amenities.equals("STAIRS")){
                            stair.setVisibility(View.VISIBLE);
                        }else if (Amenities.equals("LIFTS")){
                            lift.setVisibility(View.VISIBLE);
                        }else {
                            default_icon.setVisibility(View.VISIBLE);
                        }
                    }
                    else {
                        default_icon.setVisibility(view.VISIBLE);
                    }
                    if (!"None".equalsIgnoreCase(direction)) {
                        toastMessage += " " + direction;
                        direction_text.setText(toastMessage);
                        direction_text.setVisibility(View.VISIBLE);
                    }else {
                        direction_text.setVisibility(View.GONE);
                        distance_text.setText(action);
                    }


                    current_index++;
                    stepback.setVisibility(View.VISIBLE);
                } else {
                    go.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "You have reached your destination", Toast.LENGTH_SHORT).show();
                }
            });
        });

        stepback.setOnClickListener(view -> {
            getActivity().runOnUiThread(() -> {
                if (current_index > 0) {
                    current_index--; // Step back

                    Step currentStep = allSteps.get(current_index);
                    becomap.showStep(current_index);

                    // Hide all direction icons first
                    default_icon.setVisibility(View.GONE);
                    escalator.setVisibility(View.GONE);
                    left.setVisibility(View.GONE);
                    lift.setVisibility(View.GONE);
                    right.setVisibility(View.GONE);
                    slight_left.setVisibility(View.GONE);
                    slight_right.setVisibility(View.GONE);
                    stright.setVisibility(View.GONE);
                    stair.setVisibility(View.GONE);

                    // Set direction
                    String action = currentStep.getAction();
                    String direction = currentStep.getDirection();
                    double distanceInFeet = currentStep.getDistance() * 3.28084;
                    String formattedDistance = String.format("%.1f", distanceInFeet);
                    String toastMessage = action;

                    distance_text.setText(formattedDistance);
                    Log.e("Back direction: ", direction);

                    // Show correct icon
                    if ("Right".equals(direction)) {
                        right.setVisibility(View.VISIBLE);
                    } else if ("Left".equals(direction)) {
                        left.setVisibility(View.VISIBLE);
                    } else if ("SlightRight".equals(direction)) {
                        slight_right.setVisibility(View.VISIBLE);
                    } else if ("SlightLeft".equals(direction)) {
                        slight_left.setVisibility(View.VISIBLE);
                    } else if ("Straight".equals(direction)) {
                        stright.setVisibility(View.VISIBLE);
                    }else if (direction.equals("Up")||direction.equals("Down")) {
                        String Amenities="";
                        for (int i=0;i<getalllocation.size();i++){
                            if(currentStep.getReferenceLocationId().equals(getalllocation.get(i).getId())){
                                Amenities=getalllocation.get(i).getAmenity().toString();
                            }
                        }
                        if (Amenities.equals("ESCALATOR") || Amenities.equals("ELEVATOR")) {
                            escalator.setVisibility(view.VISIBLE);
                        }else if (Amenities.equals("STAIRS")){
                            stair.setVisibility(View.VISIBLE);
                        }else if (Amenities.equals("LIFTS")){
                            lift.setVisibility(View.VISIBLE);
                        }else {
                            default_icon.setVisibility(View.VISIBLE);
                        }
                    } else {
                        default_icon.setVisibility(View.VISIBLE);
                    }

                    // Show text
                    if (!"None".equalsIgnoreCase(direction)) {
                        toastMessage += " " + direction;
                        direction_text.setText(toastMessage);
                        direction_text.setVisibility(View.VISIBLE);
                    } else {
                        direction_text.setVisibility(View.GONE);
                        distance_text.setText(action);
                    }
                    go.setVisibility(View.VISIBLE);
                } else {
                    stepback.setVisibility(View.GONE);
                }
            });
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
                becomap.getlocation();
                if (storelocation != null ) {
                    floor.setVisibility(View.GONE);
                    SearchResult result=convertLocationToSearchResult(storelocation);
                    becomap.selectLocationWithId(result.getId());
                    setupBottomSheet();
                    showLocationBottomSheet(result);
                    progressBar.setVisibility(View.GONE);
                } else {
                    becomap.getFloors();
                    progressBar.setVisibility(View.GONE);
                    searchEditText_layout.setVisibility(View.VISIBLE);
                }

            }
            @Override
            public void onFloors_Received(List<FloorModel> floors) {
                if (floors == null || floors.isEmpty()) {
                    Log.e("onFloors_Received", "Received null or empty floor list");
                    return;
                }

                Log.e("onFloors_Received: ", floors.get(0).shortName);
                getActivity().runOnUiThread(() -> {

                List<String> shortNames = new ArrayList<>();
                for (FloorModel floor : floors) {
                    shortNames.add(floor.getShortName());
                }
                    floor.setText(shortNames.get(0));

                floor.setVisibility(View.VISIBLE);
                    adapter = new FloorAdapter(getActivity(), floors, new FloorAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(FloorModel floorModel) {
                            // Get the details of the item here
                            String floorName = floorModel.getShortName();
                            becomap.selectFloor(floorModel.id);
                            floor.setText(floorName);
                            floorRecycler.setVisibility(View.GONE);
                            // Do something with the floorName
                        }
                    });
                    floorRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                    floorRecycler.setAdapter(adapter);
                });
            }

            @Override
            public void ongetroute(List<Route> routeList) {
                double distance =routeList.get(0).getDistance() ;
                int timeInSeconds = WalkingTimeCalculator.calculateWalkingTimeInSeconds(distance);
                String formattedTime = WalkingTimeCalculator.formatTime(timeInSeconds);
                estimated_time.setText(formattedTime);
                estimated_time_start.setText(formattedTime);
                double distanceInFeet =distance * 3.28084;
                String formattedDistance = String.format("%.1f", distanceInFeet);
                estimated_distance_start.setText(formattedDistance+" ft");
                routeLists = routeList;
                allSteps.clear();
                for (Route route : routeList) {
                    allSteps.addAll(route.getSteps());
                }
                current_index = 0;
                Log.e("ongetroute: ", String.valueOf(routeList.get(0).getDistance()));
                Log.e( "ongetroute: ", String.valueOf(routeList.get(0).getSteps().size()));
                getActivity().runOnUiThread(() -> {
                becomap.showroute();
                    Start_layout.setVisibility(View.VISIBLE);

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
                getalllocation=locations;
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
    public class WalkingTimeCalculator {

        // Average walking speed in meters per second
        private static final double AVERAGE_WALKING_SPEED = 1.4;

        // Returns walking time in seconds
        public static int calculateWalkingTimeInSeconds(double distanceInMeters) {
            return (int) Math.round(distanceInMeters / AVERAGE_WALKING_SPEED);
        }

        // Optional: Convert seconds to a readable format (e.g., "1 min 15 sec")
        public static String formatTime(int seconds) {
            int minutes = seconds / 60;
            int remainingSeconds = seconds % 60;
            if (minutes > 0) {
                return minutes + " min " + remainingSeconds + " sec";
            } else {
                return remainingSeconds + " sec";
            }
        }
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
    private void setupsearchRecyclerView() {
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        searchResultAdapter = new SearchResultAdapter(new ArrayList<>(), location -> {
            searchRecyclerView.setVisibility(View.GONE);
            searchEditText_layout.setVisibility(View.GONE);
            isProgrammaticChange=true;
            searchEditText.setText("");
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
            }
            isProgrammaticChange=false;
            most_popular_text.setVisibility(View.GONE);
            locationtext_search.setVisibility(View.GONE);
            Log.e( "locationtext_search: 2","gone" );
            floor.setVisibility(View.GONE);
            becomap.selectLocationWithId(location.getId());
            setupBottomSheet();
            showLocationBottomSheet(location);
        });
        searchRecyclerView.setAdapter(searchResultAdapter);
    }
    private void setupBottomSheet() {
        View bottomSheet = getView().findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        View additionalContent = bottomSheet.findViewById(R.id.additional_content);

        // Calculate peek height based on initial content
        bottomSheet.post(() -> {
            ConstraintLayout initialContent = bottomSheet.findViewById(R.id.initial_content);
            initialContent.post(() -> {
                int peekHeight = initialContent.getHeight() +
                        (int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                16, // padding
                                getResources().getDisplayMetrics());

                bottomSheetBehavior.setPeekHeight(peekHeight, true);
            });
        });

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    additionalContent.setVisibility(View.VISIBLE);
                    additionalContent.setAlpha(1f);
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    additionalContent.setAlpha(0f);
                    additionalContent.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // Optional: Fade in/out additional content during drag
                additionalContent.setVisibility(View.VISIBLE);
                additionalContent.setAlpha(Math.max(0f, Math.min(1f, (slideOffset - 0.5f) * 2)));
            }
        });

        // Make sure the BottomSheet is draggable
        bottomSheetBehavior.setDraggable(true);
        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }


    private void showLocationBottomSheet(SearchResult location) {
        View bottomSheet = getView().findViewById(R.id.bottom_sheet);
        bottomSheet.setVisibility(View.VISIBLE);
        // Update content
        TextView nameTextView = bottomSheet.findViewById(R.id.nameTextView);
        TextView typeTextView = bottomSheet.findViewById(R.id.type);
        MaterialButton directions=bottomSheet.findViewById(R.id.directions);
        CardView banner_layout=bottomSheet.findViewById(R.id.banner_layout);
        ImageView banner=bottomSheet.findViewById(R.id.banner);
        TextView categoriesTitle=bottomSheet.findViewById(R.id.categoriesTitle);
        CardView category_layout=bottomSheet.findViewById(R.id.category_layout);
        RecyclerView categoriesRecyclerView=bottomSheet.findViewById(R.id.categoriesRecyclerView);
        if (location.getBanner() == null || location.getBanner().isEmpty()) {
            banner_layout.setVisibility(View.GONE);
        }else{
            banner_layout.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                .load(location.getBanner()) // Replace with location.getImageUrl() if available
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(banner);
        }
        Log.e( "showLocationBottomSheet: ", String.valueOf(location.categories.size()));
        Log.e( "showLocationBottomSheet: ", location.categories.get(0).getName());
        if (location.getCategories().equals(null) || location.getCategories().isEmpty())
        {
            categoriesTitle.setVisibility(View.GONE);
            categoriesRecyclerView.setVisibility(View.GONE);
            category_layout.setVisibility(View.GONE);
        }else{
            try {
                categoriesTitle.setVisibility(View.VISIBLE);
                categoriesRecyclerView.setVisibility(View.VISIBLE);
                category_layout.setVisibility(View.VISIBLE);
                categoriesRecyclerView.setLayoutManager(
                        new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false)
                );
                categoriesRecyclerView.setAdapter(new CategoryAdapter(location.getCategories()));
            }catch (Exception e){

            }
        }
        nameTextView.setText(location.getName());
        typeTextView.setText(location.getType());
//        Glide.with(getActivity())
//                .load(location.getLogo()) // Replace with location.getImageUrl() if available
//                .placeholder(R.drawable.ic_launcher_background)
//                .error(R.drawable.ic_launcher_background)
//                .into(logoImageView);
        directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheet.setVisibility(View.GONE);
                toid=location.getId();
                fromToLayout.setVisibility(View.VISIBLE);
                fromLayout.setVisibility(View.VISIBLE );
                toLayout.setVisibility(View.VISIBLE);
                toEditText.setText(location.getName());

            }
        });

        // Show bottom sheet
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
    private void setupRecyclerView() {
        locationsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        locationAdapter = new LocationAdapter(new ArrayList<>(), location -> {
            Log.e( "currentSelectedField: ", String.valueOf(currentSelectedField));
            if (location != null) {

                becomap.selectLocationWithId(location.getId());
                locationsText.setVisibility(View.GONE);
                if (isDuplicateEntry(location.getId())){
                    Toast.makeText(requireContext(), "You selected duplicate entry", Toast.LENGTH_SHORT).show();
                    locationsRecyclerView.setVisibility(View.GONE);
                    locationsText.setVisibility(View.GONE);
                    return;
                }
                if (currentSelectedField == searchEditText) {
                    searchEditText_layout.setVisibility(View.GONE);
                    isProgrammaticChange=true;
                    searchEditText.setText("");
                    toid=location.getId();
                    isProgrammaticChange=false;
                    fromToLayout.setVisibility(View.VISIBLE);
                    fromLayout.setVisibility(View.VISIBLE );
                    toLayout.setVisibility(View.VISIBLE);
                    toEditText.setText(location.getName());
                } else if (currentSelectedField == fromEditText) {
                    fromEditText.setText(location.getName());
                    InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(fromEditText.getWindowToken(), 0);
                    }
                    locationsText.setVisibility(View.GONE);
                    locationsRecyclerView.setVisibility(View.GONE);
                    locationsText.setVisibility(View.GONE);
                    Log.e( "locationsRecyclerView: set","gone" );
                    add_stop_button.setVisibility(View.VISIBLE);
                    Log.e( "addStopText: ","shown" );
                    startid=location.getId();
                    location_selected=false;
                    Log.e("location_selected_true ","yes" );
                    if (!startid.isEmpty() && !startid.equals("") && !toid.isEmpty() && !toid.equals(""))
                    {
                        becomap.getroute(startid,toid,null);
                    }
                    Log.e( "angry bird 3 ",String.valueOf(startid) );
                } else if (currentSelectedField == toEditText) {
                    toEditText.setText(location.getName());
                    InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(toEditText.getWindowToken(), 0);
                    }
                } else {
                    View parent = (View) currentSelectedField.getParent().getParent();
                    if (parent != null) {
                        parent.setTag(location.getId());
                    }
                    waypoints.add(location.getId());
                    if (!startid.isEmpty() && !startid.equals("")
                            && !toid.isEmpty() && !toid.equals("") &&
                             !waypoints.isEmpty() && !waypoints.equals(null))
                    {
                        becomap.getroute(startid,toid,waypoints);
                    }
                    // Handle stop field selection
                    currentSelectedField.setText(location.getName() );
                }
                locationsRecyclerView.setVisibility(View.GONE);
                locationsText.setVisibility(View.GONE);
                Log.e( "locationsRecyclerView: set2 ","gone" );
            }
        });
        locationsRecyclerView.setAdapter(locationAdapter);
    }

    private void setupSearchListeners() {
        searchEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && searchEditText.getText().toString().trim().isEmpty()) {
                currentSelectedField = searchEditText;
                most_popular_text.setVisibility(View.VISIBLE);
                becomap.searchLocation(""); // Trigger empty search on focus

            }
        });

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
                if (first_load){
                    first_load=false;
                    return;
                }
                String query = s.toString().trim();

                currentSelectedField = searchEditText;
                if (query!=null || !query.isEmpty()) {
                    most_popular_text.setVisibility(View.GONE);
                    locationtext_search.setVisibility(View.VISIBLE);
                    Log.e( "locationtext_search: 1","Visible" );
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
        toEditText.addTextChangedListener(new TextWatcher() {
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
                currentSelectedField = toEditText;

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

        fromEditText_close.setOnClickListener(v -> {
            locationsRecyclerView.setVisibility(View.GONE);
            locationsText.setVisibility(View.GONE);
            startid.equals("");
            Log.e( "locationsRecyclerView: setlay","gone" );
            fromLayout.setVisibility(View.GONE);
            currentSelectedField=null;
            isProgrammaticChange = true;
            fromEditText.setText("");
            isProgrammaticChange = false;
            checkAndShowSearchField();
        });

        // Set close icon for to field
        toEditText_close.setOnClickListener(v -> {
            locationsRecyclerView.setVisibility(View.GONE);
            locationsText.setVisibility(View.GONE);
            Log.e( "locationsRecyclerView: setlay2","gone" );
            toLayout.setVisibility(View.GONE);
            currentSelectedField=null;
            isProgrammaticChange = true;
            toEditText.setText("");
            toid="";
            isProgrammaticChange = false;
            checkAndShowSearchField();
        });

        // Setup add stop click listener
        add_stop_button.setOnClickListener(v -> addStopField());
    }

    private void addStopField() {
        stopCount++;
        String stopName = "Stop " + stopCount;

        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View stopFieldView = inflater.inflate(R.layout.stop_field_layout, stopsContainer, false);

        TextInputEditText stopEditText = stopFieldView.findViewById(R.id.stopEditText);
        ImageView closeIcon = stopFieldView.findViewById(R.id.stopEditText_close);

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
                    if (isProgrammaticChange) return;

                    String query = s.toString().trim();
                    currentSelectedField = stopEditText;
                    if (!query.isEmpty()) {
                        becomap.searchLocation(query);
                        locationsText.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // Optional
                }
            });
        }

        closeIcon.setOnClickListener(v -> {
            locationsRecyclerView.setVisibility(View.GONE);
            locationsText.setVisibility(View.GONE);
            currentSelectedField = null;
            isProgrammaticChange = true;
            Object tag = stopFieldView.getTag();
            if (tag != null && waypoints.contains(tag.toString())) {
                waypoints.remove(tag.toString());
            }
            stopsContainer.removeView(stopFieldView);
            isProgrammaticChange = false;
            checkAndShowSearchField();
        });

        // Optional: Add margin
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 8);
        stopFieldView.setLayoutParams(params);

        stopsContainer.addView(stopFieldView);
    }

    private void showsearchList(List<SearchResult> searchResult) {
        if (searchResult != null && !searchResult.isEmpty()) {
            SearchResult firstResult = searchResult.get(0);

            Log.e( "showsearchList: ", String.valueOf(searchResult.size()));
            searchRecyclerView.setVisibility(View.VISIBLE);

            Log.e( "locationsRecyclerView: search","visible" );
            searchResultAdapter.updateLocations(searchResult);
        } else {
            Log.e("showsearchList", "Empty or null search result");
            locationsRecyclerView.setVisibility(View.GONE);
            locationsText.setVisibility(View.GONE);
            Log.e( "locationsRecyclerView: search","gone" );
            Toast.makeText(requireContext(), "No locations available", Toast.LENGTH_SHORT).show();
        }
    }

    private void showforList(List<SearchResult> searchResult) {
        if (searchResult != null && !searchResult.isEmpty()) {
            SearchResult firstResult = searchResult.get(0);
            if (location_selected || inside==2) {
                locationsRecyclerView.setVisibility(View.VISIBLE);
                locationsText.setVisibility(View.VISIBLE);
                inside=0;
                Log.e("locationsRecyclerView: show ", String.valueOf(location_selected));
                locationAdapter.updateLocations(searchResult);
            }else {
               inside=inside+1;
            }
        } else {
            locationsRecyclerView.setVisibility(View.GONE);
            locationsText.setVisibility(View.GONE);
            Log.e( "locationsRecyclerView: show","gone" );
        }
    }

    private boolean isDuplicateEntry(String id) {
        if (id == null) return false;

        if (id.equals(startid) || id.equals(toid)) {
            return true;
        }

        if (waypoints != null && !waypoints.isEmpty()) {
            for (String waypoint : waypoints) {
                if (id.equals(waypoint)) {
                    return true;
                }
            }
        }

        return false;
    }
    public static SearchResult convertLocationToSearchResult(LocationModel location) {
        if (location == null) return null;

        SearchResult result = new SearchResult();
        result.setId(location.getId());
        result.setName(location.getName());
        result.setType(location.getType());
        result.setAmenity(location.getAmenity());
        result.setBanner(location.getBanner());
        result.setLogo(location.getLogo());
        result.setTopLocation(location.isTopLocation());
        result.setShowLogo(location.isShowLogo());
        result.setCategories(location.getCategories());

        // Optional: if Floor object mapping is needed and floorId is enough

        return result;
    }
    private void checkAndShowSearchField() {
        boolean fromGone = getView() != null && getView().findViewById(R.id.fromLayout) != null && getView().findViewById(R.id.fromLayout).getVisibility() != View.VISIBLE;
        boolean toGone = getView() != null && getView().findViewById(R.id.toLayout) != null && getView().findViewById(R.id.toLayout).getVisibility() != View.VISIBLE;
        if (fromGone && toGone )
        {
            add_stop_button.setVisibility(View.VISIBLE);
            Log.e( "addStopText: ","shown c" );
        }else {
            add_stop_button.setVisibility(View.GONE);
            Log.e( "addStopText: ","hide c" );
        }


        boolean stopsGone = stopsContainer.getChildCount() == 0;
        if (fromGone && toGone && stopsGone) {
            searchEditText_layout.setVisibility(View.VISIBLE);
            fromToLayout.setVisibility(View.GONE);
            add_stop_button.setVisibility(View.GONE);
            Log.e( "addStopText: ","shown c s" );
            locationsText.setVisibility(View.GONE);
        }
    }
}