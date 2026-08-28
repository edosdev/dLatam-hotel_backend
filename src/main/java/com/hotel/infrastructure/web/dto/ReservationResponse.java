package com.hotel.infrastructure.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO de respuesta para una reserva.
 */
@Schema(description = "Datos de una reserva")
public class ReservationResponse {

    @Schema(description = "ID único de la reserva", example = "RES-001")
    private String id;

    @Schema(description = "Nombre del huésped", example = "María Gómez")
    private String guestName;

    @Schema(description = "Número de habitación", example = "101")
    private String roomNumber;

    @Schema(description = "Número de noches", example = "3")
    private Integer nights;

    @Schema(description = "Precio por noche", example = "50.0")
    private Double pricePerNight;

    @Schema(description = "Total a pagar", example = "150.0")
    private Double total;

    @Schema(description = "Estado de la reserva", example = "CONFIRMED")
    private String status;

    // Constructor
    public ReservationResponse(String id, String guestName, String roomNumber, Integer nights,
                              Double pricePerNight, Double total, String status) {
        this.id = id;
        this.guestName = guestName;
        this.roomNumber = roomNumber;
        this.nights = nights;
        this.pricePerNight = pricePerNight;
        this.total = total;
        this.status = status;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(Double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
