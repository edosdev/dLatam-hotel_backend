package com.hotel.application.usecase;

import com.hotel.domain.entity.Reservation;
import com.hotel.domain.entity.Room;
import com.hotel.domain.exception.ReservationNotFoundException;
import com.hotel.domain.exception.RoomNotFoundException;
import com.hotel.domain.repository.EmailNotificationService;
import com.hotel.domain.repository.ReservationRepository;
import com.hotel.domain.repository.RoomRepository;

import java.util.Optional;

/**
 * Caso de uso enfocado exclusivamente en cancelar una reserva y liberar la habitación correspondiente.
 */
public class CancelReservationUseCase {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final EmailNotificationService emailNotificationService;

    // Inyección obligatoria por constructor
    public CancelReservationUseCase(
            RoomRepository roomRepository,
            ReservationRepository reservationRepository,
            EmailNotificationService emailNotificationService
    ) {
        if (roomRepository == null) {
            throw new IllegalArgumentException("Room repository cannot be null");
        }
        if (reservationRepository == null) {
            throw new IllegalArgumentException("Reservation repository cannot be null");
        }
        if (emailNotificationService == null) {
            throw new IllegalArgumentException("Notification service cannot be null");
        }
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
        this.emailNotificationService = emailNotificationService;
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
        Reservation reservation = optionalReservation.get();

        // Buscar la habitación asociada
        Optional<Room> optionalRoom = roomRepository.findByRoomNumber(reservation.getRoomNumber());
        if (optionalRoom.isEmpty()) {
            throw new RoomNotFoundException("Room " + reservation.getRoomNumber().value() + " was not found");
        }
        Room room = optionalRoom.get();

        // Cancelar reserva y liberar habitación (métodos ricos de negocio)
        reservation.cancel();
        room.markAsAvailable();

        // Persistir cambios
        roomRepository.save(room);
        reservationRepository.save(reservation);

        // Notificar cancelación por correo
        emailNotificationService.sendCancellationNotice(
                reservation.getGuestName().value(),
                reservationId
        );

        return reservation;
    }
}
