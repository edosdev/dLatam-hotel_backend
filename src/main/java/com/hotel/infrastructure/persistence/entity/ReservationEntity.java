package com.hotel.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa una reserva de hotel en la base de datos.
 * Esta clase reside exclusivamente en la capa de infraestructura.
 */
@Entity
@Table(name = "reservations")
public class ReservationEntity {

    @Id
    @Column(name = "id", length = 50)
    private String id;

    @Column(name = "guest_name", nullable = false, length = 100)
    private String guestName;

    @Column(name = "room_number", nullable = false, length = 10)
    private String roomNumber;

    @Column(name = "nights", nullable = false)
    private Integer nights;

    @Column(name = "price_per_night", nullable = false)
    private Double pricePerNight;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private com.hotel.domain.valueobject.ReservationStatus status;

    // Constructor por defecto requerido por JPA
    public ReservationEntity() {
    }

    // Constructor completo
    public ReservationEntity(String id, String guestName, String roomNumber,
                            Integer nights, Double pricePerNight,
                            com.hotel.domain.valueobject.ReservationStatus status) {
        this.id = id;
        this.guestName = guestName;
        this.roomNumber = roomNumber;
        this.nights = nights;
        this.pricePerNight = pricePerNight;
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

    public com.hotel.domain.valueobject.ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(com.hotel.domain.valueobject.ReservationStatus status) {
        this.status = status;
    }
}
