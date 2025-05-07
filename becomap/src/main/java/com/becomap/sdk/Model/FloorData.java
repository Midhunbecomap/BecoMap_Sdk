package com.becomap.sdk.Model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class FloorData {

    @SerializedName("floorId")
    private String floorId;

    @SerializedName("name")
    private String name;

    @SerializedName("shortName")
    private String shortName;

    @SerializedName("elevation")
    private int elevation;

    @SerializedName("width")
    private int width;

    @SerializedName("height")
    private int height;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("scene")
    private String scene;

    @SerializedName("geoReferences")
    private List<GeoReference> geoReferences;

    @SerializedName("outdoorView")
    private boolean outdoorView;

    @SerializedName("buildingId")
    private String buildingId;

    @SerializedName("siteId")
    private String siteId;

    @SerializedName("viewport")
    private Viewport viewport;

    // Getters and setters...

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public int getElevation() {
        return elevation;
    }

    public void setElevation(int elevation) {
        this.elevation = elevation;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public List<GeoReference> getGeoReferences() {
        return geoReferences;
    }

    public void setGeoReferences(List<GeoReference> geoReferences) {
        this.geoReferences = geoReferences;
    }

    public boolean isOutdoorView() {
        return outdoorView;
    }

    public void setOutdoorView(boolean outdoorView) {
        this.outdoorView = outdoorView;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public Viewport getViewport() {
        return viewport;
    }

    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }
}
