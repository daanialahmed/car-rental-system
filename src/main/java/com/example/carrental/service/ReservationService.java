package com.example.carrental.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.example.carrental.exception.ReservationNotFoundException;
import com.example.carrental.model.CarType;
import com.example.carrental.model.Reservation;
import com.example.carrental.model.ReservationStatus;

@Service
public class ReservationService {
	private final Map<String, Reservation> reservations = new ConcurrentHashMap<>();

	public Reservation createReservation(String customerId, String carId, CarType carType, LocalDate startDate,
			int days, BigDecimal dailyRate) {
		LocalDate endDate = startDate.plusDays(days);
		BigDecimal totalCost = dailyRate.multiply(BigDecimal.valueOf(days));

		Reservation reservation = new Reservation(customerId, carId, carType, startDate, endDate, totalCost);
		reservations.put(reservation.getReservationId(), reservation);
		return reservation;
	}

	public boolean cancelReservation(String reservationId) {
		Reservation reservation = reservations.get(reservationId);
		if (reservation == null) {
			throw new ReservationNotFoundException(reservationId);
		}
		reservation.cancel();
		return true;
	}

	public List<Reservation> getActiveReservationsForCar(String carId) {
		return reservations.values().stream().filter(r -> r.getCarId().equals(carId) && r.isActive()).toList();
	}

	public boolean isCarAvailable(String carId, LocalDate startDate, LocalDate endDate) {
		return reservations.values().stream().filter(r -> r.getCarId().equals(carId)).filter(this::isBlocking)
				.noneMatch(r -> r.isOverlapping(startDate, endDate));
	}

	public Reservation getReservation(String reservationId) {
		Reservation reservation = reservations.get(reservationId);
		if (reservation == null) {
			throw new ReservationNotFoundException(reservationId);
		}
		return reservation;
	}

	public List<Reservation> getReservationsByCustomer(String customerId) {
		return reservations.values().stream().filter(r -> r.getCustomerId().equals(customerId)).toList();
	}

	private boolean isBlocking(Reservation r) {
		return r.getStatus() == ReservationStatus.PENDING || r.getStatus() == ReservationStatus.ACTIVE;
	}
}