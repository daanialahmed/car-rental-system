package com.example.carrental.config;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.carrental.factory.CarFactory;
import com.example.carrental.model.CarType;
import com.example.carrental.model.Customer;
import com.example.carrental.service.CarRentalSystemService;

@Component
public class DataInitializer implements CommandLineRunner {

	private final CarRentalSystemService carRentalService;
	private final CarFactory carFactory;

	public DataInitializer(CarRentalSystemService carRentalService, CarFactory carFactory) {
		this.carRentalService = carRentalService;
		this.carFactory = carFactory;
	}

	@Override
	public void run(String... args) {
		// add cars
		carRentalService.addCarToInventory(
				carFactory.createCar(CarType.SEDAN, "ABC123", "Toyota", "Camry", 2023, BigDecimal.valueOf(45.00)));
		carRentalService.addCarToInventory(
				carFactory.createCar(CarType.SEDAN, "DEF456", "Honda", "Accord", 2023, BigDecimal.valueOf(50.00)));
		carRentalService.addCarToInventory(
				carFactory.createCar(CarType.SUV, "GHI789", "Toyota", "RAV4", 2023, BigDecimal.valueOf(65.00)));
		carRentalService.addCarToInventory(
				carFactory.createCar(CarType.SUV, "JKL012", "Honda", "CR-V", 2023, BigDecimal.valueOf(70.00)));
		carRentalService.addCarToInventory(
				carFactory.createCar(CarType.VAN, "MNO345", "Honda", "Odyssey", 2023, BigDecimal.valueOf(80.00)));
		carRentalService.addCarToInventory(
				carFactory.createCar(CarType.VAN, "PQR678", "Ford", "Transit", 2023, BigDecimal.valueOf(85.00)));

		// add customers
		carRentalService.addCustomer(new Customer("John Doe", "john@example.com", "+1-555-0123", "DL123456789"));
		carRentalService.addCustomer(new Customer("Jane Smith", "jane@example.com", "+1-555-0456", "DL987654321"));

		System.out.println("Initialized: 6 cars (2 Sedans, 2 SUVs, 2 Vans) and 2 customers");
	}
}
