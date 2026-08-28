package com.hotel.domain.repository;

import com.hotel.domain.entity.Reservation;
import java.util.List;
import java.util.Optional;

/**
 * Contrato puro de dominio para el almacenamiento de reservas.
 */
public interface ReservationRepository {
    Optional<Reservation> findById(String id);
    List<Reservation> findAll();
    void save(Reservation reservation);
}
