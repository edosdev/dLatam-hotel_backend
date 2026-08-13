package com.hotel.domain.entity;

import com.hotel.domain.exception.InvalidReservationException;
import com.hotel.domain.valueobject.GuestName;
import com.hotel.domain.valueobject.Nights;
import com.hotel.domain.valueobject.Price;
import com.hotel.domain.valueobject.ReservationStatus;
import com.hotel.domain.valueobject.RoomNumber;

/**
 * Entidad de dominio que representa una reserva de hotel con identidad única y encapsulamiento.
 */
public class Reservation {

    private final String id;
    private final GuestName guestName;
    private final RoomNumber roomNumber;
    private final Nights nights;
    private final Price pricePerNight;
    private ReservationStatus status;

    // Constructor principal utilizando Value Objects
    public Reservation(String id, GuestName guestName, RoomNumber roomNumber, Nights nights, Price pricePerNight) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Reservation ID cannot be empty");
        }
        if (guestName == null) {
            throw new IllegalArgumentException("Guest name cannot be null");
        }
        if (roomNumber == null) {
            throw new IllegalArgumentException("Room number cannot be null");
        }
        if (nights == null) {
            throw new IllegalArgumentException("Nights cannot be null");
        }
        if (pricePerNight == null) {
            throw new IllegalArgumentException("Price per night cannot be null");
        }
        this.id = id;
        this.guestName = guestName;
        this.roomNumber = roomNumber;
        this.nights = nights;
        this.pricePerNight = pricePerNight;
        this.status = ReservationStatus.CONFIRMED;
    }

    // Constructor de compatibilidad que recibe tipos primitivos
    public Reservation(String id, String guestName, String roomNumber, int nights, double pricePerNight) {
        this(id, new GuestName(guestName), new RoomNumber(roomNumber), new Nights(nights), new Price(pricePerNight));
    }

    // Comportamiento rico de negocio: calcular el costo total
    public double calculateTotal() {
        return nights.value() * pricePerNight.value();
    }

    // Comportamiento rico de negocio: cancelar la reserva
    public void cancel() {
        if (this.status == ReservationStatus.CANCELLED) {
            throw new InvalidReservationException("Reservation " + id + " is already cancelled");
        }
        this.status = ReservationStatus.CANCELLED;
    }

    // Getters
    public String getId() {
        return id;
    }

    public GuestName getGuestName() {
        return guestName;
    }

    public RoomNumber getRoomNumber() {
        return roomNumber;
    }

    public Nights getNights() {
        return nights;
    }

    public Price getPricePerNight() {
        return pricePerNight;
    }

    public ReservationStatus getStatus() {
        return status;
    }
}
