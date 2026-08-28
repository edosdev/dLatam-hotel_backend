package com.hotel.application.usecase;

import com.hotel.domain.entity.Reservation;
import com.hotel.domain.repository.ReservationRepository;

import java.util.List;

/**
 * Caso de uso enfocado exclusivamente en listar todas las reservas del hotel.
 */
public class ListReservationsUseCase {

    private final ReservationRepository reservationRepository;

    // Inyección obligatoria por constructor
    public ListReservationsUseCase(ReservationRepository reservationRepository) {
        if (reservationRepository == null) {
            throw new IllegalArgumentException("Reservation repository cannot be null");
        }
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> execute() {
        return reservationRepository.findAll();
    }
}
