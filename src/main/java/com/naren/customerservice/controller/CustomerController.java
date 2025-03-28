package com.naren.customerservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naren.customerservice.request.CustomerRequest;
import com.naren.customerservice.response.CustomerResponse;
import com.naren.customerservice.service.CustomerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Validated
@CrossOrigin(origins = "http://localhost:3000")
public class CustomerController {

	private final CustomerService customerService;

	@GetMapping
	public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
		List<CustomerResponse> customers = customerService.getAllCustomers();
		return ResponseEntity.ok(customers);
	}

	@GetMapping("/{id}")
	public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable("id") long id) {
		CustomerResponse customerResponse = customerService.getCustomerById(id);
		return ResponseEntity.ok(customerResponse);
	}

	@PostMapping
	public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CustomerRequest customerRequest) {
		CustomerResponse customer = customerService.createCustomer(customerRequest);
		return ResponseEntity.status(201).body(customer);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteCustomer(@PathVariable("id") long id) {
		customerService.deleteCustomerById(id);
		return ResponseEntity.noContent().build();
	}

}
