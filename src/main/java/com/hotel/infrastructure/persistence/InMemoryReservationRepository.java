package com.hotel.infrastructure.persistence;

import com.hotel.domain.entity.Reservation;
import com.hotel.domain.repository.ReservationRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Adaptador de infraestructura para almacenamiento de reservas en memoria.
 */
public class InMemoryReservationRepository implements ReservationRepository {

    private final Map<String, Reservation> database = new HashMap<>();

    @Override
    public Optional<Reservation> findById(String id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public void save(Reservation reservation) {
        if (reservation != null) {
            database.put(reservation.getId(), reservation);
        }
    }
}
