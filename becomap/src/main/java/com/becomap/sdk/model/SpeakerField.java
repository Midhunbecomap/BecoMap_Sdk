package com.becomap.sdk.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class SpeakerField {
    private String data; // JSON array string of speakers
    private int order;

    public List<Speaker> getSpeakers() {
        try {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Speaker>>(){}.getType();
            return gson.fromJson(data, listType);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    public class Speaker {
        private String name;
        private String role;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

    }
}
