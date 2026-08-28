package com.hotel.infrastructure.persistence;

import com.hotel.domain.entity.Room;
import com.hotel.domain.repository.RoomRepository;
import com.hotel.domain.valueobject.RoomNumber;
import com.hotel.domain.valueobject.RoomType;
import com.hotel.infrastructure.persistence.entity.RoomEntity;
import com.hotel.infrastructure.persistence.repository.RoomJpaRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Adaptador JPA que implementa la interfaz de dominio RoomRepository.
 * Mapea entre entidades de dominio y entidades JPA.
 * Anotado con @Primary para que Spring lo inyecte por defecto.
 */
@Repository
@Primary
public class JpaRoomRepository implements RoomRepository {

    private final RoomJpaRepository jpaRepository;

    public JpaRoomRepository(RoomJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Room> findByRoomNumber(RoomNumber roomNumber) {
        if (roomNumber == null) {
            return Optional.empty();
        }
        return jpaRepository.findByRoomNumber(roomNumber.value())
                .map(this::toDomain);
    }

    @Override
    public void save(Room room) {
        if (room != null) {
            jpaRepository.save(toEntity(room));
        }
    }

    // Mapeo de entidad JPA a entidad de dominio
    private Room toDomain(RoomEntity entity) {
        return new Room(
                entity.getId(),
                new RoomNumber(entity.getRoomNumber()),
                entity.getType(),
                new com.hotel.domain.valueobject.Price(entity.getPricePerNight()),
                entity.getAvailable()
        );
    }

    // Mapeo de entidad de dominio a entidad JPA
    private RoomEntity toEntity(Room domain) {
        RoomEntity entity = new RoomEntity();
        entity.setId(domain.getId());
        entity.setRoomNumber(domain.getRoomNumber().value());
        entity.setType(domain.getType());
        entity.setPricePerNight(domain.getPricePerNight().value());
        entity.setAvailable(domain.isAvailable());
        return entity;
    }
}
