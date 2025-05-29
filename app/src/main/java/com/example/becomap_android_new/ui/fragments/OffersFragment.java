package com.example.becomap_android_new.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.becomap.sdk.UI.Becomap;
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

import java.util.ArrayList;
import java.util.List;

public class OffersFragment extends Fragment {
    private Becomap becomap;
    FrameLayout mapContainer;
    ImageView progressBar;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_offers, container, false);
        initializeViews(root);
        return root;
    }
    private void initializeViews(View root) {
        mapContainer = root.findViewById(R.id.map_container_store);
        progressBar=root.findViewById(R.id.progressbar);
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
        becomap.initializeMap(mapContainer, "c079dfa3a77dad13351cfacd95841c2c2780fe08",
                "f62a59675b2a47ddb75f1f994d88e653",
                "67dcf5dd2f21c64e3225254f");

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
                getActivity().runOnUiThread(() -> {
                    List<LocationModel> tenantStores = new ArrayList<>();
                    for (LocationModel location : locations) {
                        if ("AMENITIES".equalsIgnoreCase(location.getType())) {
                            tenantStores.add(location);
                        }
                    }
                    Log.e("onLocationsReceived: ", "setting");
                    GridView storeGridView = getView().findViewById(R.id.storeGridView);
                    StoreAdapter adapter = new StoreAdapter(getContext(), tenantStores, new StoreAdapter.OnStoreClickListener() {
                        @Override
                        public void onStoreClick(LocationModel location) {
                            openMapFragmentWithLocation(location); // <-- your custom method
                        }
                    });
                    storeGridView.setAdapter(adapter);
                    progressBar.setVisibility(View.GONE);
                });
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
        });
    }
    private void openMapFragmentWithLocation(LocationModel locationa) {
        // your selected location
        Bundle bundle = new Bundle();
        bundle.putSerializable("location", locationa); // or use Parcelable if needed

        NavController navController = NavHostFragment.findNavController(OffersFragment.this);
        navController.navigate(R.id.navigation_map, bundle);
    }
}
