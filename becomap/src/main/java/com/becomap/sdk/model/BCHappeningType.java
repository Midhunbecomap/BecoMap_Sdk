package com.becomap.sdk.model;

public enum BCHappeningType {
    OFFER("OFFER"),
    NEWS("NEWS"),
    EVENT("EVENT");

    private final String value;

    BCHappeningType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * Parses a string value to its corresponding BCHappeningType enum.
     *
     * @param value the string representation of the enum
     * @return the corresponding BCHappeningType enum, or null if not found
     */
    public static BCHappeningType fromValue(String value) {
        for (BCHappeningType type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        return null;
    }
}
