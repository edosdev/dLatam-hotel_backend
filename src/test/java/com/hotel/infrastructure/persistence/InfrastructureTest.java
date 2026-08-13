package com.hotel.infrastructure.persistence;

import com.hotel.domain.entity.Reservation;
import com.hotel.domain.entity.Room;
import com.hotel.domain.valueobject.RoomNumber;
import com.hotel.domain.valueobject.RoomType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InfrastructureTest {

    @Test
    @DisplayName("Should test InMemoryRoomRepository save and query operations")
    void testInMemoryRoomRepository() {
        InMemoryRoomRepository repo = new InMemoryRoomRepository();
        RoomNumber num = new RoomNumber("101");

        // Query missing
        Optional<Room> missing = repo.findByRoomNumber(num);
        assertTrue(missing.isEmpty());

        // Save and query existing
        Room room = new Room("R-1", "101", RoomType.SINGLE, 50.0, true);
        repo.save(room);

        Optional<Room> found = repo.findByRoomNumber(num);
        assertTrue(found.isPresent());
        assertEquals("R-1", found.get().getId());

        // Save null, should not crash
        repo.save(null);

        // Find by null RoomNumber
        Optional<Room> foundNull = repo.findByRoomNumber(null);
        assertTrue(foundNull.isEmpty());
    }

    @Test
    @DisplayName("Should test InMemoryReservationRepository save and query operations")
    void testInMemoryReservationRepository() {
        InMemoryReservationRepository repo = new InMemoryReservationRepository();

        // Query missing
        Optional<Reservation> missing = repo.findById("RES-1");
        assertTrue(missing.isEmpty());

        // Save and query existing
        Reservation reservation = new Reservation("RES-1", "Maria", "101", 3, 50.0);
        repo.save(reservation);

        Optional<Reservation> found = repo.findById("RES-1");
        assertTrue(found.isPresent());
        assertEquals("Maria", found.get().getGuestName().value());

        // Save null, should not crash
        repo.save(null);

        // Find by null ID
        Optional<Reservation> foundNull = repo.findById(null);
        assertTrue(foundNull.isEmpty());
    }

    @Test
    @DisplayName("Should execute ConsoleEmailNotificationAdapter without throwing exceptions")
    void testConsoleEmailNotificationAdapter() {
        ConsoleEmailNotificationAdapter adapter = new ConsoleEmailNotificationAdapter();
        
        assertDoesNotThrow(() -> adapter.sendReservationConfirmation("Maria", "RES-1", 150.0));
        assertDoesNotThrow(() -> adapter.sendCancellationNotice("Maria", "RES-1"));
    }
}
