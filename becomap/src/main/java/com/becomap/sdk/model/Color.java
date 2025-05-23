package com.becomap.sdk.model;

public class Color {
    public String rgba;
    public String hex;
    public double opacity;

    public Color(String rgba, String hex, double opacity) {
        this.rgba = rgba;
        this.hex = hex;
        this.opacity = opacity;
    }

    public String getRgba() {
        return rgba;
    }

    public void setRgba(String rgba) {
        this.rgba = rgba;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public double getOpacity() {
        return opacity;
    }

    public void setOpacity(int opacity) {
        this.opacity = opacity;
    }
}

