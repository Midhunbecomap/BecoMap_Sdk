package com.becomap.sdk.Model;

import java.util.List;

public class Viewport {
    private double zoom;
    private double pitch;
    private Double bearing; // nullable
    private List<Double> bounds; // [minLng, minLat, maxLng, maxLat]
    private List<Double> center; // [lat, lng]

    public double getZoom() {
        return zoom;
    }

    public double getPitch() {
        return pitch;
    }

    public Double getBearing() {
        return bearing;
    }

    public List<Double> getBounds() {
        return bounds;
    }

    public List<Double> getCenter() {
        return center;
    }
}
