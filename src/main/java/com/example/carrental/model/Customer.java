package com.example.carrental.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Customer {

	@EqualsAndHashCode.Include
	private final String customerId;
	private final String name;
	private final String email;
	private final String phone;
	private final String licenseNumber;
	private final LocalDateTime createdAt;

	public Customer(String name, String email, String phone, String licenseNumber) {
		this.customerId = UUID.randomUUID().toString();
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.licenseNumber = licenseNumber;
		this.createdAt = LocalDateTime.now();
	}

	public boolean validateLicense() {
		return licenseNumber != null && licenseNumber.matches("^[A-Z0-9]{6,12}$");
	}

}
