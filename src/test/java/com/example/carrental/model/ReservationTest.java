package com.example.carrental.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.example.carrental.util.TestDataFactory;

class ReservationTest {

	@Test
	void testReservationCreation() {
		Reservation reservation = TestDataFactory.createTestReservation("cust1", "car1", CarType.SEDAN);

		assertNotNull(reservation.getReservationId());
		assertEquals("cust1", reservation.getCustomerId());
		assertEquals("car1", reservation.getCarId());
		assertEquals(CarType.SEDAN, reservation.getCarType());
		assertEquals(ReservationStatus.PENDING, reservation.getStatus());
		assertNotNull(reservation.getCreatedAt());
	}

	@Test
	void testOverlapDetection_Overlapping() {
		Reservation existing = new Reservation("c1", "car1", CarType.SEDAN, LocalDate.of(2024, 1, 10),
				LocalDate.of(2024, 1, 15), null);

		// Overlaps end
		assertTrue(existing.isOverlapping(LocalDate.of(2024, 1, 12), LocalDate.of(2024, 1, 17)));
		// Overlaps start
		assertTrue(existing.isOverlapping(LocalDate.of(2024, 1, 8), LocalDate.of(2024, 1, 12)));
		// Contained within
		assertTrue(existing.isOverlapping(LocalDate.of(2024, 1, 11), LocalDate.of(2024, 1, 14)));
		// Contains existing
		assertTrue(existing.isOverlapping(LocalDate.of(2024, 1, 9), LocalDate.of(2024, 1, 16)));
	}

	@Test
	void testOverlapDetection_NoOverlap() {
		Reservation existing = new Reservation("c1", "car1", CarType.SEDAN, LocalDate.of(2024, 1, 10),
				LocalDate.of(2024, 1, 15), null);

		// Starts when existing ends
		assertFalse(existing.isOverlapping(LocalDate.of(2024, 1, 15), LocalDate.of(2024, 1, 20)));
		// Ends when existing starts
		assertFalse(existing.isOverlapping(LocalDate.of(2024, 1, 5), LocalDate.of(2024, 1, 10)));
		// Completely after
		assertFalse(existing.isOverlapping(LocalDate.of(2024, 1, 16), LocalDate.of(2024, 1, 20)));
		// Completely before
		assertFalse(existing.isOverlapping(LocalDate.of(2024, 1, 5), LocalDate.of(2024, 1, 9)));
	}

	@Test
	void testDurationCalculation() {
		LocalDate start = LocalDate.of(2024, 1, 10);
		LocalDate end = LocalDate.of(2024, 1, 13);
		Reservation reservation = new Reservation("c1", "car1", CarType.SEDAN, start, end, null);

		assertEquals(3, reservation.getDurationInDays());
	}

	@Test
	void testStatusTransitions() {
		Reservation reservation = TestDataFactory.createTestReservation("c1", "car1", CarType.SEDAN);

		assertEquals(ReservationStatus.PENDING, reservation.getStatus());
		assertFalse(reservation.isActive());

		reservation.confirm();
		assertEquals(ReservationStatus.ACTIVE, reservation.getStatus());
		assertTrue(reservation.isActive());

		reservation.cancel();
		assertEquals(ReservationStatus.CANCELLED, reservation.getStatus());
		assertFalse(reservation.isActive());
	}

	@Test
	void testInvalidStatusTransitions() {
		Reservation reservation = TestDataFactory.createTestReservation("c1", "car1", CarType.SEDAN);
		reservation.confirm();

		assertThrows(IllegalStateException.class, reservation::confirm);
	}
}