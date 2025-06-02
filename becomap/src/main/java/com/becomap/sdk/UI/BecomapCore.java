package com.becomap.sdk.UI;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;
import android.webkit.WebChromeClient;
import android.webkit.JavascriptInterface;
import android.webkit.ConsoleMessage;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.becomap.sdk.Config.BecoWebInterface;
import com.becomap.sdk.ViewModel.BecomapViewModel;
import com.becomap.sdk.model.BCHappeningType;
import com.becomap.sdk.model.BCHappenings;
import com.becomap.sdk.model.BuildingsModels.BuildingModel;
import com.becomap.sdk.model.BuildingsModels.FloorModel;
import com.becomap.sdk.model.Category;
import com.becomap.sdk.model.Color;
import com.becomap.sdk.model.Language.LanguageModel;
import com.becomap.sdk.model.LocationModel;
import com.becomap.sdk.model.Questions.BCQuestion;
import com.becomap.sdk.model.Route;
import com.becomap.sdk.model.SearchResult;
import com.becomap.sdk.model.SpeakerField;
import com.becomap.sdk.model.ViewPort;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Internal implementation class for Becomap SDK.
 */
class BecomapCore {
    private static final String TAG = "BecomapCore";

    // UI Components
    private final Context context;
    private WebView webView;
    private ViewGroup rootContainer;

    // Data Components
    private BecomapViewModel viewModel;
    private BecoWebInterface jsConfig;

    // Callback
    private Becomap.BecomapCallback callback;

    public BecomapCore(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        this.context = context;
        initializeViewModel();
    }

    private void initializeViewModel() {
        if (context instanceof ViewModelStoreOwner) {
            viewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(BecomapViewModel.class);
        } else {
            throw new IllegalStateException("Context must implement ViewModelStoreOwner");
        }
    }

    public void initializeMap(ViewGroup container, String clientId, String clientSecret, String siteIdentifier) {
        this.rootContainer = container;
        setupCredentials(clientId, clientSecret, siteIdentifier);
        setupWebView(container);
        loadLocalHtml();
    }

    public void searchLocation(String value) {
        Log.d(TAG, "Searching for location: " + value);
        jsConfig.execute_search_all_location(webView, value);
    }

    public void selectFloor(String floorId) {
        jsConfig.selectFloor(webView, floorId);
    }

    public void selectLocationWithId(String locationid) {
        Log.e("selectLocationWithId: ", locationid);
        jsConfig.selectLocationWithId(webView, locationid);
    }

    public void getFloors() {
        Log.d(TAG, "Getting floors");
        jsConfig.GetBuildinsFunction(webView);
    }

    public void getroute(String Startid, String toid, List<String> waypoints) {
        Log.d(TAG, "Getting floors");
        jsConfig.getRouteById(webView, Startid, toid, waypoints);
    }

    public void showroute() {
        Log.d(TAG, "show route");
        jsConfig.showRoute(webView);
    }

    public void clearallroutes() {
        Log.d(TAG, "show route");
        jsConfig.clearallroutes(webView);
    }

    public void showStep(int stepIndex) {
        Log.d(TAG, "show route");
        jsConfig.showStepByOrderIndex(webView, stepIndex);
    }

    public void getlocation() {
        Log.d(TAG, "executegetalllocation");
        jsConfig.executegetalllocation(webView);
    }

    public void injectGetSiteIdFunction() {
        Log.d(TAG, "injectGetSiteIdFunction");
        jsConfig.injectGetSiteIdFunction(webView);
    }

    public void injectGetSiteNameFunction() {
        Log.d(TAG, "injectGetSiteNameFunction");
        jsConfig.injectGetSiteNameFunction(webView);
    }

    public void GetDefaultFloor() {
        Log.d(TAG, "GetDefaultFloor");
        jsConfig.GetDefaultFloor(webView);
    }

    public void GetLanguages() {
        Log.d(TAG, "GetLanguages");
        jsConfig.GetLanguages(webView);
    }

    public void GetCurrentFloor() {
        Log.d(TAG, "GetCurrentFloor");
        jsConfig.GetCurrentFloor(webView);
    }

    public void GetCategories() {
        Log.d(TAG, "GetCategories");
        jsConfig.GetCategories(webView);
    }

    public void GetAllAmenities() {
        Log.d(TAG, "GetAllAmenities");
        jsConfig.GetAllAmenities(webView);
    }

    public void GetSessionId() {
        Log.d(TAG, "GetSessionId");
        jsConfig.GetSessionId(webView);
    }

    public void GetQuestions() {
        Log.d(TAG, "GetQuestions");
        jsConfig.GetQuestions(webView);
    }

    public void GetAmenities() {
        Log.d(TAG, "GetAmenities");
        jsConfig.GetAmenities(webView);
    }

    public void GetHappenings(String type) {
        Log.d(TAG, "GetHappenings");
        jsConfig.GetHappenings(webView, type);
    }

    public void selectAmenities(String type) {
        Log.d(TAG, "selectAmenities");
        jsConfig.selectAmenities(webView, type);
    }

    public void focusTo(LocationModel locationModel, int zoom, int bearing, int pitch) {
        Log.d(TAG, "focusTo");
        jsConfig.focusTo(webView, locationModel, zoom, bearing, pitch);
    }

    public void clearSelection() {
        Log.d(TAG, "clearSelection");
        jsConfig.clearSelection(webView);
    }

    public void updateZoom(int zoom) {
        Log.d(TAG, "updateZoom");
        jsConfig.updateZoom(webView, zoom);
    }

    public void updatePitch(int pitch) {
        Log.d(TAG, "updatePitch");
        jsConfig.updatePitch(webView, pitch);
    }

    public void updateBearing(int bearing) {
        Log.d(TAG, "updateBearing");
        jsConfig.updateBearing(webView, bearing);
    }

    public void enableMultiSelection(boolean val) {
        Log.d(TAG, "enableMultiSelection");
        jsConfig.enableMultiSelection(webView, val);
    }

    public void setBounds(double[] sw, double[] ne) {
        Log.d(TAG, "setBounds");
        jsConfig.setBounds(webView, sw, ne);
    }

    public void setViewport(ViewPort viewPort) {
        Log.d(TAG, "setViewport");
        jsConfig.setViewport(webView, viewPort);
    }

    public void resetDefaultViewport(ViewPort viewPort) {
        Log.d(TAG, "resetDefaultViewport");
        jsConfig.resetDefaultViewport(webView, viewPort);
    }

    public void setCallback(Becomap.BecomapCallback callback) {
        this.callback = callback;
    }

    private void setupWebView(ViewGroup container) {
        webView = new WebView(context);
        container.addView(webView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        configureWebSettings();
        setupWebViewClients();
        addJavascriptInterface();
    }

    private void configureWebSettings() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDatabaseEnabled(true);
        webSettings.setGeolocationEnabled(false);
        webSettings.setSaveFormData(false);
        webSettings.setSavePassword(false);
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
    }

    private void setupWebViewClients() {
        webView.setWebViewClient(new BecomapWebViewClient());
        webView.setWebChromeClient(new BecomapWebChromeClient());
    }

    private void addJavascriptInterface() {
        webView.addJavascriptInterface(new WebAppInterface(context), "jsHandler");
    }

    private void loadLocalHtml() {
        if (webView != null) {
            webView.loadUrl("file:///android_asset/esm-build/index.html");
        }
    }

    private void setupCredentials(String clientId, String clientSecret, String siteIdentifier) {
        jsConfig = new BecoWebInterface(clientId, clientSecret, siteIdentifier);
        viewModel.setCredentials(clientId, clientSecret, siteIdentifier);
    }

    private class BecomapWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (jsConfig != null) {
                jsConfig.executeInit(webView);
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Log.e(TAG, "WebView error: " + description);
        }
    }

    private class BecomapWebChromeClient extends WebChromeClient {
        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            Log.d(TAG, "Console: " + consoleMessage.message());
            return true;
        }
    }

    private class WebAppInterface {
        private final Context mContext;

        WebAppInterface(Context context) {
            this.mContext = context;
        }

        @JavascriptInterface
        public void postMessage(String messageJson) {
            Log.d(TAG, "Received from JS: " + messageJson);
            handleJsMessage(messageJson);
        }

        private void handleJsMessage(String messageJson) {
            try {
                JSONObject message = new JSONObject(messageJson);
                String messageType = message.getString("type");
                Log.e("handleJsMessage: ", messageType);
                switch (messageType) {
                    case "onRenderComplete":
                        handleRenderComplete();
                        break;
                    case "getLocations":
                        handleLocations(message.getJSONArray("payload"));
                        break;
                    case "searchForLocations":
                        handleSearchResults(message.getJSONObject("payload"));
                        break;
                    case "initError":
                        handleInitError(message.getJSONObject("payload"));
                        break;
                    case "getSiteId":
                        handleSiteId(message.getString("payload"));
                        break;
                    case "getSiteName":
                        handleSiteName(message.getString("payload"));
                        break;
                    case "getBuildings":
                        handleBuildings(message.getJSONArray("payload"));
                        break;
                    case "getDefaultFloor":
                        handleDefaultFloor(message.getJSONObject("payload"));
                        break;
                    case "GetLanguages":
                        handleLanguages(message.getJSONArray("payload"));
                        break;
                    case "getCurrentFloor":
                        handleCurrentFloor(message.getJSONObject("payload"));
                        break;
                    case "getCategories":
                        handleCategories(message.getJSONArray("payload"));
                        break;
                    case "getAllAmenities":
                        handleAllAmenities(message.getJSONArray("payload"));
                        break;
                    case "getAmenities":
                        handleAmenityTypes(message.getJSONArray("payload"));
                        break;
                    case "getQuestions":
                        handleSurveyQuestions(message.getJSONArray("payload"));
                        break;
                    case "getSessionId":
                        handleSessionId(message.getString("payload"));
                        break;
                    case "getRoute":
                        handleGetRoute(message.getJSONArray("payload").toString());
                        break;
                    case "getHappenings":
                        handleHappenings(message.getJSONArray("payload"));
                        break;
                    default:
                        Log.w(TAG, "Unhandled message type: " + messageType);
                        break;
                }
            } catch (JSONException e) {
                Log.e(TAG, "Invalid JSON received: " + messageJson, e);
            }
        }

        private void handleRenderComplete() {
            runOnUiThread(() -> {
                Log.d(TAG, "Map Render Complete");
                if (callback != null) {
                    callback.onMapRenderComplete();
                }
            });
        }

        private void handleLocations(JSONArray locationsArray) throws JSONException {
            Log.d(TAG, "location array: " + locationsArray.get(0));
            List<LocationModel> locations = parseLocations(locationsArray);
            if (callback != null) {
                callback.onLocationsReceived(locations);
            }
        }

        private void handleSearchResults(JSONObject payload) throws JSONException {
            List<SearchResult> searchResults = parseSearchResults(payload.getJSONArray("results"));
            Log.e("handleSearchResults: ", String.valueOf(searchResults.get(0)));
            if (callback != null) {
                callback.onSearchResultsReceived(searchResults);
            }
        }

        private void handleInitError(JSONObject error) {
            Log.e(TAG, "Init error: " + error.optString("message"));
        }

        private void handleSiteId(String siteId) {
            Log.d(TAG, "Site ID: " + siteId);
            if (callback != null) {
                callback.onSiteIdAvailable(siteId);
            }
        }

        private void handleSiteName(String siteName) {
            Log.d(TAG, "Site name: " + siteName);
            if (callback != null) {
                callback.onSiteNameAvailable(siteName);
            }
        }

        private void handleBuildings(JSONArray buildingsArray) throws JSONException {
            List<BuildingModel> buildings = parseBuildings(buildingsArray);
            if (callback != null) {
                callback.onBuildingsReceived(buildings);
            }
        }

        private void handleDefaultFloor(JSONObject floorJson) throws JSONException {
            FloorModel floor = parseFloor(floorJson);
            if (callback != null) {
                callback.onDefaultFloorReceived(floor);
            }
        }

        private void handleLanguages(JSONArray languagesArray) throws JSONException {
            List<LanguageModel> languages = parseLanguages(languagesArray);
            if (callback != null) {
                callback.onLanguagesReceived(languages);
            }
        }

        private void handleCurrentFloor(JSONObject floorJson) throws JSONException {
            FloorModel floor = parseFloor(floorJson);
            if (callback != null) {
                callback.onCurrentFloorReceived(floor);
            }
        }

        private void handleCategories(JSONArray categoriesArray) throws JSONException {
            List<Category> categories = parseCategories(categoriesArray);
            if (callback != null) {
                callback.onCategoriesReceived(categories);
            }
        }

        private void handleAllAmenities(JSONArray amenitiesArray) throws JSONException {
            List<LocationModel> amenities = parseLocations(amenitiesArray);
            if (callback != null) {
                callback.onAllAmenitiesReceived(amenities);
            }
        }

        private void handleAmenityTypes(JSONArray amenityTypesArray) throws JSONException {
            List<String> amenityTypes = new ArrayList<>();
            for (int i = 0; i < amenityTypesArray.length(); i++) {
                amenityTypes.add(amenityTypesArray.getString(i));
            }
            if (callback != null) {
                callback.onAmenityTypesReceived(amenityTypes);
            }
        }

        private void handleSurveyQuestions(JSONArray questionsArray) throws JSONException {
            List<BCQuestion> questions = parseQuestions(questionsArray);
            if (callback != null) {
                callback.onSurveyQuestionsReceived(questions);
            }
        }

        private void handleSessionId(String sessionId) {
            Log.d(TAG, "Session ID: " + sessionId);
            if (callback != null) {
                callback.onSessionIdReceived(sessionId);
            }
        }

        private void handleGetRoute(String getroutejson) {
            Log.e("handleGetRoute: ", getroutejson);
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Route>>() {}.getType();
            List<Route> routeList = gson.fromJson(getroutejson, listType);
            if (callback != null) {
                callback.ongetroute(routeList);
            }
        }

        private void handleHappenings(JSONArray payload) {
            try {
                List<BCHappenings> happenings = new ArrayList<>();
                Gson gson = new Gson();

                for (int i = 0; i < payload.length(); i++) {
                    JSONObject obj = payload.getJSONObject(i);
                    BCHappenings happening = gson.fromJson(obj.toString(), BCHappenings.class);
                    happenings.add(happening);
                }

                for (BCHappenings h : happenings) {
                    Log.d(TAG, "Event: " + h.getName());
                    if (h.getCustomFields() != null && h.getCustomFields().getSpeaker() != null) {
                        for (SpeakerField.Speaker speaker : h.getCustomFields().getSpeaker().getSpeakers()) {
                            Log.d(TAG, "Speaker: " + speaker.getName() + ", Role: " + speaker.getRole());
                        }
                    }
                }

                if (callback != null) {
                    callback.onGetHappening(happenings);
                }

            } catch (JSONException e) {
                Log.e(TAG, "Error parsing happenings payload", e);
            }
        }

        // Parsing Methods (same as before)
        private List<LocationModel> parseLocations(JSONArray locationsArray) throws JSONException {
            List<LocationModel> locations = new ArrayList<>();
            for (int i = 0; i < locationsArray.length(); i++) {
                locations.add(parseLocation(locationsArray.getJSONObject(i)));
            }
            return locations;
        }

        private LocationModel parseLocation(JSONObject locJson) throws JSONException {
            return new LocationModel(
                    locJson.optString("id"),
                    locJson.optString("name"),
                    locJson.optString("description"),
                    locJson.optString("address"),
                    locJson.optString("stateName"),
                    locJson.optString("type"),
                    locJson.optString("amenity"),
                    locJson.optString("banner"),
                    locJson.optString("logo"),
                    locJson.optString("floorId"),
                    locJson.optBoolean("topLocation", false),
                    locJson.optBoolean("showLogo", false),
                    parseCategories(locJson.optJSONArray("categories"))
            );
        }

        private List<SearchResult> parseSearchResults(JSONArray resultsArray) throws JSONException {
            Gson gson = new Gson();
            List<SearchResult> results = new ArrayList<>();
            for (int i = 0; i < resultsArray.length(); i++) {
                results.add(gson.fromJson(resultsArray.getJSONObject(i).toString(), SearchResult.class));
            }
            return results;
        }

        private List<BuildingModel> parseBuildings(JSONArray buildingsArray) throws JSONException {
            List<BuildingModel> buildings = new ArrayList<>();
            for (int i = 0; i < buildingsArray.length(); i++) {
                buildings.add(parseBuilding(buildingsArray.getJSONObject(i)));
            }
            return buildings;
        }

        private BuildingModel parseBuilding(JSONObject buildingObj) throws JSONException {
            return new BuildingModel(
                    buildingObj.optString("buildingId"),
                    buildingObj.optString("buildingName"),
                    parseFloors(buildingObj.optJSONArray("floors"))
            );
        }

        private List<FloorModel> parseFloors(JSONArray floorsArray) throws JSONException {
            List<FloorModel> floors = new ArrayList<>();
            if (floorsArray != null) {
                for (int i = 0; i < floorsArray.length(); i++) {
                    floors.add(parseFloor(floorsArray.getJSONObject(i)));
                }
                if (callback != null) {
                    callback.onFloors_Received(floors);
                }
            }
            return floors;
        }

        private FloorModel parseFloor(JSONObject floorObj) throws JSONException {
            return new FloorModel(
                    floorObj.optString("id"),
                    floorObj.optString("name"),
                    floorObj.optString("shortName"),
                    floorObj.optString("imageUrl", null),
                    floorObj.optDouble("elevation", 0.0)
            );
        }

        private List<LanguageModel> parseLanguages(JSONArray languagesArray) throws JSONException {
            List<LanguageModel> languages = new ArrayList<>();
            for (int i = 0; i < languagesArray.length(); i++) {
                JSONObject langObj = languagesArray.getJSONObject(i);
                languages.add(new LanguageModel(
                        langObj.optString("name"),
                        langObj.optString("code")
                ));
            }
            return languages;
        }

        private List<Category> parseCategories(JSONArray categoriesArray) throws JSONException {
            List<Category> categories = new ArrayList<>();
            if (categoriesArray != null) {
                for (int i = 0; i < categoriesArray.length(); i++) {
                    categories.add(parseCategory(categoriesArray.getJSONObject(i)));
                }
            }
            return categories;
        }

        private Category parseCategory(JSONObject categoryJson) throws JSONException {
            JSONObject colorJson = categoryJson.optJSONObject("color");
            return new Category(
                    categoryJson.optString("id"),
                    parseColor(colorJson),
                    categoryJson.optString("externalId"),
                    categoryJson.optString("iconName"),
                    categoryJson.optString("name"),
                    categoryJson.optString("icon")
            );
        }

        private Color parseColor(JSONObject colorJson) {
            return new Color(
                    colorJson != null ? colorJson.optString("rgba") : "",
                    colorJson != null ? colorJson.optString("hex") : "",
                    colorJson != null ? colorJson.optDouble("opacity", 1.0) : 1.0
            );
        }

        private List<BCQuestion> parseQuestions(JSONArray questionsArray) throws JSONException {
            List<BCQuestion> questions = new ArrayList<>();
            for (int i = 0; i < questionsArray.length(); i++) {
                questions.add(parseQuestion(questionsArray.getJSONObject(i)));
            }
            return questions;
        }

        private BCQuestion parseQuestion(JSONObject qObj) throws JSONException {
            BCQuestion question = new BCQuestion();
            question.setId(qObj.getString("id"));
            question.setQuestion(qObj.getString("question"));
            question.setType(BCQuestion.BCQuestionType.valueOf(qObj.getString("type")));

            if (qObj.has("options")) {
                question.setOptions(parseOptions(qObj.getJSONArray("options")));
            }
            return question;
        }

        private List<String> parseOptions(JSONArray optionsArray) throws JSONException {
            List<String> options = new ArrayList<>();
            for (int i = 0; i < optionsArray.length(); i++) {
                options.add(optionsArray.getString(i));
            }
            return options;
        }

        private void runOnUiThread(Runnable action) {
            if (mContext instanceof Activity) {
                ((Activity) mContext).runOnUiThread(action);
            }
        }
    }

    // Lifecycle methods
    public void onStart() {
        if (webView != null) webView.onResume();
    }

    public void onResume() {
        if (webView != null) webView.onResume();
    }

    public void onPause() {
        if (webView != null) webView.onPause();
    }

    public void onStop() {
        if (webView != null) webView.onPause();
    }

    public void onDestroy() {
        if (webView != null) {
            cleanupWebView();
        }
    }

    private void cleanupWebView() {
        webView.stopLoading();
        webView.clearHistory();
        webView.clearCache(true);
        webView.loadUrl("about:blank");
        webView.onPause();
        webView.removeAllViews();
        webView.destroy();
        webView.setWebChromeClient(null);
        webView.setWebViewClient(null);
        webView.removeJavascriptInterface("jsHandler");
        webView = null;
    }

    public void onSaveInstanceState(Bundle outState) {
        if (webView != null) {
            webView.saveState(outState);
        }
    }

    public void onLowMemory() {
        if (webView != null) {
            webView.clearCache(true);
        }
    }

    public boolean canGoBack() {
        return webView != null && webView.canGoBack();
    }

    public void goBack() {
        if (webView != null) {
            webView.goBack();
        }
    }
}