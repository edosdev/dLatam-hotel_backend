package com.hotel.domain.entity;

import com.hotel.domain.exception.RoomNotAvailableException;
import com.hotel.domain.valueobject.Price;
import com.hotel.domain.valueobject.RoomNumber;
import com.hotel.domain.valueobject.RoomType;

/**
 * Entidad de dominio que representa una habitación de hotel con identidad única y encapsulamiento.
 */
public class Room {

    private final String id;
    private final RoomNumber roomNumber;
    private final RoomType type;
    private final Price pricePerNight;
    private boolean available;

    // Constructor principal utilizando Value Objects
    public Room(String id, RoomNumber roomNumber, RoomType type, Price pricePerNight, boolean available) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Room ID cannot be empty");
        }
        if (roomNumber == null) {
            throw new IllegalArgumentException("Room number cannot be null");
        }
        if (type == null) {
            throw new IllegalArgumentException("Room type cannot be null");
        }
        if (pricePerNight == null) {
            throw new IllegalArgumentException("Price per night cannot be null");
        }
        this.id = id;
        this.roomNumber = roomNumber;
        this.type = type;
        this.pricePerNight = pricePerNight;
        this.available = available;
    }

    // Constructor de compatibilidad que recibe tipos primitivos
    public Room(String id, String roomNumber, RoomType type, double pricePerNight, boolean available) {
        this(id, new RoomNumber(roomNumber), type, new Price(pricePerNight), available);
    }

    // Comportamiento rico de negocio: ocupar habitación
    public void markAsOccupied() {
        if (!this.available) {
            throw new RoomNotAvailableException("Room " + roomNumber.value() + " is already occupied or not available");
        }
        this.available = false;
    }

    // Comportamiento rico de negocio: liberar habitación
    public void markAsAvailable() {
        this.available = true;
    }

    // Getters
    public String getId() {
        return id;
    }

    public RoomNumber getRoomNumber() {
        return roomNumber;
    }

    public RoomType getType() {
        return type;
    }

    public Price getPricePerNight() {
        return pricePerNight;
    }

    public boolean isAvailable() {
        return available;
    }
}
