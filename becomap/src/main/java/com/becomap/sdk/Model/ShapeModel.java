package com.becomap.sdk.Model;

public class ShapeModel {
    private String shapeId;
    private String type;
    private ShapeJsonData jsonData;
    private String floorId;
    private String siteId;
    private String layerId;

    public ShapeModel(String shapeId, String type, ShapeJsonData jsonData,
                      String floorId, String siteId, String layerId) {
        this.shapeId = shapeId;
        this.type = type;
        this.jsonData = jsonData;
        this.floorId = floorId;
        this.siteId = siteId;
        this.layerId = layerId;
    }

    public String getShapeId() {
        return shapeId;
    }

    public void setShapeId(String shapeId) {
        this.shapeId = shapeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ShapeJsonData getJsonData() {
        return jsonData;
    }

    public void setJsonData(ShapeJsonData jsonData) {
        this.jsonData = jsonData;
    }

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getLayerId() {
        return layerId;
    }

    public void setLayerId(String layerId) {
        this.layerId = layerId;
    }
}
