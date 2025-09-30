package com.example.carrental.model;

import java.math.BigDecimal;

public class SUV extends Car {

	public SUV(String licensePlate, String make, String model, int year, BigDecimal dailyRate) {
		super(CarType.SUV, licensePlate, make, model, year, dailyRate);
	}

}
