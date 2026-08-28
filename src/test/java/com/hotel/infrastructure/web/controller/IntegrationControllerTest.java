package com.hotel.infrastructure.web.controller;

import com.hotel.infrastructure.web.dto.ErrorResponse;
import com.hotel.infrastructure.web.dto.ReservationRequest;
import com.hotel.infrastructure.web.dto.ReservationResponse;
import com.hotel.infrastructure.web.dto.RoomRequest;
import com.hotel.infrastructure.web.dto.RoomResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests de integración para RoomController y ReservationController.
 * Cada test crea su propia habitación y reserva con IDs únicos para evitar conflictos de estado.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class IntegrationControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private <T> HttpEntity<T> jsonEntity(T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }

    private void createRoom(String roomNumber, String type, double price) {
        RoomRequest roomRequest = new RoomRequest();
        roomRequest.setRoomNumber(roomNumber);
        roomRequest.setType(com.hotel.domain.valueobject.RoomType.valueOf(type));
        roomRequest.setPricePerNight(price);
        restTemplate.postForEntity("/api/v1/rooms", jsonEntity(roomRequest), RoomResponse.class);
    }

    // ========== ROOM TESTS ==========

    @Test
    void shouldCreateRoomSuccessfully() {
        RoomRequest request = new RoomRequest();
        request.setRoomNumber("INT-A101");
        request.setType(com.hotel.domain.valueobject.RoomType.SINGLE);
        request.setPricePerNight(50.0);

        ResponseEntity<RoomResponse> response = restTemplate.postForEntity(
                "/api/v1/rooms", jsonEntity(request), RoomResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("INT-A101", response.getBody().getRoomNumber());
        assertEquals("SINGLE", response.getBody().getType());
        assertEquals(50.0, response.getBody().getPricePerNight());
        assertTrue(response.getBody().getAvailable());
    }

    @Test
    void shouldGetRoomSuccessfully() {
        createRoom("INT-B201", "DOUBLE", 80.0);

        ResponseEntity<RoomResponse> response = restTemplate.getForEntity(
                "/api/v1/rooms/INT-B201", RoomResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("INT-B201", response.getBody().getRoomNumber());
        assertEquals("DOUBLE", response.getBody().getType());
        assertEquals(80.0, response.getBody().getPricePerNight());
    }

    @Test
    void shouldReturnNotFoundForNonexistentRoom() {
        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(
                "/api/v1/rooms/NONEXISTENT", ErrorResponse.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("RESOURCE_NOT_FOUND", response.getBody().getCode());
    }

    @Test
    void shouldReturnBadRequestForInvalidRoomData() {
        RoomRequest request = new RoomRequest();

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                "/api/v1/rooms", jsonEntity(request), ErrorResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("VALIDATION_ERROR", response.getBody().getCode());
    }

    // ========== RESERVATION TESTS ==========

    @Test
    void shouldCreateReservationSuccessfully() {
        createRoom("INT-C101", "SINGLE", 50.0);

        ReservationRequest request = new ReservationRequest();
        request.setReservationId("INT-RES-001");
        request.setGuestName("María Gómez");
        request.setRoomNumber("INT-C101");
        request.setNights(3);

        ResponseEntity<ReservationResponse> response = restTemplate.postForEntity(
                "/api/v1/reservations", jsonEntity(request), ReservationResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("INT-RES-001", response.getBody().getId());
        assertEquals("María Gómez", response.getBody().getGuestName());
        assertEquals("INT-C101", response.getBody().getRoomNumber());
        assertEquals(3, response.getBody().getNights());
        assertEquals(50.0, response.getBody().getPricePerNight());
        assertEquals(150.0, response.getBody().getTotal());
        assertEquals("CONFIRMED", response.getBody().getStatus());
    }

    @Test
    void shouldGetReservationSuccessfully() {
        createRoom("INT-D101", "SINGLE", 60.0);

        ReservationRequest createRequest = new ReservationRequest();
        createRequest.setReservationId("INT-RES-002");
        createRequest.setGuestName("Carlos Pérez");
        createRequest.setRoomNumber("INT-D101");
        createRequest.setNights(2);

        ResponseEntity<ReservationResponse> createResponse = restTemplate.postForEntity(
                "/api/v1/reservations", jsonEntity(createRequest), ReservationResponse.class);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());

        ResponseEntity<ReservationResponse> getResponse = restTemplate.getForEntity(
                "/api/v1/reservations/INT-RES-002", ReservationResponse.class);

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals("INT-RES-002", getResponse.getBody().getId());
        assertEquals("Carlos Pérez", getResponse.getBody().getGuestName());
        assertEquals(120.0, getResponse.getBody().getTotal());
    }

    @Test
    void shouldCancelReservationSuccessfully() {
        createRoom("INT-E101", "SINGLE", 70.0);

        ReservationRequest createRequest = new ReservationRequest();
        createRequest.setReservationId("INT-RES-003");
        createRequest.setGuestName("Ana López");
        createRequest.setRoomNumber("INT-E101");
        createRequest.setNights(1);

        ResponseEntity<ReservationResponse> createResponse = restTemplate.postForEntity(
                "/api/v1/reservations", jsonEntity(createRequest), ReservationResponse.class);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());

        ResponseEntity<ReservationResponse> cancelResponse = restTemplate.exchange(
                "/api/v1/reservations/INT-RES-003",
                org.springframework.http.HttpMethod.DELETE,
                null,
                ReservationResponse.class);

        assertEquals(HttpStatus.OK, cancelResponse.getStatusCode());
        assertNotNull(cancelResponse.getBody());
        assertEquals("INT-RES-003", cancelResponse.getBody().getId());
        assertEquals("CANCELLED", cancelResponse.getBody().getStatus());
    }

    @Test
    void shouldReturnNotFoundForNonexistentReservation() {
        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(
                "/api/v1/reservations/INT-NONEXISTENT", ErrorResponse.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("RESOURCE_NOT_FOUND", response.getBody().getCode());
    }

    @Test
    void shouldReturnBadRequestForInvalidReservationData() {
        ReservationRequest request = new ReservationRequest();

        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                "/api/v1/reservations", jsonEntity(request), ErrorResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("VALIDATION_ERROR", response.getBody().getCode());
    }
}
