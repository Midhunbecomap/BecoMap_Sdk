package com.becomap.sdk.Model;

import com.google.gson.annotations.SerializedName;

public class GeoReference {

    @SerializedName("control")
    private Coordinate control;

    @SerializedName("target")
    private Coordinate target;

    public Coordinate getControl() {
        return control;
    }

    public void setControl(Coordinate control) {
        this.control = control;
    }

    public Coordinate getTarget() {
        return target;
    }

    public void setTarget(Coordinate target) {
        this.target = target;
    }
}
