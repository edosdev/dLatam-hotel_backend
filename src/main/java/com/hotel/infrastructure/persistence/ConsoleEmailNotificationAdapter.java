package com.hotel.infrastructure.persistence;

import com.hotel.domain.repository.EmailNotificationService;

/**
 * Adaptador de infraestructura que implementa notificaciones enviadas a la consola.
 */
public class ConsoleEmailNotificationAdapter implements EmailNotificationService {

    @Override
    public void sendReservationConfirmation(String guestName, String reservationId, double totalAmount) {
        System.out.println("📧 [CORREO ENVIADO] Confirmación a " + guestName +
                " | Reserva: " + reservationId + " | Total A Pagar: $" + totalAmount);
    }

    @Override
    public void sendCancellationNotice(String guestName, String reservationId) {
        System.out.println("📧 [CORREO ENVIADO] Notificación de Cancelación a " + guestName +
                " | Reserva: " + reservationId);
    }
}
