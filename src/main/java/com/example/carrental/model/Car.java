package com.example.carrental.model;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class Car {

    @EqualsAndHashCode.Include
    private final String id;
    private final CarType type;
    private final String licensePlate;
    private final String make;
    private final String model;
    private final int year;
    private final BigDecimal dailyRate;

    public Car(CarType type, String licensePlate, String make, String model, int year, BigDecimal dailyRate) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.licensePlate = licensePlate;
        this.make = make;
        this.model = model;
        this.year = year;
        this.dailyRate = dailyRate;
    }
}
