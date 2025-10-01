package com.example.carrental.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.carrental.exception.CarNotAvailableException;
import com.example.carrental.model.Car;
import com.example.carrental.model.CarType;
import com.example.carrental.model.Customer;
import com.example.carrental.model.Reservation;

@Service
public class CarRentalSystemService {
	private final CarInventoryService carInventoryService;
	private final ReservationService reservationService;
	private final CustomerService customerService;

	public CarRentalSystemService(CarInventoryService carInventoryService, ReservationService reservationService,
			CustomerService customerService) {
		this.carInventoryService = carInventoryService;
		this.reservationService = reservationService;
		this.customerService = customerService;
	}

	public Reservation makeReservation(String customerId, CarType carType, LocalDate startDate, int days) {

		Customer customer = customerService.getCustomer(customerId);

		LocalDate endDate = startDate.plusDays(days);
		List<Car> availableCars = getAvailableCars(carType, startDate, endDate);

		if (availableCars.isEmpty()) {
			throw new CarNotAvailableException(carType.toString(), startDate, endDate);
		}

		Car selectedCar = availableCars.get(0);
		return reservationService.createReservation(customerId, selectedCar.getId(), carType, startDate, days,
				selectedCar.getDailyRate());
	}

	public boolean cancelReservation(String reservationId) {
		return reservationService.cancelReservation(reservationId);
	}

	public List<Car> checkAvailability(CarType carType, LocalDate startDate, LocalDate endDate) {
		return getAvailableCars(carType, startDate, endDate);
	}

	public Reservation getReservationDetails(String reservationId) {
		return reservationService.getReservation(reservationId);
	}

	public String addCustomer(Customer customer) {
		return customerService.addCustomer(customer);
	}

	public void addCarToInventory(Car car) {
		carInventoryService.addCar(car);
	}

	private List<Car> getAvailableCars(CarType carType, LocalDate startDate, LocalDate endDate) {
		return carInventoryService.getCarsByType(carType).stream()
				.filter(car -> reservationService.isCarAvailable(car.getId(), startDate, endDate)).toList();
	}
}