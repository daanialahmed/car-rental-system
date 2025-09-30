package com.example.carrental.model;

import java.math.BigDecimal;

public class Sedan extends Car {
	
	public Sedan(String licensePlate, String make, String model, int year, BigDecimal dailyRate) {
		super(CarType.SEDAN, licensePlate, make, model, year, dailyRate);
	}
	
}
