package com.hotel.infrastructure.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO de respuesta para una habitación.
 */
@Schema(description = "Datos de una habitación")
public class RoomResponse {

    @Schema(description = "ID único de la habitación", example = "R101")
    private String id;

    @Schema(description = "Número de la habitación", example = "101")
    private String roomNumber;

    @Schema(description = "Tipo de habitación", example = "DOUBLE")
    private String type;

    @Schema(description = "Precio por noche", example = "80.0")
    private Double pricePerNight;

    @Schema(description = "Disponibilidad de la habitación", example = "true")
    private Boolean available;

    // Constructor
    public RoomResponse(String id, String roomNumber, String type, Double pricePerNight, Boolean available) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.type = type;
        this.pricePerNight = pricePerNight;
        this.available = available;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(Double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
