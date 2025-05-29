package com.becomap.sdk.model;

import java.util.List;
import java.util.Map;

/**
 * Represents a happening (event, news, or offer) within the application.
 */
public class BCHappenings {

    /**
     * Unique identifier for the happening.
     */
    private String id;

    /**
     * The name or title of the happening.
     */
    private String name;

    /**
     * A description providing more details about the happening.
     */
    private String description;

    /**
     * The start date and time of the happening in ISO 8601 format.
     */
    private String startDate;

    /**
     * The end date and time of the happening in ISO 8601 format.
     */
    private String endDate;

    /**
     * The date and time when the happening should be displayed or highlighted in ISO 8601 format.
     */
    private String showDate;

    /**
     * An external identifier associated with the happening, if available.
     */
    private String externalId;

    /**
     * The unique identifier of the site associated with the happening.
     */
    private String siteId;

    /**
     * The unique identifier of the location associated with the happening.
     */
    private String locationId;

    /**
     * A list of image URLs associated with the happening.
     */
    private List<String> images;

    /**
     * The type of the happening, such as OFFER, NEWS, or EVENT.
     */
    private BCHappeningType type;

    /**
     * A priority value used to determine the display order of happenings.
     */
    private int priority;

    /**
     * Additional custom fields that provide extra information about the happening.
     * These fields are dynamically structured and can vary based on the type of happening.
     */
    private Map<String, Object> customFields;

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getShowDate() {
        return showDate;
    }

    public void setShowDate(String showDate) {
        this.showDate = showDate;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public BCHappeningType getType() {
        return type;
    }

    public void setType(BCHappeningType type) {
        this.type = type;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Map<String, Object> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(Map<String, Object> customFields) {
        this.customFields = customFields;
    }
}

