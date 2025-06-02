package com.example.becomap_android_new.ui.fragments;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.becomap.sdk.UI.Becomap;
import com.becomap.sdk.model.BCHappenings;
import com.becomap.sdk.model.BuildingsModels.BuildingModel;
import com.becomap.sdk.model.BuildingsModels.FloorModel;
import com.becomap.sdk.model.Category;
import com.becomap.sdk.model.Language.LanguageModel;
import com.becomap.sdk.model.LocationModel;
import com.becomap.sdk.model.Questions.BCQuestion;
import com.becomap.sdk.model.Route;
import com.becomap.sdk.model.SearchResult;
import com.bumptech.glide.Glide;
import com.example.becomap_android_new.R;
import com.example.becomap_android_new.adapter.StoreAdapter;
import com.example.becomap_android_new.model.Store;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class StoreFragment extends Fragment {
    private Becomap becomap;
    FrameLayout mapContainer;
    ImageView progressBar;
    CardView nodata;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_store, container, false);


        initializeViews(root);

        return root;
    }
    private void initializeViews(View root) {
        mapContainer = root.findViewById(R.id.map_container_store);
        progressBar=root.findViewById(R.id.progressbar);
        nodata=root.findViewById(R.id.nodata);
        Glide.with(this)
                .asGif()
                .load(R.drawable.becomaploader) // your loading.gif in res/raw
                .into(progressBar);
        initializeBecomap();
    }
    private void initializeBecomap() {
        if (getContext() == null) return;

        becomap = new Becomap(requireContext());

        // Initialize map with WebView
        becomap.initializeMap(mapContainer, "7a2f9d3c85b14eef6670c20458e607d912314b76",
                "3f9c27d4b68ea52a7c1d5e034f8b6a1",
                "67b481f2b253dc2bccb426f2");

        setupBecomapCallback();
    }
    private void setupBecomapCallback() {
        becomap.setCallback(new Becomap.BecomapCallback() {
            @Override
            public void onMapRenderComplete() {
                Log.e( "onMapRenderComplete: ","complete" );
                becomap.getlocation();
            }

            @Override
            public void onLocationsReceived(List<LocationModel> locations) {
                if (locations.equals(null)||locations.isEmpty()){
                    progressBar.setVisibility(View.GONE);
                    nodata.setVisibility(View.VISIBLE);
                }else {
                    getActivity().runOnUiThread(() -> {
                        List<LocationModel> tenantStores = new ArrayList<>();
                        for (LocationModel location : locations) {
                            List<Category> categories = location.getCategories();
                            if (categories != null) {
                                for (Category category : categories) {
                                    String categoryName = category.getName();
                                    if (categoryName != null && !categoryName.trim().isEmpty()) {
                                        if (categoryName.equals("Retail") || categoryName.equals("Shops")) {
                                            tenantStores.add(location);
                                        }
                                        Log.e("onLocationsReceived", categoryName);
                                    } else {
                                        Log.e("onLocationsReceived", "Category name is null or empty");
                                    }
                                }
                            }

                        }
                        Log.e("onLocationsReceived: ", "setting");
                        RecyclerView storeGridView = getView().findViewById(R.id.storeGridView);
                        storeGridView.setLayoutManager(new LinearLayoutManager(getContext())); // 2 columns
                        StoreAdapter adapter = new StoreAdapter(getContext(), tenantStores, new StoreAdapter.OnStoreClickListener() {
                            @Override
                            public void onStoreClick(LocationModel location) {
                                openMapFragmentWithLocation(location.getId());
                            }
                        });
                        storeGridView.setAdapter(adapter);
                        progressBar.setVisibility(View.GONE);
                    });
                }
            }

            @Override
            public void onSearchResultsReceived(List<SearchResult> searchResults) {

            }

            @Override
            public void onSiteIdAvailable(String siteId) {

            }

            @Override
            public void onSiteNameAvailable(String siteName) {

            }

            @Override
            public void onBuildingsReceived(List<BuildingModel> buildingModels) {

            }

            @Override
            public void onDefaultFloorReceived(FloorModel defaultFloor) {

            }

            @Override
            public void onLanguagesReceived(List<LanguageModel> languages) {

            }

            @Override
            public void onCurrentFloorReceived(FloorModel current) {

            }

            @Override
            public void onCategoriesReceived(List<Category> categories) {

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

            @Override
            public void onFloors_Received(List<FloorModel> floors) {


            }

            @Override
            public void ongetroute(List<Route> routeList) {

            }

            @Override
            public void onGetHappening(List<BCHappenings> happenings) {

            }
        });
    }
    private void openMapFragmentWithLocation(String locationa) {
        Bundle bundle = new Bundle();
        bundle.putString("locationName", locationa);  // use putString for Strings

        NavController navController = NavHostFragment.findNavController(StoreFragment.this);
        navController.navigate(R.id.navigation_map, bundle);
    }
}