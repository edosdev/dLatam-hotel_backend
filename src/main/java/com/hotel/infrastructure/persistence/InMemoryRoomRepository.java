package com.hotel.infrastructure.persistence;

import com.hotel.domain.entity.Room;
import com.hotel.domain.repository.RoomRepository;
import com.hotel.domain.valueobject.RoomNumber;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Adaptador de infraestructura para almacenamiento de habitaciones en memoria.
 */
public class InMemoryRoomRepository implements RoomRepository {

    private final Map<RoomNumber, Room> database = new HashMap<>();

    @Override
    public Optional<Room> findByRoomNumber(RoomNumber roomNumber) {
        if (roomNumber == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(database.get(roomNumber));
    }

    @Override
    public void save(Room room) {
        if (room != null) {
            database.put(room.getRoomNumber(), room);
        }
    }
}
