package com.hotel.domain.valueobject;

/**
 * Value Object inmutable que representa el número de noches de una reserva.
 */
public record Nights(int value) {
    public Nights {
        if (value <= 0) {
            throw new IllegalArgumentException("Nights must be greater than zero");
        }
    }
}
