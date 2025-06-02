package com.becomap.sdk.UI;

import android.content.Context;
import android.view.ViewGroup;

import com.becomap.sdk.Config.BecoWebInterface;
import com.becomap.sdk.model.BCHappeningType;
import com.becomap.sdk.model.BCHappenings;
import com.becomap.sdk.model.BuildingsModels.BuildingModel;
import com.becomap.sdk.model.BuildingsModels.FloorModel;
import com.becomap.sdk.model.Category;
import com.becomap.sdk.model.Language.LanguageModel;
import com.becomap.sdk.model.LocationModel;
import com.becomap.sdk.model.Questions.BCQuestion;
import com.becomap.sdk.model.Route;
import com.becomap.sdk.model.SearchResult;
import com.becomap.sdk.model.ViewPort;

import java.util.List;

/**
 * Main public API class for Becomap SDK integration.
 */
public class Becomap {
    private final BecomapCore core;

    public Becomap(Context context) {
        this.core = new BecomapCore(context);
    }

    public void initializeMap(ViewGroup container, String clientId, String clientSecret, String siteIdentifier) {
        core.initializeMap(container, clientId, clientSecret, siteIdentifier);
    }

    public void searchLocation(String value) {
        core.searchLocation(value);
    }

    public void selectFloor(String floorId) {
        core.selectFloor(floorId);
    }

    public void selectLocationWithId(String locationid) {
        core.selectLocationWithId(locationid);
    }

    public void getFloors() {
        core.getFloors();
    }

    public void getroute(String Startid, String toid, List<String> waypoints) {
        core.getroute(Startid, toid, waypoints);
    }

    public void showroute() {
        core.showroute();
    }

    public void clearallroutes() {
        core.clearallroutes();
    }

    public void showStep(int stepIndex) {
        core.showStep(stepIndex);
    }

    public void getlocation() {
        core.getlocation();
    }

    public void injectGetSiteIdFunction() {
        core.injectGetSiteIdFunction();
    }

    public void injectGetSiteNameFunction() {
        core.injectGetSiteNameFunction();
    }

    public void GetDefaultFloor() {
        core.GetDefaultFloor();
    }

    public void GetLanguages() {
        core.GetLanguages();
    }

    public void GetCurrentFloor() {
        core.GetCurrentFloor();
    }

    public void GetCategories() {
        core.GetCategories();
    }

    public void GetAllAmenities() {
        core.GetAllAmenities();
    }

    public void GetSessionId() {
        core.GetSessionId();
    }

    public void GetQuestions() {
        core.GetQuestions();
    }

    public void GetAmenities() {
        core.GetAmenities();
    }

    public void GetHappenings(String type) {
        core.GetHappenings(type);
    }

    public void selectAmenities(String type) {
        core.selectAmenities(type);
    }

    public void focusTo(LocationModel locationModel, int zoom, int bearing, int pitch) {
        core.focusTo(locationModel, zoom, bearing, pitch);
    }

    public void clearSelection() {
        core.clearSelection();
    }

    public void updateZoom(int zoom) {
        core.updateZoom(zoom);
    }

    public void updatePitch(int pitch) {
        core.updatePitch(pitch);
    }

    public void updateBearing(int bearing) {
        core.updateBearing(bearing);
    }

    public void enableMultiSelection(boolean val) {
        core.enableMultiSelection(val);
    }

    public void setBounds(double[] sw, double[] ne) {
        core.setBounds(sw, ne);
    }

    public void setViewport(ViewPort viewPort) {
        core.setViewport(viewPort);
    }

    public void resetDefaultViewport(ViewPort viewPort) {
        core.resetDefaultViewport(viewPort);
    }

    public void setCallback(BecomapCallback callback) {
        core.setCallback(callback);
    }

    // Lifecycle methods
    public void onStart() {
        core.onStart();
    }

    public void onResume() {
        core.onResume();
    }

    public void onPause() {
        core.onPause();
    }

    public void onStop() {
        core.onStop();
    }

    public void onDestroy() {
        core.onDestroy();
    }

    public void onSaveInstanceState(android.os.Bundle outState) {
        core.onSaveInstanceState(outState);
    }

    public void onLowMemory() {
        core.onLowMemory();
    }

    public boolean canGoBack() {
        return core.canGoBack();
    }

    public void goBack() {
        core.goBack();
    }

    public interface BecomapCallback {
        void onMapRenderComplete();
        void onLocationsReceived(List<LocationModel> locations);
        void onSearchResultsReceived(List<SearchResult> searchResults);
        void onSiteIdAvailable(String siteId);
        void onSiteNameAvailable(String siteName);
        void onBuildingsReceived(List<BuildingModel> buildingModels);
        void onDefaultFloorReceived(FloorModel defaultFloor);
        void onLanguagesReceived(List<LanguageModel> languages);
        void onCurrentFloorReceived(FloorModel current);
        void onCategoriesReceived(List<Category> categories);
        void onAllAmenitiesReceived(List<LocationModel> amenities);
        void onAmenityTypesReceived(List<String> amenityTypes);
        void onSurveyQuestionsReceived(List<BCQuestion> questions);
        void onSessionIdReceived(String sessionId);
        void onFloors_Received(List<FloorModel> floors);
        void ongetroute(List<Route> routeList);
        void onGetHappening(List<BCHappenings> happenings);
    }
}