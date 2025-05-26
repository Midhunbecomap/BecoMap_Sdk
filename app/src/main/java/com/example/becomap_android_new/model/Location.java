package com.example.becomap_android_new.model;

import java.util.List;

public class Location {
    private Floor floor;
    private String id;
    private String name;
    private String externalId;
    private String type;
    private String description;
    private String amenity;
    private Phone phone;
    private Social social;
    private List<Object> operationHours;
    private List<Object> links;
    private List<Category> categories;
    private String stateName;
    private List<String> tags;
    private String address;
    private String banner;
    private String logo;
    private List<Object> siblingGroups;
    private boolean topLocation;
    private boolean showLogo;
    private Object mapObject;
    private int sortOrder;

    public static class Floor {
        private String id;
        private String name;
        private String shortName;
        private String imageUrl;
        private int elevation;

        public String getId() { return id; }
        public String getName() { return name; }
        public String getShortName() { return shortName; }
        public String getImageUrl() { return imageUrl; }
        public int getElevation() { return elevation; }
    }

    public static class Phone {
        private String phone;
        public String getPhone() { return phone; }
    }

    public static class Social {
        // Empty class as social is an empty object in JSON
    }

    public static class Category {
        private String id;
        private String name;
        private Color color;
        private String externalId;
        private String iconName;

        public static class Color {
            private String rgba;
            private String hex;
            private double opacity;

            public String getRgba() { return rgba; }
            public String getHex() { return hex; }
            public double getOpacity() { return opacity; }
        }

        public String getId() { return id; }
        public String getName() { return name; }
        public Color getColor() { return color; }
        public String getExternalId() { return externalId; }
        public String getIconName() { return iconName; }
    }

    public Floor getFloor() { return floor; }
    public String getId() { return id; }
    public String getName() { return name; }
    public String getExternalId() { return externalId; }
    public String getType() { return type; }
    public String getDescription() { return description; }
    public String getAmenity() { return amenity; }
    public Phone getPhone() { return phone; }
    public Social getSocial() { return social; }
    public List<Object> getOperationHours() { return operationHours; }
    public List<Object> getLinks() { return links; }
    public List<Category> getCategories() { return categories; }
    public String getStateName() { return stateName; }
    public List<String> getTags() { return tags; }
    public String getAddress() { return address; }
    public String getBanner() { return banner; }
    public String getLogo() { return logo; }
    public List<Object> getSiblingGroups() { return siblingGroups; }
    public boolean isTopLocation() { return topLocation; }
    public boolean isShowLogo() { return showLogo; }
    public Object getMapObject() { return mapObject; }
    public int getSortOrder() { return sortOrder; }

    // Add setter methods
    public void setName(String name) { this.name = name; }
    public void setType(String type) { this.type = type; }

    public void setId(String id) {
        this.id = id;
    }
}
