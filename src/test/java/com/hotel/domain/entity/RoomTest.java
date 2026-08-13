package com.hotel.domain.entity;

import com.hotel.domain.exception.RoomNotAvailableException;
import com.hotel.domain.valueobject.Price;
import com.hotel.domain.valueobject.RoomNumber;
import com.hotel.domain.valueobject.RoomType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    @Test
    @DisplayName("Should create room successfully using Value Objects")
    void shouldCreateRoomSuccessfullyWithVOs() {
        String id = "ROOM-1";
        RoomNumber roomNumber = new RoomNumber("101");
        RoomType type = RoomType.DOUBLE;
        Price price = new Price(80.0);

        Room room = new Room(id, roomNumber, type, price, true);

        assertEquals(id, room.getId());
        assertEquals(roomNumber, room.getRoomNumber());
        assertEquals(type, room.getType());
        assertEquals(price, room.getPricePerNight());
        assertTrue(room.isAvailable());
    }

    @Test
    @DisplayName("Should create room successfully using primitive compatibility constructor")
    void shouldCreateRoomSuccessfullyWithPrimitives() {
        String id = "ROOM-1";
        String roomNumber = "101";
        RoomType type = RoomType.SINGLE;
        double price = 50.0;

        Room room = new Room(id, roomNumber, type, price, true);

        assertEquals(id, room.getId());
        assertEquals("101", room.getRoomNumber().value());
        assertEquals(type, room.getType());
        assertEquals(50.0, room.getPricePerNight().value());
        assertTrue(room.isAvailable());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when constructor VOs or arguments are null/empty")
    void shouldThrowExceptionWhenArgumentsAreInvalid() {
        RoomNumber roomNumber = new RoomNumber("101");
        Price price = new Price(50.0);

        assertThrows(IllegalArgumentException.class, () -> new Room(null, roomNumber, RoomType.SINGLE, price, true));
        assertThrows(IllegalArgumentException.class, () -> new Room("", roomNumber, RoomType.SINGLE, price, true));
        assertThrows(IllegalArgumentException.class, () -> new Room("   ", roomNumber, RoomType.SINGLE, price, true));
        assertThrows(IllegalArgumentException.class, () -> new Room("R-1", null, RoomType.SINGLE, price, true));
        assertThrows(IllegalArgumentException.class, () -> new Room("R-1", roomNumber, null, price, true));
        assertThrows(IllegalArgumentException.class, () -> new Room("R-1", roomNumber, RoomType.SINGLE, null, true));
    }

    @Test
    @DisplayName("Should mark room as occupied successfully when available")
    void shouldMarkAsOccupiedSuccessfully() {
        Room room = new Room("R-1", "101", RoomType.SINGLE, 50.0, true);
        room.markAsOccupied();
        assertFalse(room.isAvailable());
    }

    @Test
    @DisplayName("Should throw RoomNotAvailableException when marking an already occupied room")
    void shouldThrowExceptionWhenAlreadyOccupied() {
        Room room = new Room("R-1", "101", RoomType.SINGLE, 50.0, false);
        assertThrows(RoomNotAvailableException.class, room::markAsOccupied);
    }

    @Test
    @DisplayName("Should mark room as available successfully")
    void shouldMarkAsAvailableSuccessfully() {
        Room room = new Room("R-1", "101", RoomType.SINGLE, 50.0, false);
        room.markAsAvailable();
        assertTrue(room.isAvailable());
    }
}
