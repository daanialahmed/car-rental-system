package com.example.carrental.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.example.carrental.model.Car;
import com.example.carrental.model.CarType;

@Service
public class CarInventoryService {
	private final Map<CarType, List<Car>> cars = new ConcurrentHashMap<>();

	public CarInventoryService() {
		for (CarType type : CarType.values()) {
			cars.put(type, new ArrayList<>());
		}
	}

	public void addCar(Car car) {
		cars.get(car.getType()).add(car);
	}

	public List<Car> getCarsByType(CarType carType) {
		return new ArrayList<>(cars.get(carType));
	}

	public Optional<Car> findCarById(String carId) {
		return cars.values().stream().flatMap(List::stream).filter(car -> car.getId().equals(carId)).findFirst();
	}

	public int getTotalCarsOfType(CarType carType) {
		return cars.get(carType).size();
	}
}