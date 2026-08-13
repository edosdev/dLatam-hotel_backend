package com.hotel.domain.valueobject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValueObjectsTest {

    @Test
    @DisplayName("Should create RoomNumber successfully when value is valid")
    void shouldCreateRoomNumberSuccessfully() {
        RoomNumber roomNumber = new RoomNumber("101");
        assertEquals("101", roomNumber.value());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when RoomNumber value is null or blank")
    void shouldThrowExceptionWhenRoomNumberIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> new RoomNumber(null));
        assertThrows(IllegalArgumentException.class, () -> new RoomNumber(""));
        assertThrows(IllegalArgumentException.class, () -> new RoomNumber("   "));
    }

    @Test
    @DisplayName("Should create Price successfully when value is valid")
    void shouldCreatePriceSuccessfully() {
        Price price = new Price(80.0);
        assertEquals(80.0, price.value());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when Price value is zero or negative")
    void shouldThrowExceptionWhenPriceIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> new Price(0.0));
        assertThrows(IllegalArgumentException.class, () -> new Price(-10.0));
    }

    @Test
    @DisplayName("Should create Nights successfully when value is valid")
    void shouldCreateNightsSuccessfully() {
        Nights nights = new Nights(5);
        assertEquals(5, nights.value());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when Nights value is zero or negative")
    void shouldThrowExceptionWhenNightsIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> new Nights(0));
        assertThrows(IllegalArgumentException.class, () -> new Nights(-1));
    }

    @Test
    @DisplayName("Should create GuestName successfully when value is valid")
    void shouldCreateGuestNameSuccessfully() {
        GuestName guestName = new GuestName("Juan Perez");
        assertEquals("Juan Perez", guestName.value());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when GuestName value is null or blank")
    void shouldThrowExceptionWhenGuestNameIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> new GuestName(null));
        assertThrows(IllegalArgumentException.class, () -> new GuestName(""));
        assertThrows(IllegalArgumentException.class, () -> new GuestName("   "));
    }

    @Test
    @DisplayName("Should verify RoomType and ReservationStatus enums")
    void shouldVerifyEnums() {
        assertNotNull(RoomType.valueOf("SINGLE"));
        assertNotNull(RoomType.valueOf("DOUBLE"));
        assertNotNull(RoomType.valueOf("SUITE"));
        assertEquals(3, RoomType.values().length);

        assertNotNull(ReservationStatus.valueOf("CONFIRMED"));
        assertNotNull(ReservationStatus.valueOf("CANCELLED"));
        assertEquals(2, ReservationStatus.values().length);
    }
}
