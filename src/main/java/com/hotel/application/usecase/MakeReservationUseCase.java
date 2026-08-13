package com.hotel.application.usecase;

import com.hotel.domain.entity.Reservation;
import com.hotel.domain.entity.Room;
import com.hotel.domain.exception.RoomNotFoundException;
import com.hotel.domain.repository.EmailNotificationService;
import com.hotel.domain.repository.ReservationRepository;
import com.hotel.domain.repository.RoomRepository;
import com.hotel.domain.valueobject.GuestName;
import com.hotel.domain.valueobject.Nights;
import com.hotel.domain.valueobject.RoomNumber;

import java.util.Optional;

/**
 * Caso de uso enfocado exclusivamente en realizar una reserva en el hotel.
 */
public class MakeReservationUseCase {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final EmailNotificationService emailNotificationService;

    // Inyección obligatoria por constructor
    public MakeReservationUseCase(
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

    public Reservation execute(String reservationId, String guestName, String roomNumber, int nights) {
        // Validar reservationId
        if (reservationId == null || reservationId.trim().isEmpty()) {
            throw new IllegalArgumentException("Reservation ID cannot be empty");
        }

        // Instanciación de Value Objects para validación defensiva en el dominio
        GuestName guestNameVO = new GuestName(guestName);
        RoomNumber roomNumberVO = new RoomNumber(roomNumber);
        Nights nightsVO = new Nights(nights);

        // Buscar la habitación
        Optional<Room> optionalRoom = roomRepository.findByRoomNumber(roomNumberVO);
        if (optionalRoom.isEmpty()) {
            throw new RoomNotFoundException("Room " + roomNumber + " was not found");
        }
        Room room = optionalRoom.get();

        // Ocupar habitación
        room.markAsOccupied();

        // Crear la reserva utilizando VOs
        Reservation reservation = new Reservation(
                reservationId,
                guestNameVO,
                roomNumberVO,
                nightsVO,
                room.getPricePerNight()
        );

        // Persistir cambios
        roomRepository.save(room);
        reservationRepository.save(reservation);

        // Notificar por correo
        emailNotificationService.sendReservationConfirmation(
                guestNameVO.value(),
                reservationId,
                reservation.calculateTotal()
        );

        return reservation;
    }
}
