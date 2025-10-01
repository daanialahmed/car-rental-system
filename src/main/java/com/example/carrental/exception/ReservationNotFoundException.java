package com.example.carrental.exception;

public class ReservationNotFoundException extends RuntimeException {
    public ReservationNotFoundException(String reservationId) {
        super("Reservation not found: " + reservationId);
    }
}
