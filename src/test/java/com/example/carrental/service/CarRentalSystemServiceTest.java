package com.example.carrental.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.carrental.exception.CarNotAvailableException;
import com.example.carrental.exception.CustomerNotFoundException;
import com.example.carrental.model.Car;
import com.example.carrental.model.CarType;
import com.example.carrental.model.Customer;
import com.example.carrental.model.Reservation;
import com.example.carrental.model.ReservationStatus;
import com.example.carrental.model.Sedan;
import com.example.carrental.util.TestDataFactory;

class CarRentalSystemServiceTest {

	private CarRentalSystemService carRentalSystemService;
	private CarInventoryService carInventoryService;
	private ReservationService reservationService;
	private CustomerService customerService;

	@BeforeEach
	void setUp() {
		carInventoryService = new CarInventoryService();
		reservationService = new ReservationService();
		customerService = new CustomerService();
		carRentalSystemService = new CarRentalSystemService(carInventoryService, reservationService, customerService);
	}

	@Test
	void testSuccessfulReservation() {
		Customer customer = TestDataFactory.createTestCustomer();
		customerService.addCustomer(customer);

		Car sedan = TestDataFactory.createTestSedan();
		carInventoryService.addCar(sedan);

		LocalDate startDate = LocalDate.now().plusDays(1);
		Reservation reservation = carRentalSystemService.makeReservation(customer.getCustomerId(), CarType.SEDAN,
				startDate, 3);

		assertNotNull(reservation);
		assertEquals(customer.getCustomerId(), reservation.getCustomerId());
		assertEquals(CarType.SEDAN, reservation.getCarType());
		assertEquals(startDate, reservation.getStartDate());
		assertEquals(3, reservation.getDurationInDays());
	}

	@Test
	void testReservationWithNonexistentCustomer() {
		Car sedan = TestDataFactory.createTestSedan();
		carInventoryService.addCar(sedan);

		assertThrows(CustomerNotFoundException.class, () -> carRentalSystemService.makeReservation("nonexistent",
				CarType.SEDAN, LocalDate.now().plusDays(1), 3));
	}

	@Test
	void testReservationWithNoAvailableCars() {
		Customer customer = TestDataFactory.createTestCustomer();
		customerService.addCustomer(customer);

		assertThrows(CarNotAvailableException.class, () -> carRentalSystemService
				.makeReservation(customer.getCustomerId(), CarType.SEDAN, LocalDate.now().plusDays(1), 3));
	}

	@Test
	void testReservationWithOverlappingDates() {
		Customer customer1 = TestDataFactory.createTestCustomer("Customer 1", "customer1@example.com");
		Customer customer2 = TestDataFactory.createTestCustomer("Customer 2", "customer2@example.com");
		customerService.addCustomer(customer1);
		customerService.addCustomer(customer2);

		Car sedan = TestDataFactory.createTestSedan();
		carInventoryService.addCar(sedan);

		LocalDate start1 = LocalDate.of(2024, 1, 10);
		Reservation res1 = carRentalSystemService.makeReservation(customer1.getCustomerId(), CarType.SEDAN, start1, 3);
		res1.confirm();

		LocalDate start2 = LocalDate.of(2024, 1, 11);
		assertThrows(CarNotAvailableException.class,
				() -> carRentalSystemService.makeReservation(customer2.getCustomerId(), CarType.SEDAN, start2, 3));
	}

	@Test
	void testReservationWithNonOverlappingDates() {
		Customer customer1 = TestDataFactory.createTestCustomer("Customer 1", "customer1@example.com");
		Customer customer2 = TestDataFactory.createTestCustomer("Customer 2", "customer2@example.com");
		customerService.addCustomer(customer1);
		customerService.addCustomer(customer2);

		Car sedan = TestDataFactory.createTestSedan();
		carInventoryService.addCar(sedan);

		LocalDate start1 = LocalDate.of(2024, 1, 10);
		Reservation res1 = carRentalSystemService.makeReservation(customer1.getCustomerId(), CarType.SEDAN, start1, 3);
		res1.confirm();

		LocalDate start2 = LocalDate.of(2024, 1, 13);
		Reservation res2 = carRentalSystemService.makeReservation(customer2.getCustomerId(), CarType.SEDAN, start2, 3);

		assertNotNull(res2);
		assertEquals(customer2.getCustomerId(), res2.getCustomerId());
	}

	@Test
	void testMultipleCarsOfSameType() {
		Customer customer1 = TestDataFactory.createTestCustomer("Customer 1", "customer1@example.com");
		Customer customer2 = TestDataFactory.createTestCustomer("Customer 2", "customer2@example.com");
		customerService.addCustomer(customer1);
		customerService.addCustomer(customer2);

		Car sedan1 = TestDataFactory.createTestSedan();
		Car sedan2 = new Sedan("TEST999", "Honda", "Accord", 2023, new BigDecimal("85"));
		carInventoryService.addCar(sedan1);
		carInventoryService.addCar(sedan2);

		LocalDate startDate = LocalDate.now().plusDays(1);
		Reservation res1 = carRentalSystemService.makeReservation(customer1.getCustomerId(), CarType.SEDAN, startDate,
				3);
		Reservation res2 = carRentalSystemService.makeReservation(customer2.getCustomerId(), CarType.SEDAN, startDate,
				3);

		assertNotNull(res1);
		assertNotNull(res2);
		assertNotEquals(res1.getCarId(), res2.getCarId());
	}

	@Test
	void testCheckAvailability() {
		Car sedan = TestDataFactory.createTestSedan();
		Car suv = TestDataFactory.createTestSUV();
		carInventoryService.addCar(sedan);
		carInventoryService.addCar(suv);

		LocalDate startDate = LocalDate.now().plusDays(1);
		LocalDate endDate = startDate.plusDays(3);

		List<Car> availableSedans = carRentalSystemService.checkAvailability(CarType.SEDAN, startDate, endDate);
		List<Car> availableSUVs = carRentalSystemService.checkAvailability(CarType.SUV, startDate, endDate);
		List<Car> availableVans = carRentalSystemService.checkAvailability(CarType.VAN, startDate, endDate);

		assertEquals(1, availableSedans.size());
		assertEquals(1, availableSUVs.size());
		assertEquals(0, availableVans.size());
	}

	@Test
	void testCancelReservation() {
		Customer customer = TestDataFactory.createTestCustomer();
		customerService.addCustomer(customer);

		Car sedan = TestDataFactory.createTestSedan();
		carInventoryService.addCar(sedan);

		Reservation reservation = carRentalSystemService.makeReservation(customer.getCustomerId(), CarType.SEDAN,
				LocalDate.now().plusDays(1), 3);

		assertTrue(carRentalSystemService.cancelReservation(reservation.getReservationId()));
		assertEquals(ReservationStatus.CANCELLED, reservation.getStatus());
	}

	@Test
	void testGetReservationDetails() {
		Customer customer = TestDataFactory.createTestCustomer();
		customerService.addCustomer(customer);

		Car sedan = TestDataFactory.createTestSedan();
		carInventoryService.addCar(sedan);

		Reservation reservation = carRentalSystemService.makeReservation(customer.getCustomerId(), CarType.SEDAN,
				LocalDate.now().plusDays(1), 3);

		Reservation retrieved = carRentalSystemService.getReservationDetails(reservation.getReservationId());

		assertEquals(reservation.getReservationId(), retrieved.getReservationId());
		assertEquals(reservation.getCustomerId(), retrieved.getCustomerId());
	}

	@Test
	void testAddCustomerAndCar() {
		Customer customer = TestDataFactory.createTestCustomer();
		String customerId = carRentalSystemService.addCustomer(customer);

		assertEquals(customer.getCustomerId(), customerId);

		Car sedan = TestDataFactory.createTestSedan();
		carRentalSystemService.addCarToInventory(sedan);

		assertEquals(1, carInventoryService.getTotalCarsOfType(CarType.SEDAN));
	}

	@Test
	void testLimitedInventoryScenario() {
		carInventoryService.addCar(TestDataFactory.createTestSedan());
		carInventoryService.addCar(new Sedan("TEST999", "Honda", "Accord", 2023, new BigDecimal("85")));

		Customer c1 = TestDataFactory.createTestCustomer("Customer 1", "c1@example.com");
		Customer c2 = TestDataFactory.createTestCustomer("Customer 2", "c2@example.com");
		Customer c3 = TestDataFactory.createTestCustomer("Customer 3", "c3@example.com");
		customerService.addCustomer(c1);
		customerService.addCustomer(c2);
		customerService.addCustomer(c3);

		LocalDate startDate = LocalDate.now().plusDays(1);

		Reservation res1 = carRentalSystemService.makeReservation(c1.getCustomerId(), CarType.SEDAN, startDate, 3);
		Reservation res2 = carRentalSystemService.makeReservation(c2.getCustomerId(), CarType.SEDAN, startDate, 3);

		assertNotNull(res1);
		assertNotNull(res2);

		assertThrows(CarNotAvailableException.class,
				() -> carRentalSystemService.makeReservation(c3.getCustomerId(), CarType.SEDAN, startDate, 3));
	}
}