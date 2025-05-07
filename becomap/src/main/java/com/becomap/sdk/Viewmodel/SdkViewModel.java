package com.becomap.sdk.Viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.becomap.sdk.Model.BuildingResponse;
import com.becomap.sdk.Model.FloorData;
import com.becomap.sdk.Model.SiteResponse;
import com.becomap.sdk.Network.SdkRepository;
import com.becomap.sdk.Model.SdkTokenResponse;

import java.util.List;

public class SdkViewModel extends ViewModel {
    private final SdkRepository sdkRepository;
    private final MutableLiveData<SdkTokenResponse> sdkTokenResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<SiteResponse> siteResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<FloorData>> buildingResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public SdkViewModel() {
        sdkRepository = new SdkRepository();
    }

    public LiveData<SdkTokenResponse> getSdkTokenResponse() {
        return sdkTokenResponseLiveData;
    }

    public LiveData<SiteResponse> getSiteResponse() {
        return siteResponseLiveData;
    }

    public LiveData<List<FloorData>> getBuildingResponse() {
        return buildingResponseLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    // Method to generate SDK token
    public void generateSdkToken(String clientId, String clientSecret) {
        sdkRepository.generateToken(clientId, clientSecret)
                .observeForever(response -> {
                    if (response != null) {
                        sdkTokenResponseLiveData.setValue(response);
                        if (response.getAccessToken() != null) {
                            fetchSiteData(response.getAccessToken(), "67613c2a487f0072644c4db8");
                        } else {
                            errorLiveData.setValue("Token generation failed.");
                        }
                    } else {
                        sdkTokenResponseLiveData.setValue(null);
                        errorLiveData.setValue("Token generation failed.");
                    }
                });
    }

    // Fetch site data
    private void fetchSiteData(String accessToken, String siteId) {
        sdkRepository.getSiteData(accessToken, siteId)
                .observeForever(siteResponse -> {
                    if (siteResponse != null) {
                        siteResponseLiveData.setValue(siteResponse);
                        // After fetching site data, fetch building data
                        fetchBuildingData(accessToken, siteId, siteResponse.getBuildings().get(0).getBuildingId());
                    } else {
                        siteResponseLiveData.setValue(null);
                        errorLiveData.setValue("Failed to fetch site data.");
                    }
                });
    }

    // Fetch building data
    private void fetchBuildingData(String accessToken, String siteId, String buildingId) {
        Log.e("fetchBuildingData: token", accessToken);
        Log.e("fetchBuildingData: siteId", siteId);
        Log.e("fetchBuildingData: buildingId", buildingId);

        sdkRepository.getBuildingData(accessToken, siteId, buildingId)
                .observeForever(floorDataList -> {
                    if (floorDataList != null) {
                        buildingResponseLiveData.setValue(floorDataList);  // Update with List<FloorData>

                        // Log each floor's shortName
                        for (FloorData floor : floorDataList) {
                            Log.d("FloorData", "shortName: " + floor.getShortName());
                        }
                    } else {
                        Log.e("fetchBuildingData", "Error: null response");
                        buildingResponseLiveData.setValue(null);
                        errorLiveData.setValue("Failed to fetch building data.");
                    }
                });
    }
}
