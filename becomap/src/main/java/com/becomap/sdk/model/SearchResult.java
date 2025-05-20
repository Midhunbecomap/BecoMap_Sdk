package com.becomap.sdk.model;

import java.util.List;

public class SearchResult {
    public String id;
    public String externalId;
    public String type;
    public String amenity;
    public Floor floor;
    public List<Category> categories;
    public String logo;
    public boolean topLocation;
    public boolean showLogo;
    public int sortOrder;
}

