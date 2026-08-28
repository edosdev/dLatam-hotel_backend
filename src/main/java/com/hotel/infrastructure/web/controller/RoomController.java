package com.hotel.infrastructure.web.controller;

import com.hotel.domain.entity.Room;
import com.hotel.domain.exception.RoomNotFoundException;
import com.hotel.domain.repository.RoomRepository;
import com.hotel.domain.valueobject.Price;
import com.hotel.domain.valueobject.RoomNumber;
import com.hotel.infrastructure.web.dto.RoomRequest;
import com.hotel.infrastructure.web.dto.RoomResponse;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para gestión de habitaciones de hotel.
 * Expone endpoints semánticos bajo /api/v1/rooms.
 */
@RestController
@RequestMapping("/api/v1/rooms")
@Tag(name = "Habitaciones", description = "Gestión de habitaciones del hotel")
public class RoomController {

    private final RoomRepository roomRepository;

    public RoomController(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    /**
     * Obtiene una habitación por su número.
     */
    @GetMapping("/{roomNumber}")
    @Operation(summary = "Obtener habitación", description = "Busca una habitación por su número")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Habitación encontrada",
                    content = @Content(schema = @Schema(implementation = RoomResponse.class))),
            @ApiResponse(responseCode = "404", description = "Habitación no encontrada",
                    content = @Content(schema = @Schema(implementation = com.hotel.infrastructure.web.dto.ErrorResponse.class)))
    })
    public ResponseEntity<RoomResponse> getRoom(
            @Parameter(description = "Número de la habitación") @PathVariable String roomNumber) {

        Room room = roomRepository.findByRoomNumber(new RoomNumber(roomNumber))
                .orElseThrow(() -> new RoomNotFoundException("Habitación " + roomNumber + " no encontrada"));

        RoomResponse response = new RoomResponse(
                room.getId(),
                room.getRoomNumber().value(),
                room.getType().name(),
                room.getPricePerNight().value(),
                room.isAvailable()
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Crea una nueva habitación.
     */
    @PostMapping
    @Operation(summary = "Crear habitación", description = "Registra una nueva habitación en el hotel")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Habitación creada exitosamente",
                    content = @Content(schema = @Schema(implementation = RoomResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
                    content = @Content(schema = @Schema(implementation = com.hotel.infrastructure.web.dto.ErrorResponse.class)))
    })
    public ResponseEntity<RoomResponse> createRoom(
            @Valid @RequestBody RoomRequest request) {

        // Verificar que no exista una habitación con ese número
        if (roomRepository.findByRoomNumber(new RoomNumber(request.getRoomNumber())).isPresent()) {
            throw new com.hotel.domain.exception.InvalidReservationException(
                    "Ya existe una habitación con el número " + request.getRoomNumber());
        }

        // Crear la entidad Room con un ID generado
        String roomId = "R" + request.getRoomNumber();
        Room room = new Room(roomId, request.getRoomNumber(), request.getType(),
                request.getPricePerNight(), true);

        roomRepository.save(room);

        RoomResponse response = new RoomResponse(
                room.getId(),
                room.getRoomNumber().value(),
                room.getType().name(),
                room.getPricePerNight().value(),
                room.isAvailable()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
