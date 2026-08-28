package com.hotel.infrastructure.web.controller;

import com.hotel.application.usecase.HotelService;
import com.hotel.domain.entity.Reservation;
import com.hotel.domain.valueobject.ReservationStatus;
import com.hotel.infrastructure.web.dto.ReservationRequest;
import com.hotel.infrastructure.web.dto.ReservationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestión de reservas de hotel.
 * Expone endpoints semánticos bajo /api/v1/reservations.
 */
@RestController
@RequestMapping("/api/v1/reservations")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
@Tag(name = "Reservas", description = "Gestión de reservas del hotel")
public class ReservationController {

    private final HotelService hotelService;

    public ReservationController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    /**
     * Lista todas las reservas.
     */
    @GetMapping
    @Operation(summary = "Listar reservas", description = "Obtiene todas las reservas registradas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de reservas",
                    content = @Content(schema = @Schema(implementation = ReservationResponse.class)))
    })
    public ResponseEntity<List<ReservationResponse>> listAll() {
        List<ReservationResponse> reservations = hotelService.listAllReservations().stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(reservations);
    }

    /**
     * Obtiene los detalles de una reserva por su ID.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener reserva", description = "Busca una reserva por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reserva encontrada",
                    content = @Content(schema = @Schema(implementation = ReservationResponse.class))),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada",
                    content = @Content(schema = @Schema(implementation = com.hotel.infrastructure.web.dto.ErrorResponse.class)))
    })
    public ResponseEntity<ReservationResponse> getReservation(
            @Parameter(description = "ID de la reserva") @PathVariable String id) {

        Reservation reservation = hotelService.getReservationDetails(id);

        ReservationResponse response = toResponse(reservation);

        return ResponseEntity.ok(response);
    }

    /**
     * Crea una nueva reserva.
     */
    @PostMapping
    @Operation(summary = "Crear reserva", description = "Crea una nueva reserva de hotel")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reserva creada exitosamente",
                    content = @Content(schema = @Schema(implementation = ReservationResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
                    content = @Content(schema = @Schema(implementation = com.hotel.infrastructure.web.dto.ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Habitación no encontrada",
                    content = @Content(schema = @Schema(implementation = com.hotel.infrastructure.web.dto.ErrorResponse.class))),
            @ApiResponse(responseCode = "422", description = "Habitación no disponible",
                    content = @Content(schema = @Schema(implementation = com.hotel.infrastructure.web.dto.ErrorResponse.class)))
    })
    public ResponseEntity<ReservationResponse> createReservation(
            @Valid @RequestBody ReservationRequest request) {

        Reservation reservation = hotelService.makeReservation(
                request.getReservationId(),
                request.getGuestName(),
                request.getRoomNumber(),
                request.getNights()
        );

        ReservationResponse response = toResponse(reservation);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Cancela una reserva existente (vía DELETE, compatibilidad con frontend).
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar reserva", description = "Cancela una reserva existente y libera la habitación")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reserva cancelada exitosamente",
                    content = @Content(schema = @Schema(implementation = ReservationResponse.class))),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada",
                    content = @Content(schema = @Schema(implementation = com.hotel.infrastructure.web.dto.ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "La reserva ya está cancelada",
                    content = @Content(schema = @Schema(implementation = com.hotel.infrastructure.web.dto.ErrorResponse.class)))
    })
    public ResponseEntity<ReservationResponse> cancelReservation(
            @Parameter(description = "ID de la reserva") @PathVariable String id) {

        Reservation reservation = hotelService.cancelReservation(id);

        ReservationResponse response = toResponse(reservation);

        return ResponseEntity.ok(response);
    }

    /**
     * Cancela una reserva existente (vía PATCH, compatibilidad con guía NeonPulse).
     */
    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar reserva", description = "Actualiza el estado de una reserva (ej: cancelar)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reserva actualizada exitosamente",
                    content = @Content(schema = @Schema(implementation = ReservationResponse.class))),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada",
                    content = @Content(schema = @Schema(implementation = com.hotel.infrastructure.web.dto.ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Operación no válida",
                    content = @Content(schema = @Schema(implementation = com.hotel.infrastructure.web.dto.ErrorResponse.class)))
    })
    public ResponseEntity<ReservationResponse> patchReservation(
            @Parameter(description = "ID de la reserva") @PathVariable String id,
            @RequestBody Map<String, String> body) {

        String status = body.get("status");
        if (status == null || status.trim().isEmpty()) {
            throw new com.hotel.domain.exception.InvalidReservationException("El campo 'status' es obligatorio");
        }

        if (!"CANCELLED".equals(status)) {
            throw new com.hotel.domain.exception.InvalidReservationException(
                    "Solo se permite cambiar el estado a CANCELLED");
        }

        Reservation reservation = hotelService.cancelReservation(id);

        ReservationResponse response = toResponse(reservation);

        return ResponseEntity.ok(response);
    }

    /**
     * Convierte una entidad de dominio a DTO de respuesta.
     */
    private ReservationResponse toResponse(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getGuestName().value(),
                reservation.getRoomNumber().value(),
                reservation.getNights().value(),
                reservation.getPricePerNight().value(),
                reservation.calculateTotal(),
                reservation.getStatus().name()
        );
    }
}
