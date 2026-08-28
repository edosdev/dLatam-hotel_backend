package com.hotel.infrastructure.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * DTO unificado para respuestas de error.
 * Se utiliza en el manejo global de excepciones.
 */
@Schema(description = "Respuesta de error estandarizada")
public class ErrorResponse {

    @Schema(description = "Mensaje descriptivo del error", example = "Habitación no encontrada")
    private String message;

    @Schema(description = "Código semántico del error", example = "RESOURCE_NOT_FOUND")
    private String code;

    @Schema(description = "Timestamp del error")
    private LocalDateTime timestamp;

    // Constructor
    public ErrorResponse(String message, String code, LocalDateTime timestamp) {
        this.message = message;
        this.code = code;
        this.timestamp = timestamp;
    }

    // Getters y Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
