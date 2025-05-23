package com.becomap.sdk.model;

public class Category {
    public String id;
    public Color color;
    public String externalId;
    public String iconName;
    private String name;
    private String icon;

    public Category(String id, Color color, String externalId, String iconName, String name, String icon) {
        this.id = id;
        this.color = color;
        this.externalId = externalId;
        this.iconName = iconName;
        this.name = name;
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
