package com.example.carrental.util;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.carrental.model.CarType;
import com.example.carrental.model.Customer;
import com.example.carrental.model.Reservation;
import com.example.carrental.model.SUV;
import com.example.carrental.model.Sedan;
import com.example.carrental.model.Van;

public class TestDataFactory {

	public static Customer createTestCustomer() {
		return new Customer("Test Customer", "test@example.com", "+1-555-1234", "DL123456789");
	}

	public static Customer createTestCustomer(String name, String email) {
		return new Customer(name, email, "+1-555-1234", "DL123456789");
	}

	public static Sedan createTestSedan() {
		return new Sedan("TEST123", "Toyota", "Camry", 2023, BigDecimal.valueOf(45.00));
	}

	public static SUV createTestSUV() {
		return new SUV("TEST456", "Honda", "CR-V", 2023, BigDecimal.valueOf(65.00));
	}

	public static Van createTestVan() {
		return new Van("TEST789", "Ford", "Transit", 2023, BigDecimal.valueOf(80.00));
	}

	public static Reservation createTestReservation(String customerId, String carId, CarType carType) {
		return new Reservation(customerId, carId, carType, LocalDate.now().plusDays(1), LocalDate.now().plusDays(4),
				BigDecimal.valueOf(135.00));
	}
}