package com.becomap.sdk.Model;

import java.util.List;

public class ShapeJsonData {
    private String type;
    private Properties properties;
    private Geometry geometry;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public static class Properties {
        private String type;
        private String floorId;
        private String siteId;
        private String id;
        private String shapeId;
        private String layerId;
        private ShapeProperties shapeProperties;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getShapeId() {
            return shapeId;
        }

        public void setShapeId(String shapeId) {
            this.shapeId = shapeId;
        }

        public String getLayerId() {
            return layerId;
        }

        public void setLayerId(String layerId) {
            this.layerId = layerId;
        }

        public ShapeProperties getShapeProperties() {
            return shapeProperties;
        }

        public void setShapeProperties(ShapeProperties shapeProperties) {
            this.shapeProperties = shapeProperties;
        }
    }

    public static class ShapeProperties {
        private String color;
        private double height;
        private String hoverColor;
        private Label label;
        private double opacity;

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public double getHeight() {
            return height;
        }

        public void setHeight(double height) {
            this.height = height;
        }

        public String getHoverColor() {
            return hoverColor;
        }

        public void setHoverColor(String hoverColor) {
            this.hoverColor = hoverColor;
        }

        public Label getLabel() {
            return label;
        }

        public void setLabel(Label label) {
            this.label = label;
        }

        public double getOpacity() {
            return opacity;
        }

        public void setOpacity(double opacity) {
            this.opacity = opacity;
        }
    }

    public static class Label {
        private String align;
        private String fontFamily;
        private int fontSize;
        private String labelColor;

        public String getAlign() {
            return align;
        }

        public void setAlign(String align) {
            this.align = align;
        }

        public String getFontFamily() {
            return fontFamily;
        }

        public void setFontFamily(String fontFamily) {
            this.fontFamily = fontFamily;
        }

        public int getFontSize() {
            return fontSize;
        }

        public void setFontSize(int fontSize) {
            this.fontSize = fontSize;
        }

        public String getLabelColor() {
            return labelColor;
        }

        public void setLabelColor(String labelColor) {
            this.labelColor = labelColor;
        }
    }

    public static class Geometry {
        private String type;
        private Object coordinates;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Object getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(Object coordinates) {
            this.coordinates = coordinates;
        }
    }
}
