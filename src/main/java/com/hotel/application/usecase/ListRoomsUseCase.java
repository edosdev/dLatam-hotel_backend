package com.hotel.application.usecase;

import com.hotel.domain.entity.Room;
import com.hotel.domain.repository.RoomRepository;

import java.util.List;

/**
 * Caso de uso enfocado exclusivamente en listar todas las habitaciones del hotel.
 */
public class ListRoomsUseCase {

    private final RoomRepository roomRepository;

    // Inyección obligatoria por constructor
    public ListRoomsUseCase(RoomRepository roomRepository) {
        if (roomRepository == null) {
            throw new IllegalArgumentException("Room repository cannot be null");
        }
        this.roomRepository = roomRepository;
    }

    public List<Room> execute() {
        return roomRepository.findAll();
    }
}
