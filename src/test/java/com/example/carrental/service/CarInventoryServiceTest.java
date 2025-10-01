package com.example.carrental.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.carrental.model.Car;
import com.example.carrental.model.CarType;
import com.example.carrental.model.Sedan;
import com.example.carrental.util.TestDataFactory;

class CarInventoryServiceTest {

	private CarInventoryService carInventoryService;

	@BeforeEach
	void setUp() {
		carInventoryService = new CarInventoryService();
	}

	@Test
	void testAddCar() {
		Car sedan = TestDataFactory.createTestSedan();
		carInventoryService.addCar(sedan);

		assertEquals(1, carInventoryService.getTotalCarsOfType(CarType.SEDAN));
		assertEquals(0, carInventoryService.getTotalCarsOfType(CarType.SUV));
	}

	@Test
	void testGetCarsByType() {
		Car sedan1 = TestDataFactory.createTestSedan();
		Car sedan2 = new Sedan("TEST456", "Honda", "Accord", 2023, null);
		Car suv = TestDataFactory.createTestSUV();

		carInventoryService.addCar(sedan1);
		carInventoryService.addCar(sedan2);
		carInventoryService.addCar(suv);

		List<Car> sedans = carInventoryService.getCarsByType(CarType.SEDAN);
		List<Car> suvs = carInventoryService.getCarsByType(CarType.SUV);

		assertEquals(2, sedans.size());
		assertEquals(1, suvs.size());
	}

	@Test
	void testFindCarById() {
		Car sedan = TestDataFactory.createTestSedan();
		carInventoryService.addCar(sedan);

		assertTrue(carInventoryService.findCarById(sedan.getId()).isPresent());
		assertTrue(carInventoryService.findCarById("nonexistent").isEmpty());
	}
}