package com.becomap.sdk.model;

import java.util.ArrayList;
import java.util.List;

public class LocationModel {
    public String id;
    public String type;
    public String amenity;
    public List<String> categories = new ArrayList<>();
    public double elevation;

    public LocationModel(String id, String type, String amenity, List<String> categories, double elevation) {
        this.id = id;
        this.type = type;
        this.amenity = amenity;
        this.categories = categories;
        this.elevation = elevation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmenity() {
        return amenity;
    }

    public void setAmenity(String amenity) {
        this.amenity = amenity;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public double getElevation() {
        return elevation;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }
}
