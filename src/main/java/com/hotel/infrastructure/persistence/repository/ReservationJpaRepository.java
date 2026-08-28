package com.hotel.infrastructure.persistence.repository;

import com.hotel.infrastructure.persistence.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio Spring Data JPA para operaciones CRUD de reservas.
 * Extiende JpaRepository para automatizar las operaciones estándar.
 */
@Repository
public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, String> {
    // Spring Data JPA resuelve las operaciones CRUD de forma nativa
}
