package com.becomap.sdk.model;

import java.util.List;

public class Route {
    private int orderIndex;
    private double distance;
    private List<Step> steps;

    // Getters and Setters
    public int getOrderIndex() { return orderIndex; }
    public void setOrderIndex(int orderIndex) { this.orderIndex = orderIndex; }

    public double getDistance() { return distance; }
    public void setDistance(double distance) { this.distance = distance; }

    public List<Step> getSteps() { return steps; }
    public void setSteps(List<Step> steps) { this.steps = steps; }
}
