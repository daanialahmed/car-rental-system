package com.example.carrental.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class CarTest {

    @Test
    void testCarCreation() {
        Sedan sedan = new Sedan("ABC123", "Toyota", "Camry", 2023, BigDecimal.valueOf(45.00));
        
        assertNotNull(sedan.getId());
        assertEquals(CarType.SEDAN, sedan.getType());
        assertEquals("ABC123", sedan.getLicensePlate());
        assertEquals("Toyota", sedan.getMake());
        assertEquals("Camry", sedan.getModel());
        assertEquals(2023, sedan.getYear());
        assertEquals(BigDecimal.valueOf(45.00), sedan.getDailyRate());
    }

    @Test
    void testCarTypePolymorphism() {
        Car sedan = new Sedan("ABC123", "Toyota", "Camry", 2023, BigDecimal.valueOf(45.00));
        Car suv = new SUV("DEF456", "Honda", "CR-V", 2023, BigDecimal.valueOf(65.00));
        Car van = new Van("GHI789", "Ford", "Transit", 2023, BigDecimal.valueOf(80.00));

        assertEquals(CarType.SEDAN, sedan.getType());
        assertEquals(CarType.SUV, suv.getType());
        assertEquals(CarType.VAN, van.getType());
    }

    @Test
    void testCarEquality() {
        Sedan sedan1 = new Sedan("ABC123", "Toyota", "Camry", 2023, BigDecimal.valueOf(45.00));
        Sedan sedan2 = new Sedan("DEF456", "Honda", "Accord", 2023, BigDecimal.valueOf(50.00));

        assertNotEquals(sedan1, sedan2);
        assertEquals(sedan1, sedan1);
    }
}