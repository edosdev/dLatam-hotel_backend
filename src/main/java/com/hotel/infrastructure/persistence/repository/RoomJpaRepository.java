package com.hotel.infrastructure.persistence.repository;

import com.hotel.infrastructure.persistence.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio Spring Data JPA para operaciones CRUD de habitaciones.
 * Extiende JpaRepository para automatizar las operaciones estándar.
 */
@Repository
public interface RoomJpaRepository extends JpaRepository<RoomEntity, String> {

    /**
     * Busca una habitación por su número.
     * Spring Data JPA genera la consulta automáticamente.
     */
    Optional<RoomEntity> findByRoomNumber(String roomNumber);
}
