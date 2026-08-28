package com.hotel.domain.repository;

import com.hotel.domain.entity.Room;
import com.hotel.domain.valueobject.RoomNumber;
import java.util.List;
import java.util.Optional;

/**
 * Contrato puro de dominio para el almacenamiento de habitaciones.
 */
public interface RoomRepository {
    Optional<Room> findByRoomNumber(RoomNumber roomNumber);
    List<Room> findAll();
    void save(Room room);
}
