package com.naren.customerservice.service;

import java.util.List;

import com.naren.customerservice.request.CustomerRequest;
import com.naren.customerservice.response.CustomerResponse;

public interface CustomerService {
	
	public List<CustomerResponse> getAllCustomers();
	public CustomerResponse getCustomerById(long id);
	public CustomerResponse createCustomer(CustomerRequest customerRequest);
	public CustomerResponse updateCustomerById(long id, CustomerRequest customerRequest);
	public void deleteCustomerById(long id);

}
