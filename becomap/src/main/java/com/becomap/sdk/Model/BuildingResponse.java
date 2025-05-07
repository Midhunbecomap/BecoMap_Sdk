package com.becomap.sdk.Model;

public class BuildingResponse {
    private String buildingId;
    private String buildingName;
    private String floorDetails;

    // Add other fields based on your API response

    // Getters and Setters
    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getFloorDetails() {
        return floorDetails;
    }

    public void setFloorDetails(String floorDetails) {
        this.floorDetails = floorDetails;
    }
}
