package com.example.becomap_android_new.model;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class Store {
    private String id;
    private String name;
    private String type;
    private String description;
    private Map<String, String> operatingHours;
    private Location location;
    private Contact contact;
    private List<String> features;
    private double rating;
    private int totalReviews;
    private String imageName;

    public static class Location {
        private String address;
        private String city;
        private String state;
        private String zipCode;
        private double latitude;
        private double longitude;

        public Location() {
            this.address = "";
            this.city = "";
            this.state = "";
            this.zipCode = "";
        }

        public String getAddress() { return address != null ? address : ""; }
        public String getCity() { return city != null ? city : ""; }
        public String getState() { return state != null ? state : ""; }
        public String getZipCode() { return zipCode != null ? zipCode : ""; }
        public double getLatitude() { return latitude; }
        public double getLongitude() { return longitude; }
    }

    public static class Contact {
        private String phone;
        private String email;

        public Contact() {
            this.phone = "";
            this.email = "";
        }

        public String getPhone() { return phone != null ? phone : ""; }
        public String getEmail() { return email != null ? email : ""; }
    }

    public Store() {
        this.id = "";
        this.name = "";
        this.type = "";
        this.description = "";
        this.location = new Location();
        this.contact = new Contact();
        this.rating = 0.0;
        this.totalReviews = 0;
        this.imageName = "";
    }

    public String getId() { return id != null ? id : ""; }
    public String getName() { return name != null ? name : ""; }
    public String getType() { return type != null ? type : ""; }
    public String getDescription() { return description != null ? description : ""; }
    public Map<String, String> getOperatingHours() { return operatingHours; }
    public Location getLocation() { return location != null ? location : new Location(); }
    public Contact getContact() { return contact != null ? contact : new Contact(); }
    public List<String> getFeatures() { return features != null ? features : new ArrayList<>(); }
    public double getRating() { return rating; }
    public int getTotalReviews() { return totalReviews; }
    public String getImageName() { return imageName != null ? imageName : ""; }
}
