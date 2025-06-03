package com.example.becomap_android_new.model;

public class Waypoint {
    private final String id;
    private final String name;

    public Waypoint(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // Optional: override equals/hashCode to help remove by value
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Waypoint)) return false;
        Waypoint other = (Waypoint) obj;
        return id.equals(other.id) && name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return id.hashCode() * 31 + name.hashCode();
    }
}
