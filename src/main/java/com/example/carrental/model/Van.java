package com.example.carrental.model;

import java.math.BigDecimal;

public class Van extends Car {
	
	public Van(String licensePlate, String make, String model, int year, BigDecimal dailyRate) {
		super(CarType.VAN, licensePlate, make, model, year, dailyRate);
	}
	
}
