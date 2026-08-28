package com.hotel.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa una habitación de hotel en la base de datos.
 * Esta clase reside exclusivamente en la capa de infraestructura.
 */
@Entity
@Table(name = "rooms")
public class RoomEntity {

    @Id
    @Column(name = "id", length = 50)
    private String id;

    @Column(name = "room_number", unique = true, nullable = false, length = 10)
    private String roomNumber;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private com.hotel.domain.valueobject.RoomType type;

    @Column(name = "price_per_night", nullable = false)
    private Double pricePerNight;

    @Column(name = "available", nullable = false)
    private Boolean available;

    // Constructor por defecto requerido por JPA
    public RoomEntity() {
    }

    // Constructor completo
    public RoomEntity(String id, String roomNumber, com.hotel.domain.valueobject.RoomType type,
                      Double pricePerNight, Boolean available) {
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

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
