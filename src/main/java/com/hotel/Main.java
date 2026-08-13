package com.hotel;

import com.hotel.application.usecase.HotelService;
import com.hotel.domain.entity.Reservation;
import com.hotel.domain.entity.Room;
import com.hotel.domain.repository.EmailNotificationService;
import com.hotel.domain.repository.ReservationRepository;
import com.hotel.domain.repository.RoomRepository;
import com.hotel.domain.valueobject.RoomNumber;
import com.hotel.domain.valueobject.RoomType;
import com.hotel.infrastructure.persistence.ConsoleEmailNotificationAdapter;
import com.hotel.infrastructure.persistence.InMemoryReservationRepository;
import com.hotel.infrastructure.persistence.InMemoryRoomRepository;

/**
 * Clase ejecutable principal para probar y hacer funcionar el sistema en consola.
 * Adaptada bajo Arquitectura Limpia e infraestructura desacoplada.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("====================================================");
        System.out.println("   🏨 SISTEMA DE RESERVAS DE HOTEL 🏨");
        System.out.println("====================================================\n");

        // 1. Creación de Adaptadores de Persistencia e Infraestructura
        RoomRepository roomRepository = new InMemoryRoomRepository();
        ReservationRepository reservationRepository = new InMemoryReservationRepository();
        EmailNotificationService emailPort = new ConsoleEmailNotificationAdapter();

        // 2. Instanciación de la fachada de aplicación con inyección por constructor
        HotelService hotelService = new HotelService(roomRepository, reservationRepository, emailPort);

        // 3. Cargar habitaciones iniciales en el hotel
        System.out.println("--- Cargando Habitaciones en el Hotel ---");
        Room room101 = new Room("R101", "101", RoomType.SINGLE, 50.0, true);
        Room room102 = new Room("R102", "102", RoomType.DOUBLE, 80.0, true);
        roomRepository.save(room101);
        roomRepository.save(room102);
        System.out.println("✅ Habitación 101 (Simple - $50/noche) registrada.");
        System.out.println("✅ Habitación 102 (Doble - $80/noche) registrada.\n");

        // 4. Crear una reserva
        System.out.println("--- 1. Creando una Reserva ---");
        Reservation res1 = hotelService.makeReservation("RES-001", "María Gómez", "101", 3);
        System.out.println("--> Reserva creada con éxito!");
        System.out.println("    ID Reserva: " + res1.getId());
        System.out.println("    Huésped: " + res1.getGuestName().value());
        System.out.println("    Habitación: " + res1.getRoomNumber().value());
        System.out.println("    Noches: " + res1.getNights().value());
        System.out.println("    Total Calculado: $" + res1.calculateTotal());
        System.out.println("    Estado: " + res1.getStatus() + "\n");

        // 5. Intentar reservar una habitación ya ocupada (Demostración de excepciones)
        System.out.println("--- 2. Intentando reservar Habitación 101 nuevamente ---");
        try {
            hotelService.makeReservation("RES-002", "Carlos Pérez", "101", 2);
        } catch (Exception e) {
            System.out.println("⚠️ EXCEPCIÓN DE NEGOCIO CAPTURADA: " + e.getMessage() + "\n");
        }

        // 6. Cancelar la reserva
        System.out.println("--- 3. Cancelando la Reserva RES-001 ---");
        Reservation resCancelada = hotelService.cancelReservation("RES-001");
        System.out.println("--> Reserva cancelada con éxito!");
        System.out.println("    Nuevo Estado de Reserva: " + resCancelada.getStatus());
        System.out.println("    Disponibilidad Habitación 101: " +
                (roomRepository.findByRoomNumber(new RoomNumber("101")).get().isAvailable() ? "Disponible" : "Ocupada") + "\n");

        System.out.println("====================================================");
        System.out.println("   🎉 ¡EL SISTEMA FUNCIONA PERFECTAMENTE! 🎉");
        System.out.println("====================================================");
    }
}
