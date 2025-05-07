    package com.becomap.sdk.Model;

    import java.util.List;

    public class SiteResponse {
        private String siteId;
        private String mapId;
        private String siteName;
        private int orgId;
        private List<Language> languages;
        private Language defaultLanguage;
        private List<OperationHours> operationHours;
        private double latitude;
        private double longitude;
        private String address;
        private String telephone;
        private List<String> topLocations;
        private String tzId;
        private String utcOffset;
        private ImageUrl imageUrl;
        private BinaryUrl binaryUrl;
        private String link;
        private String type;
        private List<Building> buildings;
        private MultilingualJsonPaths multilingualJsonPaths;

        // Getters and Setters

        public String getSiteId() {
            return siteId;
        }

        public void setSiteId(String siteId) {
            this.siteId = siteId;
        }

        public String getMapId() {
            return mapId;
        }

        public void setMapId(String mapId) {
            this.mapId = mapId;
        }

        public String getSiteName() {
            return siteName;
        }

        public void setSiteName(String siteName) {
            this.siteName = siteName;
        }

        public int getOrgId() {
            return orgId;
        }

        public void setOrgId(int orgId) {
            this.orgId = orgId;
        }

        public List<Language> getLanguages() {
            return languages;
        }

        public void setLanguages(List<Language> languages) {
            this.languages = languages;
        }

        public Language getDefaultLanguage() {
            return defaultLanguage;
        }

        public void setDefaultLanguage(Language defaultLanguage) {
            this.defaultLanguage = defaultLanguage;
        }

        public List<OperationHours> getOperationHours() {
            return operationHours;
        }

        public void setOperationHours(List<OperationHours> operationHours) {
            this.operationHours = operationHours;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public List<String> getTopLocations() {
            return topLocations;
        }

        public void setTopLocations(List<String> topLocations) {
            this.topLocations = topLocations;
        }

        public String getTzId() {
            return tzId;
        }

        public void setTzId(String tzId) {
            this.tzId = tzId;
        }

        public String getUtcOffset() {
            return utcOffset;
        }

        public void setUtcOffset(String utcOffset) {
            this.utcOffset = utcOffset;
        }

        public ImageUrl getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(ImageUrl imageUrl) {
            this.imageUrl = imageUrl;
        }

        public BinaryUrl getBinaryUrl() {
            return binaryUrl;
        }

        public void setBinaryUrl(BinaryUrl binaryUrl) {
            this.binaryUrl = binaryUrl;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<Building> getBuildings() {
            return buildings;
        }

        public void setBuildings(List<Building> buildings) {
            this.buildings = buildings;
        }

        public MultilingualJsonPaths getMultilingualJsonPaths() {
            return multilingualJsonPaths;
        }

        public void setMultilingualJsonPaths(MultilingualJsonPaths multilingualJsonPaths) {
            this.multilingualJsonPaths = multilingualJsonPaths;
        }

        // Inner classes
        public static class Language {
            private String name;
            private String code;
            private boolean enabled;

            // Getters and Setters
            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }
        }

        public static class OperationHours {
            private String opens;
            private String closes;
            private List<String> dayOfWeek;

            // Getters and Setters
            public String getOpens() {
                return opens;
            }

            public void setOpens(String opens) {
                this.opens = opens;
            }

            public String getCloses() {
                return closes;
            }

            public void setCloses(String closes) {
                this.closes = closes;
            }

            public List<String> getDayOfWeek() {
                return dayOfWeek;
            }

            public void setDayOfWeek(List<String> dayOfWeek) {
                this.dayOfWeek = dayOfWeek;
            }
        }

        public static class ImageUrl {
            private String small;
            private String original;
            private String large;
            private String medium;

            // Getters and Setters
            public String getSmall() {
                return small;
            }

            public void setSmall(String small) {
                this.small = small;
            }

            public String getOriginal() {
                return original;
            }

            public void setOriginal(String original) {
                this.original = original;
            }

            public String getLarge() {
                return large;
            }

            public void setLarge(String large) {
                this.large = large;
            }

            public String getMedium() {
                return medium;
            }

            public void setMedium(String medium) {
                this.medium = medium;
            }
        }

        public static class BinaryUrl {
            private String route;
            private String shape;
            private String mapObject;
            private String location;
            private String category;

            // Getters and Setters
            public String getRoute() {
                return route;
            }

            public void setRoute(String route) {
                this.route = route;
            }

            public String getShape() {
                return shape;
            }

            public void setShape(String shape) {
                this.shape = shape;
            }

            public String getMapObject() {
                return mapObject;
            }

            public void setMapObject(String mapObject) {
                this.mapObject = mapObject;
            }

            public String getLocation() {
                return location;
            }

            public void setLocation(String location) {
                this.location = location;
            }

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }
        }

        public static class Building {
            private String buildingId;
            private String buildingName;

            // Getters and Setters
            public String getBuildingId() {
                return buildingId;
            }

            public void setBuildingId(String buildingId) {
                this.buildingId = buildingId;
            }

            public String getBuildingName() {
                return buildingName;
            }

            public void setBuildingName(String buildingName) {
                this.buildingName = buildingName;
            }
        }

        public static class MultilingualJsonPaths {
            private String hi;
            private String de;

            // Getters and Setters
            public String getHi() {
                return hi;
            }

            public void setHi(String hi) {
                this.hi = hi;
            }

            public String getDe() {
                return de;
            }

            public void setDe(String de) {
                this.de = de;
            }
        }
    }
