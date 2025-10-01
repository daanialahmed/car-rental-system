package com.example.carrental.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.example.carrental.exception.CustomerNotFoundException;
import com.example.carrental.model.Customer;

@Service
public class CustomerService {
	private final Map<String, Customer> customers = new ConcurrentHashMap<>();

	public String addCustomer(Customer customer) {
		customers.put(customer.getCustomerId(), customer);
		return customer.getCustomerId();
	}

	public Customer getCustomer(String customerId) {
		Customer customer = customers.get(customerId);
		if (customer == null) {
			throw new CustomerNotFoundException(customerId);
		}
		return customer;
	}

	public List<Customer> getAllCustomers() {
		return new ArrayList<>(customers.values());
	}
}
