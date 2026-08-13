package com.hotel.domain.entity;

import com.hotel.domain.exception.InvalidReservationException;
import com.hotel.domain.valueobject.GuestName;
import com.hotel.domain.valueobject.Nights;
import com.hotel.domain.valueobject.Price;
import com.hotel.domain.valueobject.ReservationStatus;
import com.hotel.domain.valueobject.RoomNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReservationTest {

    @Test
    @DisplayName("Should create reservation successfully using Value Objects")
    void shouldCreateReservationSuccessfullyWithVOs() {
        String id = "RES-100";
        GuestName guestName = new GuestName("Juan Perez");
        RoomNumber roomNumber = new RoomNumber("202");
        Nights nights = new Nights(3);
        Price price = new Price(100.0);

        Reservation reservation = new Reservation(id, guestName, roomNumber, nights, price);

        assertEquals(id, reservation.getId());
        assertEquals(guestName, reservation.getGuestName());
        assertEquals(roomNumber, reservation.getRoomNumber());
        assertEquals(nights, reservation.getNights());
        assertEquals(price, reservation.getPricePerNight());
        assertEquals(ReservationStatus.CONFIRMED, reservation.getStatus());
        assertEquals(300.0, reservation.calculateTotal());
    }

    @Test
    @DisplayName("Should create reservation successfully using compatibility constructor")
    void shouldCreateReservationSuccessfullyWithPrimitives() {
        String id = "RES-100";
        String guestName = "Juan Perez";
        String roomNumber = "202";
        int nights = 3;
        double price = 100.0;

        Reservation reservation = new Reservation(id, guestName, roomNumber, nights, price);

        assertEquals(id, reservation.getId());
        assertEquals("Juan Perez", reservation.getGuestName().value());
        assertEquals("202", reservation.getRoomNumber().value());
        assertEquals(3, reservation.getNights().value());
        assertEquals(100.0, reservation.getPricePerNight().value());
        assertEquals(ReservationStatus.CONFIRMED, reservation.getStatus());
        assertEquals(300.0, reservation.calculateTotal());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when constructor VOs or arguments are null/empty")
    void shouldThrowExceptionWhenArgumentsAreInvalid() {
        GuestName guestName = new GuestName("Juan");
        RoomNumber roomNumber = new RoomNumber("101");
        Nights nights = new Nights(2);
        Price price = new Price(50.0);

        assertThrows(IllegalArgumentException.class, () -> new Reservation(null, guestName, roomNumber, nights, price));
        assertThrows(IllegalArgumentException.class, () -> new Reservation("", guestName, roomNumber, nights, price));
        assertThrows(IllegalArgumentException.class, () -> new Reservation("  ", guestName, roomNumber, nights, price));
        assertThrows(IllegalArgumentException.class, () -> new Reservation("RES-1", null, roomNumber, nights, price));
        assertThrows(IllegalArgumentException.class, () -> new Reservation("RES-1", guestName, null, nights, price));
        assertThrows(IllegalArgumentException.class, () -> new Reservation("RES-1", guestName, roomNumber, null, price));
        assertThrows(IllegalArgumentException.class, () -> new Reservation("RES-1", guestName, roomNumber, nights, null));
    }

    @Test
    @DisplayName("Should cancel reservation successfully")
    void shouldCancelReservationSuccessfully() {
        Reservation reservation = new Reservation("RES-1", "Juan", "101", 2, 50.0);
        reservation.cancel();
        assertEquals(ReservationStatus.CANCELLED, reservation.getStatus());
    }

    @Test
    @DisplayName("Should throw InvalidReservationException when cancelling an already cancelled reservation")
    void shouldThrowExceptionWhenAlreadyCancelled() {
        Reservation reservation = new Reservation("RES-1", "Juan", "101", 2, 50.0);
        reservation.cancel();
        assertThrows(InvalidReservationException.class, reservation::cancel);
    }
}
