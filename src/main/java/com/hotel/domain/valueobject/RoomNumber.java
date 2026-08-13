package com.hotel.domain.valueobject;

/**
 * Value Object inmutable que representa el número de una habitación de hotel.
 */
public record RoomNumber(String value) {
    public RoomNumber {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Room number cannot be empty");
        }
    }
}
