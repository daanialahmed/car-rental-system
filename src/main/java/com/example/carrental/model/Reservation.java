package com.example.carrental.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Reservation {

	@EqualsAndHashCode.Include
	private final String reservationId;
	private final String customerId;
	private final String carId;
	private final CarType carType;
	private final LocalDate startDate;
	private final LocalDate endDate;
	private final BigDecimal totalCost;
	private final LocalDateTime createdAt;
	private ReservationStatus status;

	public Reservation(String customerId, String carId, CarType carType, LocalDate startDate, LocalDate endDate,
			BigDecimal totalCost) {
		this.reservationId = UUID.randomUUID().toString();
		this.customerId = customerId;
		this.carId = carId;
		this.carType = carType;
		this.startDate = startDate;
		this.endDate = endDate;
		this.totalCost = totalCost;
		this.createdAt = LocalDateTime.now();
		this.status = ReservationStatus.PENDING;
	}

	public boolean isOverlapping(LocalDate requestStart, LocalDate requestEnd) {
		return !(requestStart.isAfter(endDate) || requestEnd.isBefore(startDate) || requestEnd.equals(startDate));
	}

	public int getDurationInDays() {
		return (int) ChronoUnit.DAYS.between(startDate, endDate);
	}

	public boolean isActive() {
		return status == ReservationStatus.ACTIVE;
	}

	public void confirm() {
		if (status != ReservationStatus.PENDING) {
			throw new IllegalStateException("Can only confirm pending reservations");
		}
		this.status = ReservationStatus.ACTIVE;
	}

	public void cancel() {
		this.status = ReservationStatus.CANCELLED;
	}
}
