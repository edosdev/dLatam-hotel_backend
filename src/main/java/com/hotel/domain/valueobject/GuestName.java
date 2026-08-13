package com.hotel.domain.valueobject;

/**
 * Value Object inmutable que representa el nombre de un huésped.
 */
public record GuestName(String value) {
    public GuestName {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Guest name cannot be empty");
        }
    }
}
