package com.becomap.sdk.model.BuildingsModels;

public class FloorModel {
    public String id;
    public String name;
    public String shortName;
    public String imageUrl;
    public double elevation;

    public FloorModel(String id, String name, String shortName, String imageUrl, double elevation) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
        this.imageUrl = imageUrl;
        this.elevation = elevation;
    }
    public String getId() { return id; }
    public String getName() { return name; }
    public String getShortName() { return shortName; }
    public String getImageUrl() { return imageUrl; }
    public double getElevation() { return elevation; }
}