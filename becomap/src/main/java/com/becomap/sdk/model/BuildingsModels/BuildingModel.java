package com.becomap.sdk.model.BuildingsModels;

import java.util.List;

public class BuildingModel {
    public String buildingId;
    public String buildingName;
    public List<FloorModel> floors;

    public BuildingModel(String buildingId, String buildingName, List<FloorModel> floors) {
        this.buildingId = buildingId;
        this.buildingName = buildingName;
        this.floors = floors;
    }
}