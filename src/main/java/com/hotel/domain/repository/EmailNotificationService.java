package com.hotel.domain.repository;

/**
 * Contrato puro de dominio para notificaciones por correo electrónico.
 */
public interface EmailNotificationService {
    void sendReservationConfirmation(String guestName, String reservationId, double totalAmount);
    void sendCancellationNotice(String guestName, String reservationId);
}
