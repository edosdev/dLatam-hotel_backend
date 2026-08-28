package com.hotel.infrastructure.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * DTO de solicitud para crear una habitación.
 */
@Schema(description = "Datos para crear una habitación")
public class RoomRequest {

    @NotBlank(message = "El número de habitación es obligatorio")
    @Schema(description = "Número de la habitación", example = "101", requiredMode = Schema.RequiredMode.REQUIRED)
    private String roomNumber;

    @NotNull(message = "El tipo de habitación es obligatorio")
    @Schema(description = "Tipo de habitación", example = "DOUBLE", requiredMode = Schema.RequiredMode.REQUIRED)
    private com.hotel.domain.valueobject.RoomType type;

    @NotNull(message = "El precio por noche es obligatorio")
    @Positive(message = "El precio debe ser mayor a cero")
    @Schema(description = "Precio por noche", example = "80.0", requiredMode = Schema.RequiredMode.REQUIRED)
    private Double pricePerNight;

    // Getters y Setters
    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public com.hotel.domain.valueobject.RoomType getType() {
        return type;
    }

    public void setType(com.hotel.domain.valueobject.RoomType type) {
        this.type = type;
    }

    public Double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(Double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }
}
