package com.hotel.application.usecase;

import com.hotel.domain.entity.Reservation;
import com.hotel.domain.repository.EmailNotificationService;
import com.hotel.domain.repository.ReservationRepository;
import com.hotel.domain.repository.RoomRepository;

/**
 * Fachada de aplicación que mantiene compatibilidad con la API de HotelService del Hito 1,
 * delegando la ejecución en casos de uso cohesivos e independientes.
 */
public class HotelService {

    private final MakeReservationUseCase makeReservationUseCase;
    private final CancelReservationUseCase cancelReservationUseCase;
    private final GetReservationDetailsUseCase getReservationDetailsUseCase;

    // Constructor que recibe las interfaces puras de dominio
    public HotelService(
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
        this.makeReservationUseCase = new MakeReservationUseCase(roomRepository, reservationRepository, emailNotificationService);
        this.cancelReservationUseCase = new CancelReservationUseCase(roomRepository, reservationRepository, emailNotificationService);
        this.getReservationDetailsUseCase = new GetReservationDetailsUseCase(reservationRepository);
    }

    // Método para crear una nueva reserva
    public Reservation makeReservation(String reservationId, String guestName, String roomNumber, int nights) {
        return makeReservationUseCase.execute(reservationId, guestName, roomNumber, nights);
    }

    // Método para cancelar una reserva existente
    public Reservation cancelReservation(String reservationId) {
        return cancelReservationUseCase.execute(reservationId);
    }

    // Método para obtener los detalles de una reserva
    public Reservation getReservationDetails(String reservationId) {
        return getReservationDetailsUseCase.execute(reservationId);
    }
}
