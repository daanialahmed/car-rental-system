package com.example.carrental.exception;

import java.time.LocalDate;

public class CarNotAvailableException extends RuntimeException {
    public CarNotAvailableException(String carType, LocalDate startDate, LocalDate endDate) {
        super(String.format("No %s available from %s to %s", carType, startDate, endDate));
    }
}
