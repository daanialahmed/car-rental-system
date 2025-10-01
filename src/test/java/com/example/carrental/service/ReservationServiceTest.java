package com.example.carrental.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.carrental.exception.ReservationNotFoundException;
import com.example.carrental.model.CarType;
import com.example.carrental.model.Reservation;
import com.example.carrental.model.ReservationStatus;

class ReservationServiceTest {

	private ReservationService reservationService;

	@BeforeEach
	void setUp() {
		reservationService = new ReservationService();
	}

	@Test
	void testCreateReservation() {
		LocalDate startDate = LocalDate.now().plusDays(1);
		Reservation reservation = reservationService.createReservation("cust1", "car1", CarType.SEDAN, startDate, 3,
				BigDecimal.valueOf(45.00));

		assertNotNull(reservation.getReservationId());
		assertEquals("cust1", reservation.getCustomerId());
		assertEquals("car1", reservation.getCarId());
		assertEquals(startDate, reservation.getStartDate());
		assertEquals(startDate.plusDays(3), reservation.getEndDate());
		assertEquals(BigDecimal.valueOf(135.00), reservation.getTotalCost());
	}

	@Test
	void testCancelReservation() {
		Reservation reservation = reservationService.createReservation("cust1", "car1", CarType.SEDAN,
				LocalDate.now().plusDays(1), 3, BigDecimal.valueOf(45.00));

		assertTrue(reservationService.cancelReservation(reservation.getReservationId()));
		assertEquals(ReservationStatus.CANCELLED, reservation.getStatus());
	}

	@Test
	void testCancelNonexistentReservation() {
		assertThrows(ReservationNotFoundException.class, () -> reservationService.cancelReservation("nonexistent"));
	}

	@Test
	void testCarAvailabilityChecking() {
		LocalDate start1 = LocalDate.of(2024, 1, 10);
		Reservation reservation1 = reservationService.createReservation("cust1", "car1", CarType.SEDAN, start1, 3,
				BigDecimal.valueOf(45.00));
		reservation1.confirm();

		assertFalse(reservationService.isCarAvailable("car1", LocalDate.of(2024, 1, 11), LocalDate.of(2024, 1, 14)));

		assertTrue(reservationService.isCarAvailable("car1", LocalDate.of(2024, 1, 13), LocalDate.of(2024, 1, 16)));

		assertTrue(reservationService.isCarAvailable("car1", LocalDate.of(2024, 1, 7), LocalDate.of(2024, 1, 10)));
	}

	@Test
	void testGetReservationsByCustomer() {
		reservationService.createReservation("cust1", "car1", CarType.SEDAN, LocalDate.now().plusDays(1), 3,
				BigDecimal.valueOf(45.00));
		reservationService.createReservation("cust1", "car2", CarType.SUV, LocalDate.now().plusDays(5), 2,
				BigDecimal.valueOf(65.00));
		reservationService.createReservation("cust2", "car3", CarType.VAN, LocalDate.now().plusDays(10), 4,
				BigDecimal.valueOf(80.00));

		List<Reservation> cust1Reservations = reservationService.getReservationsByCustomer("cust1");
		List<Reservation> cust2Reservations = reservationService.getReservationsByCustomer("cust2");

		assertEquals(2, cust1Reservations.size());
		assertEquals(1, cust2Reservations.size());
	}

	@Test
	void testGetActiveReservationsForCar() {
		Reservation r1 = reservationService.createReservation("cust1", "car1", CarType.SEDAN,
				LocalDate.now().plusDays(1), 3, BigDecimal.valueOf(45.00));
		Reservation r2 = reservationService.createReservation("cust2", "car1", CarType.SEDAN,
				LocalDate.now().plusDays(10), 2, BigDecimal.valueOf(45.00));

		r1.confirm();
		r2.cancel();

		List<Reservation> activeReservations = reservationService.getActiveReservationsForCar("car1");

		assertEquals(1, activeReservations.size());
		assertEquals(r1.getReservationId(), activeReservations.get(0).getReservationId());
	}
}
