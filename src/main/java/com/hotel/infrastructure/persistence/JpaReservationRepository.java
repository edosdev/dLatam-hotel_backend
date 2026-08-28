package com.hotel.infrastructure.persistence;

import com.hotel.domain.entity.Reservation;
import com.hotel.domain.repository.ReservationRepository;
import com.hotel.domain.valueobject.GuestName;
import com.hotel.domain.valueobject.Nights;
import com.hotel.domain.valueobject.Price;
import com.hotel.domain.valueobject.ReservationStatus;
import com.hotel.domain.valueobject.RoomNumber;
import com.hotel.infrastructure.persistence.entity.ReservationEntity;
import com.hotel.infrastructure.persistence.repository.ReservationJpaRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador JPA que implementa la interfaz de dominio ReservationRepository.
 * Mapea entre entidades de dominio y entidades JPA.
 * Anotado con @Primary para que Spring lo inyecte por defecto.
 */
@Repository
@Primary
public class JpaReservationRepository implements ReservationRepository {

    private final ReservationJpaRepository jpaRepository;

    public JpaReservationRepository(ReservationJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Reservation> findById(String id) {
        if (id == null) {
            return Optional.empty();
        }
        return jpaRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public List<Reservation> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public void save(Reservation reservation) {
        if (reservation != null) {
            jpaRepository.save(toEntity(reservation));
        }
    }

    // Mapeo de entidad JPA a entidad de dominio
    private Reservation toDomain(ReservationEntity entity) {
        return new Reservation(
                entity.getId(),
                new GuestName(entity.getGuestName()),
                new RoomNumber(entity.getRoomNumber()),
                new Nights(entity.getNights()),
                new Price(entity.getPricePerNight())
        );
    }

    // Mapeo de entidad de dominio a entidad JPA
    private ReservationEntity toEntity(Reservation domain) {
        ReservationEntity entity = new ReservationEntity();
        entity.setId(domain.getId());
        entity.setGuestName(domain.getGuestName().value());
        entity.setRoomNumber(domain.getRoomNumber().value());
        entity.setNights(domain.getNights().value());
        entity.setPricePerNight(domain.getPricePerNight().value());
        entity.setStatus(domain.getStatus());
        return entity;
    }
}
