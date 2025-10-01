# Car Rental System

A simple car rental management system built with Java 17 and Spring Boot.

## Project Structure

```
src/main/java/com/example/carrental/
├── CarRentalSystemApplication.java    # Spring Boot main class
├── model/                              # Domain models
│   ├── CarType.java                   # Enum: SEDAN, SUV, VAN
│   ├── ReservationStatus.java         # Enum: PENDING, ACTIVE, CANCELLED
│   ├── Car.java                       # Abstract car class
│   ├── Sedan.java                     # Sedan implementation
│   ├── SUV.java                       # SUV implementation
│   ├── Van.java                       # Van implementation
│   ├── Customer.java                  # Customer entity
│   └── Reservation.java               # Reservation entity
├── service/                           # Business logic layer
│   ├── CarInventoryService.java      # Manages car inventory
│   ├── CustomerService.java          # Manages customers
│   ├── ReservationService.java       # Manages reservations
│   └── CarRentalSystemService.java   # Main service (Facade)
├── factory/                           # Factory pattern
│   └── CarFactory.java               # Creates different car types
├── exception/                         # Custom exceptions
│   ├── CarNotAvailableException.java
│   ├── CustomerNotFoundException.java
│   └── ReservationNotFoundException.java
└── config/                            # Configuration
    └── DataInitializer.java          # Sample data loader

src/test/java/com/example/carrental/
├── model/
│   ├── CarTest.java
│   └── ReservationTest.java
├── service/
│   ├── CarInventoryServiceTest.java
│   ├── ReservationServiceTest.java
│   └── CarRentalSystemServiceTest.java
└── util/
    └── TestDataFactory.java          # Helper for test data
```

## Architecture

### Layers
1. **Model Layer** - Domain entities (Car, Customer, Reservation)
2. **Service Layer** - Business logic and orchestration
3. **Factory Layer** - Object creation patterns
4. **Exception Layer** - Custom error handling

### Design Patterns
- **Facade Pattern**: CarRentalSystemService provides a simple interface to complex subsystems
- **Factory Pattern**: CarFactory creates different car types (Sedan, SUV, Van)
- **Repository Pattern**: Services use in-memory storage (ConcurrentHashMap)
- **Template Method**: Car abstract class defines structure for subclasses

## Core Entities

### Car (Abstract)
- Base class for all vehicles
- Fields: id, type, licensePlate, make, model, year, dailyRate
- Subclasses: Sedan, SUV, Van

### Customer
- Represents rental customers
- Fields: customerId, name, email, phone, licenseNumber
- Business method: validateLicense()

### Reservation
- Represents a car booking
- Fields: reservationId, customerId, carId, startDate, endDate, totalCost, status
- Core method: isOverlapping() - prevents double-booking

## Key Features

1. **Car Inventory Management**
   - Support for 3 car types: Sedan, SUV, Van
   - Limited inventory per type
   - Track available cars

2. **Reservation System**
   - Date-based bookings
   - Overlap detection algorithm
   - Status management (PENDING → ACTIVE → CANCELLED)

3. **Customer Management**
   - Customer registration
   - License validation

## Business Rules

- A car can only be reserved if available for the entire requested period
- Reservations are checked for overlaps: no double-booking allowed
- Status transitions: PENDING (new) → ACTIVE (confirmed) → CANCELLED (if cancelled)
- Dates are end-exclusive: a car ending on Jan 10 can start a new reservation on Jan 10

## Overlap Detection Algorithm

```java
public boolean isOverlapping(LocalDate requestStart, LocalDate requestEnd) {
    // No overlap if request is completely before OR completely after existing reservation
    return !(requestStart.isAfter(endDate) || 
             requestEnd.isBefore(startDate) || 
             requestEnd.equals(startDate));
}
```

## Technology Stack

- Java 17
- Spring Boot 3.2.0
- Lombok (reduce boilerplate)
- JUnit 5 (testing)
- Maven (build tool)

## How to Run

```bash
# Build the project
mvn clean compile

# Run tests
mvn test

# Run the application
mvn spring-boot:run
```

## Sample Data

On startup, the system initializes with:
- 6 cars: 2 Sedans, 2 SUVs, 2 Vans
- 2 customers with valid licenses

## Testing

Run all tests:
```bash
mvn test
```

Test coverage includes:
- Model validation and behavior
- Service layer business logic
- Integration scenarios (overlapping bookings, limited inventory)
- Edge cases (non-existent customers, no availability)