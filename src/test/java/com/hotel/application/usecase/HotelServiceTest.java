package com.hotel.application.usecase;

import com.hotel.domain.entity.Reservation;
import com.hotel.domain.entity.Room;
import com.hotel.domain.exception.ReservationNotFoundException;
import com.hotel.domain.exception.RoomNotFoundException;
import com.hotel.domain.repository.EmailNotificationService;
import com.hotel.domain.repository.ReservationRepository;
import com.hotel.domain.repository.RoomRepository;
import com.hotel.domain.valueobject.RoomNumber;
import com.hotel.domain.valueobject.RoomType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private EmailNotificationService emailNotificationService;

    @InjectMocks
    private HotelService hotelService;

    // --- CONSTRUCTOR TESTS ---

    @Test
    @DisplayName("Should throw IllegalArgumentException when any constructor dependency is null on HotelService")
    void shouldThrowExceptionWhenHotelServiceDependenciesAreNull() {
        assertThrows(IllegalArgumentException.class, () -> new HotelService(null, reservationRepository, emailNotificationService));
        assertThrows(IllegalArgumentException.class, () -> new HotelService(roomRepository, null, emailNotificationService));
        assertThrows(IllegalArgumentException.class, () -> new HotelService(roomRepository, reservationRepository, null));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when any constructor dependency is null on Use Cases")
    void shouldThrowExceptionWhenUseCaseDependenciesAreNull() {
        assertThrows(IllegalArgumentException.class, () -> new MakeReservationUseCase(null, reservationRepository, emailNotificationService));
        assertThrows(IllegalArgumentException.class, () -> new MakeReservationUseCase(roomRepository, null, emailNotificationService));
        assertThrows(IllegalArgumentException.class, () -> new MakeReservationUseCase(roomRepository, reservationRepository, null));

        assertThrows(IllegalArgumentException.class, () -> new CancelReservationUseCase(null, reservationRepository, emailNotificationService));
        assertThrows(IllegalArgumentException.class, () -> new CancelReservationUseCase(roomRepository, null, emailNotificationService));
        assertThrows(IllegalArgumentException.class, () -> new CancelReservationUseCase(roomRepository, reservationRepository, null));

        assertThrows(IllegalArgumentException.class, () -> new GetReservationDetailsUseCase(null));

        assertThrows(IllegalArgumentException.class, () -> new ListReservationsUseCase(null));
        assertThrows(IllegalArgumentException.class, () -> new ListRoomsUseCase(null));
    }

    // --- MAKE RESERVATION TESTS ---

    @Test
    @DisplayName("Should make reservation successfully when room is available")
    void shouldMakeReservationSuccessfully() {
        String resId = "RES-001";
        String guest = "Maria Gomez";
        String roomNum = "101";
        int nights = 2;
        Room room = new Room("R-1", roomNum, RoomType.SINGLE, 60.0, true);

        when(roomRepository.findByRoomNumber(new RoomNumber(roomNum))).thenReturn(Optional.of(room));

        Reservation result = hotelService.makeReservation(resId, guest, roomNum, nights);

        assertNotNull(result);
        assertEquals(resId, result.getId());
        assertEquals(guest, result.getGuestName().value());
        assertEquals(roomNum, result.getRoomNumber().value());
        assertEquals(nights, result.getNights().value());
        assertEquals(120.0, result.calculateTotal());
        assertFalse(room.isAvailable());

        verify(roomRepository).save(room);
        verify(reservationRepository).save(result);
        verify(emailNotificationService).sendReservationConfirmation(guest, resId, 120.0);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when makeReservation parameters are invalid")
    void shouldThrowExceptionWhenMakeReservationParamsAreInvalid() {
        // null or blank reservationId
        assertThrows(IllegalArgumentException.class, () -> hotelService.makeReservation(null, "Maria", "101", 2));
        assertThrows(IllegalArgumentException.class, () -> hotelService.makeReservation("  ", "Maria", "101", 2));
        
        // null or blank guestName (thrown by GuestName VO)
        assertThrows(IllegalArgumentException.class, () -> hotelService.makeReservation("RES-1", null, "101", 2));
        assertThrows(IllegalArgumentException.class, () -> hotelService.makeReservation("RES-1", "", "101", 2));

        // null or blank roomNumber (thrown by RoomNumber VO)
        assertThrows(IllegalArgumentException.class, () -> hotelService.makeReservation("RES-1", "Maria", null, 2));
        assertThrows(IllegalArgumentException.class, () -> hotelService.makeReservation("RES-1", "Maria", "  ", 2));

        // non-positive nights (thrown by Nights VO)
        assertThrows(IllegalArgumentException.class, () -> hotelService.makeReservation("RES-1", "Maria", "101", 0));
        assertThrows(IllegalArgumentException.class, () -> hotelService.makeReservation("RES-1", "Maria", "101", -5));
    }

    @Test
    @DisplayName("Should throw RoomNotFoundException when room does not exist")
    void shouldThrowExceptionWhenRoomNotFoundOnMakeReservation() {
        String roomNum = "999";
        when(roomRepository.findByRoomNumber(new RoomNumber(roomNum))).thenReturn(Optional.empty());

        assertThrows(RoomNotFoundException.class, () -> hotelService.makeReservation("RES-1", "Maria", roomNum, 2));

        verify(reservationRepository, never()).save(any());
        verify(emailNotificationService, never()).sendReservationConfirmation(anyString(), anyString(), anyDouble());
    }

    // --- CANCEL RESERVATION TESTS ---

    @Test
    @DisplayName("Should cancel reservation successfully")
    void shouldCancelReservationSuccessfully() {
        String resId = "RES-001";
        String roomNum = "101";
        Reservation reservation = new Reservation(resId, "Maria Gomez", roomNum, 2, 60.0);
        Room room = new Room("R-1", roomNum, RoomType.SINGLE, 60.0, false);

        when(reservationRepository.findById(resId)).thenReturn(Optional.of(reservation));
        when(roomRepository.findByRoomNumber(new RoomNumber(roomNum))).thenReturn(Optional.of(room));

        Reservation result = hotelService.cancelReservation(resId);

        assertEquals(com.hotel.domain.valueobject.ReservationStatus.CANCELLED, result.getStatus());
        assertTrue(room.isAvailable());

        verify(roomRepository).save(room);
        verify(reservationRepository).save(reservation);
        verify(emailNotificationService).sendCancellationNotice("Maria Gomez", resId);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when cancelReservation parameter is invalid")
    void shouldThrowExceptionWhenCancelReservationIdIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> hotelService.cancelReservation(null));
        assertThrows(IllegalArgumentException.class, () -> hotelService.cancelReservation("  "));
    }

    @Test
    @DisplayName("Should throw ReservationNotFoundException when reservation does not exist for cancel")
    void shouldThrowExceptionWhenReservationNotFoundOnCancel() {
        when(reservationRepository.findById("MISSING")).thenReturn(Optional.empty());
        assertThrows(ReservationNotFoundException.class, () -> hotelService.cancelReservation("MISSING"));
    }

    @Test
    @DisplayName("Should throw RoomNotFoundException when room is missing on cancel")
    void shouldThrowExceptionWhenRoomNotFoundOnCancel() {
        String resId = "RES-001";
        String roomNum = "101";
        Reservation reservation = new Reservation(resId, "Maria Gomez", roomNum, 2, 60.0);

        when(reservationRepository.findById(resId)).thenReturn(Optional.of(reservation));
        when(roomRepository.findByRoomNumber(new RoomNumber(roomNum))).thenReturn(Optional.empty());

        assertThrows(RoomNotFoundException.class, () -> hotelService.cancelReservation(resId));
    }

    // --- GET RESERVATION DETAILS TESTS ---

    @Test
    @DisplayName("Should return reservation details when reservation exists")
    void shouldGetReservationDetailsSuccessfully() {
        String resId = "RES-001";
        Reservation reservation = new Reservation(resId, "Maria Gomez", "101", 2, 60.0);
        when(reservationRepository.findById(resId)).thenReturn(Optional.of(reservation));

        Reservation result = hotelService.getReservationDetails(resId);

        assertNotNull(result);
        assertEquals(resId, result.getId());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when getReservationDetails parameter is invalid")
    void shouldThrowExceptionWhenGetReservationDetailsIdIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> hotelService.getReservationDetails(null));
        assertThrows(IllegalArgumentException.class, () -> hotelService.getReservationDetails(" "));
    }

    @Test
    @DisplayName("Should throw ReservationNotFoundException when reservation is missing on getDetails")
    void shouldThrowExceptionWhenReservationNotFoundOnGetDetails() {
        when(reservationRepository.findById("MISSING")).thenReturn(Optional.empty());
        assertThrows(ReservationNotFoundException.class, () -> hotelService.getReservationDetails("MISSING"));
    }

    // --- LIST RESERVATIONS TESTS ---

    @Test
    @DisplayName("Should list all reservations successfully")
    void shouldListAllReservationsSuccessfully() {
        Reservation r1 = new Reservation("RES-1", "Maria Gomez", "101", 2, 60.0);
        Reservation r2 = new Reservation("RES-2", "Juan Perez", "201", 3, 100.0);
        when(reservationRepository.findAll()).thenReturn(List.of(r1, r2));

        List<Reservation> result = hotelService.listAllReservations();

        assertEquals(2, result.size());
        assertEquals("RES-1", result.get(0).getId());
        assertEquals("RES-2", result.get(1).getId());
        verify(reservationRepository).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no reservations exist")
    void shouldReturnEmptyListWhenNoReservations() {
        when(reservationRepository.findAll()).thenReturn(List.of());

        List<Reservation> result = hotelService.listAllReservations();

        assertTrue(result.isEmpty());
        verify(reservationRepository).findAll();
    }

    // --- LIST ROOMS TESTS ---

    @Test
    @DisplayName("Should list all rooms successfully")
    void shouldListAllRoomsSuccessfully() {
        Room r1 = new Room("R-1", "101", RoomType.SINGLE, 60.0, true);
        Room r2 = new Room("R-2", "201", RoomType.DOUBLE, 100.0, false);
        when(roomRepository.findAll()).thenReturn(List.of(r1, r2));

        List<Room> result = hotelService.listAllRooms();

        assertEquals(2, result.size());
        assertEquals("101", result.get(0).getRoomNumber().value());
        assertEquals("201", result.get(1).getRoomNumber().value());
        verify(roomRepository).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no rooms exist")
    void shouldReturnEmptyListWhenNoRooms() {
        when(roomRepository.findAll()).thenReturn(List.of());

        List<Room> result = hotelService.listAllRooms();

        assertTrue(result.isEmpty());
        verify(roomRepository).findAll();
    }

    // --- LIST USE CASES INDEPENDENT TESTS ---

    @Test
    @DisplayName("ListReservationsUseCase should delegate to repository")
    void listReservationsUseCaseShouldDelegateToRepository() {
        ListReservationsUseCase useCase = new ListReservationsUseCase(reservationRepository);
        Reservation r1 = new Reservation("RES-1", "Maria", "101", 2, 60.0);
        when(reservationRepository.findAll()).thenReturn(List.of(r1));

        List<Reservation> result = useCase.execute();

        assertEquals(1, result.size());
        verify(reservationRepository).findAll();
    }

    @Test
    @DisplayName("ListRoomsUseCase should delegate to repository")
    void listRoomsUseCaseShouldDelegateToRepository() {
        ListRoomsUseCase useCase = new ListRoomsUseCase(roomRepository);
        Room r1 = new Room("R-1", "101", RoomType.SINGLE, 60.0, true);
        when(roomRepository.findAll()).thenReturn(List.of(r1));

        List<Room> result = useCase.execute();

        assertEquals(1, result.size());
        verify(roomRepository).findAll();
    }
}
