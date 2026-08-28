package com.hotel.infrastructure.persistence.repository;

import com.hotel.infrastructure.persistence.entity.RoomEntity;
import com.hotel.domain.valueobject.RoomType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests de integración para RoomJpaRepository.
 * Verifica operaciones CRUD con base de datos H2 en memoria.
 */
@DataJpaTest
@ActiveProfiles("test")
class RoomJpaRepositoryTest {

    @Autowired
    private RoomJpaRepository roomJpaRepository;

    @Test
    void shouldSaveAndFindRoom() {
        // Arrange
        RoomEntity room = new RoomEntity("R101", "101", RoomType.SINGLE, 50.0, true);

        // Act
        roomJpaRepository.save(room);
        Optional<RoomEntity> found = roomJpaRepository.findByRoomNumber("101");

        // Assert
        assertTrue(found.isPresent());
        assertEquals("R101", found.get().getId());
        assertEquals("101", found.get().getRoomNumber());
        assertEquals(RoomType.SINGLE, found.get().getType());
        assertEquals(50.0, found.get().getPricePerNight());
        assertTrue(found.get().getAvailable());
    }

    @Test
    void shouldReturnEmptyForNonexistentRoom() {
        // Act
        Optional<RoomEntity> found = roomJpaRepository.findByRoomNumber("999");

        // Assert
        assertFalse(found.isPresent());
    }

    @Test
    void shouldUpdateRoomAvailability() {
        // Arrange
        RoomEntity room = new RoomEntity("R102", "102", RoomType.DOUBLE, 80.0, true);
        roomJpaRepository.save(room);

        // Act
        RoomEntity savedRoom = roomJpaRepository.findByRoomNumber("102").get();
        savedRoom.setAvailable(false);
        roomJpaRepository.save(savedRoom);

        // Assert
        Optional<RoomEntity> found = roomJpaRepository.findByRoomNumber("102");
        assertTrue(found.isPresent());
        assertFalse(found.get().getAvailable());
    }

    @Test
    void shouldFindAllRooms() {
        // Arrange
        roomJpaRepository.save(new RoomEntity("R101", "101", RoomType.SINGLE, 50.0, true));
        roomJpaRepository.save(new RoomEntity("R102", "102", RoomType.DOUBLE, 80.0, true));
        roomJpaRepository.save(new RoomEntity("R201", "201", RoomType.SUITE, 150.0, true));

        // Act
        var rooms = roomJpaRepository.findAll();

        // Assert
        assertEquals(3, rooms.size());
    }

    @Test
    void shouldDeleteRoom() {
        // Arrange
        RoomEntity room = new RoomEntity("R103", "103", RoomType.SINGLE, 50.0, true);
        roomJpaRepository.save(room);

        // Act
        roomJpaRepository.deleteById("R103");

        // Assert
        Optional<RoomEntity> found = roomJpaRepository.findByRoomNumber("103");
        assertFalse(found.isPresent());
    }
}
