package com.becomap.sdk.model;

public class Step {
    private int orderIndex;
    private String action;
    private String direction;
    private String reference;
    private double distance;
    private String floorId;
    private String referenceLocationId;

    // Getters and Setters
    public int getOrderIndex() { return orderIndex; }
    public void setOrderIndex(int orderIndex) { this.orderIndex = orderIndex; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getDirection() { return direction; }
    public void setDirection(String direction) { this.direction = direction; }

    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }

    public double getDistance() { return distance; }
    public void setDistance(double distance) { this.distance = distance; }

    public String getFloorId() { return floorId; }
    public void setFloorId(String floorId) { this.floorId = floorId; }

    public String getReferenceLocationId() { return referenceLocationId; }
    public void setReferenceLocationId(String referenceLocationId) { this.referenceLocationId = referenceLocationId; }
}

