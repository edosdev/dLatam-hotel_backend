package com.hotel.domain.valueobject;

/**
 * Value Object inmutable que representa el precio por noche de una habitación.
 */
public record Price(double value) {
    public Price {
        if (value <= 0) {
            throw new IllegalArgumentException("Price per night must be greater than zero");
        }
    }
}
