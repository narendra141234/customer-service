package com.naren.customerservice.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.naren.customerservice.entity.Customer;
import com.naren.customerservice.exceptions.CustomerNotFoundException;
import com.naren.customerservice.exceptions.DuplicateCustomerException;
import com.naren.customerservice.repository.CustomerRepository;
import com.naren.customerservice.request.CustomerRequest;
import com.naren.customerservice.response.CustomerResponse;
import com.naren.customerservice.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

	private final CustomerRepository customerRepository;

	public CustomerServiceImpl(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	@Override
	public List<CustomerResponse> getAllCustomers() {

		List<Customer> customers = customerRepository.findAll();

		if (customers.isEmpty()) {
			throw new CustomerNotFoundException("Customer DB is empty");
		}

		return customers.stream().map(customer -> new CustomerResponse(customer.getId(), customer.getName(),
				customer.getEmail(), customer.getPhone())).collect(Collectors.toList());
	}

	@Override
	public CustomerResponse getCustomerById(long id) {
		// Fetch customer from the database
		Customer customer = customerRepository.findById(id)
				.orElseThrow(() -> new CustomerNotFoundException("Customer with ID " + id + " not found"));

		// Convert Customer entity to CustomerResponse DTO using Builder
		return CustomerResponse.builder().id(customer.getId()).name(customer.getName()).email(customer.getEmail())
				.phone(customer.getPhone()).build();
	}

	@Override
	public CustomerResponse createCustomer(CustomerRequest customerRequest) {
		if (customerRepository.findByEmail(customerRequest.getEmail()).isPresent()) {
			throw new DuplicateCustomerException("User with email already exist" + customerRequest.getEmail());
		}
		Customer customer = Customer.builder().name(customerRequest.getName()).email(customerRequest.getEmail())
				.phone(customerRequest.getPhone()).build();
		customer = customerRepository.save(customer);

		return CustomerResponse.builder().id(customer.getId()).name(customer.getName()).email(customer.getEmail())
				.phone(customer.getPhone()).build();

	}

	@Override
	public CustomerResponse updateCustomerById(long id, CustomerRequest customerRequest) {
		Customer customer = customerRepository.findById(id)
				.orElseThrow(() -> new CustomerNotFoundException("Customer with ID " + id + " not found"));

		customer.setName(customerRequest.getName());
		customer.setEmail(customerRequest.getEmail());
		customer.setPhone(customerRequest.getPhone());

		Customer updatedCustomer = customerRepository.save(customer);

		return new CustomerResponse(updatedCustomer.getId(), updatedCustomer.getName(), updatedCustomer.getEmail(),
				updatedCustomer.getPhone());
	}

	@Override
	public void deleteCustomerById(long id) {
		// Check if customer exists
		Customer customer = customerRepository.findById(id)
				.orElseThrow(() -> new CustomerNotFoundException("Customer with ID " + id + " not found"));

		// Delete the customer
		customerRepository.delete(customer);
	}

}
