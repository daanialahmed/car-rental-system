package com.example.carrental.factory;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.example.carrental.model.Car;
import com.example.carrental.model.CarType;
import com.example.carrental.model.SUV;
import com.example.carrental.model.Sedan;
import com.example.carrental.model.Van;

@Component
public class CarFactory {

	public Car createCar(CarType carType, String licensePlate, String make, String model, int year,
			BigDecimal dailyRate) {
		return switch (carType) {
		case SEDAN -> new Sedan(licensePlate, make, model, year, dailyRate);
		case SUV -> new SUV(licensePlate, make, model, year, dailyRate);
		case VAN -> new Van(licensePlate, make, model, year, dailyRate);
		};
	}
}
