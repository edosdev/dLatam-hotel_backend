package com.hotel.infrastructure.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO de solicitud para crear una reserva.
 */
@Schema(description = "Datos para crear una reserva")
public class ReservationRequest {

    @NotBlank(message = "El ID de la reserva es obligatorio")
    @Schema(description = "ID único de la reserva", example = "RES-001", requiredMode = Schema.RequiredMode.REQUIRED)
    private String reservationId;

    @NotBlank(message = "El nombre del huésped es obligatorio")
    @Schema(description = "Nombre completo del huésped", example = "María Gómez", requiredMode = Schema.RequiredMode.REQUIRED)
    private String guestName;

    @NotBlank(message = "El número de habitación es obligatorio")
    @Schema(description = "Número de la habitación a reservar", example = "101", requiredMode = Schema.RequiredMode.REQUIRED)
    private String roomNumber;

    @NotNull(message = "El número de noches es obligatorio")
    @Min(value = 1, message = "El número de noches debe ser al menos 1")
    @Schema(description = "Número de noches de la reserva", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer nights;

    // Getters y Setters
    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Integer getNights() {
        return nights;
    }

    public void setNights(Integer nights) {
        this.nights = nights;
    }
}
