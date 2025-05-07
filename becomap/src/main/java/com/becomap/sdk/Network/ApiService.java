package com.becomap.sdk.Network;

import com.becomap.sdk.Model.BuildingResponse;
import com.becomap.sdk.Model.SdkTokenRequest;
import com.becomap.sdk.Model.SdkTokenResponse;
import com.becomap.sdk.Model.SiteResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @Headers({
            "accept: */*",
            "Content-Type: application/json"
    })
    @POST("api/v1/auth/sdk/generate-token")
    Call<SdkTokenResponse> generateSdkToken(@Body SdkTokenRequest request);

    @GET("api/v1/sdk/site/{siteId}")
    Call<SiteResponse> getSiteDetails(@Path("siteId") String siteId, @Header("Authorization") String token);

    @GET("api/v1/sdk/floor/{siteId}/{buildingId}")
    Call<BuildingResponse> getBuildingDetails(@Path("siteId") String siteId,
                                              @Path("buildingId") String buildingId,
                                              @Header("Authorization") String token);
}
