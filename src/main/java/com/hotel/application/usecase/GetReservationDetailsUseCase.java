package com.hotel.application.usecase;

import com.hotel.domain.entity.Reservation;
import com.hotel.domain.exception.ReservationNotFoundException;
import com.hotel.domain.repository.ReservationRepository;

import java.util.Optional;

/**
 * Caso de uso enfocado exclusivamente en consultar los detalles de una reserva existente.
 */
public class GetReservationDetailsUseCase {

    private final ReservationRepository reservationRepository;

    // Inyección obligatoria por constructor
    public GetReservationDetailsUseCase(ReservationRepository reservationRepository) {
        if (reservationRepository == null) {
            throw new IllegalArgumentException("Reservation repository cannot be null");
        }
        this.reservationRepository = reservationRepository;
    }

    public Reservation execute(String reservationId) {
        // Validar reservationId
        if (reservationId == null || reservationId.trim().isEmpty()) {
            throw new IllegalArgumentException("Reservation ID cannot be empty");
        }

        // Buscar la reserva
        Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);
        if (optionalReservation.isEmpty()) {
            throw new ReservationNotFoundException("Reservation " + reservationId + " was not found");
        }

        return optionalReservation.get();
    }
}
