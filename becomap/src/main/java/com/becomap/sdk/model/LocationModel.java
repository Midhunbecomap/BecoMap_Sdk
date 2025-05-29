package com.becomap.sdk.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LocationModel implements Serializable {
    private String id;
    private String name;
    private String description;
    private String address;
    private String stateName;
    private String type;
    private String amenity;
    private String banner;
    private String logo;
    private String floorId;
    private boolean topLocation;
    private boolean showLogo;
    private List<Category> categories;

    public LocationModel(String id, String name, String description, String address, String stateName, String type,
                         String amenity, String banner, String logo, String floorId, boolean topLocation, boolean showLogo,
                         List<Category> categories) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.stateName = stateName;
        this.type = type;
        this.amenity = amenity;
        this.banner = banner;
        this.logo = logo;
        this.floorId = floorId;
        this.topLocation = topLocation;
        this.showLogo = showLogo;
        this.categories = categories;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getAmenity() {
        return amenity;
    }

    public void setAmenity(String amenity) {
        this.amenity = amenity;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public boolean isTopLocation() {
        return topLocation;
    }

    public void setTopLocation(boolean topLocation) {
        this.topLocation = topLocation;
    }

    public boolean isShowLogo() {
        return showLogo;
    }

    public void setShowLogo(boolean showLogo) {
        this.showLogo = showLogo;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
