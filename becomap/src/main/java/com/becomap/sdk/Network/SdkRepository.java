package com.becomap.sdk.Network;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.becomap.sdk.Model.BuildingResponse;
import com.becomap.sdk.Model.FloorData;
import com.becomap.sdk.Model.SdkTokenRequest;
import com.becomap.sdk.Model.SdkTokenResponse;
import com.becomap.sdk.Model.SiteResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SdkRepository {
    private final ApiService apiService = RetrofitClient.getApiService();

    public LiveData<SdkTokenResponse> generateToken(String clientId, String clientSecret) {
        MutableLiveData<SdkTokenResponse> liveData = new MutableLiveData<>();
        apiService.generateSdkToken(new SdkTokenRequest(clientId, clientSecret))
                .enqueue(new Callback<SdkTokenResponse>() {
                    @Override
                    public void onResponse(Call<SdkTokenResponse> call, Response<SdkTokenResponse> response) {
                        liveData.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<SdkTokenResponse> call, Throwable t) {
                        liveData.setValue(null);
                    }
                });
        return liveData;
    }

    public LiveData<SiteResponse> getSiteData(String accessToken, String siteId) {
        MutableLiveData<SiteResponse> liveData = new MutableLiveData<>();

        apiService.getSiteDetails(siteId, "Bearer " + accessToken)
                .enqueue(new Callback<SiteResponse>() {
                    @Override
                    public void onResponse(Call<SiteResponse> call, Response<SiteResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            liveData.setValue(response.body());
                        } else {
                            liveData.setValue(null);  // Error or no data
                        }
                    }

                    @Override
                    public void onFailure(Call<SiteResponse> call, Throwable t) {
                        liveData.setValue(null);  // Network or other error
                    }
                });

        return liveData;
    }
    public LiveData<List<FloorData>> getBuildingData(String accessToken, String siteId, String buildingId) {
        MutableLiveData<List<FloorData>> liveData = new MutableLiveData<>();

        apiService.getBuildingDetails(siteId, buildingId, "Bearer " + accessToken)
                .enqueue(new Callback<List<FloorData>>() {
                    @Override
                    public void onResponse(Call<List<FloorData>> call, Response<List<FloorData>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            liveData.setValue(response.body());
                        } else {
                            liveData.setValue(null);
                            Log.e("fetchBuildingData", "Empty or error response");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<FloorData>> call, Throwable t) {
                        liveData.setValue(null);
                        Log.e("fetchBuildingData: Failure", t.getMessage());
                    }
                });

        return liveData;
    }
}
