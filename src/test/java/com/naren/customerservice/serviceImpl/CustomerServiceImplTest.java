package com.naren.customerservice.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.naren.customerservice.entity.Customer;
import com.naren.customerservice.exceptions.CustomerNotFoundException;
import com.naren.customerservice.exceptions.DuplicateCustomerException;
import com.naren.customerservice.repository.CustomerRepository;
import com.naren.customerservice.request.CustomerRequest;
import com.naren.customerservice.response.CustomerResponse;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

	@Mock
	private CustomerRepository customerRepository;

	@InjectMocks
	private CustomerServiceImpl customerService;

	private Customer customer;
	private CustomerRequest customerRequest;

	@BeforeEach
	void setUp() {
		customer = Customer.builder().id(1L).name("John Doe").email("john@example.com").phone("1234567890").build();

		customerRequest = CustomerRequest.builder().name("John Doe").email("john@example.com").phone("1234567890")
				.build();
	}

	// Test getAllCustomers() when DB is not empty
	@Test
    void getAllCustomers_ShouldReturnCustomerList() {
        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer));

        List<CustomerResponse> customers = customerService.getAllCustomers();

        assertFalse(customers.isEmpty());
        assertEquals(1, customers.size());
        assertEquals("John Doe", customers.get(0).getName());
    }

	// Test getAllCustomers() when DB is empty
	@Test
    void getAllCustomers_ShouldThrowException_WhenNoCustomersFound() {
        when(customerRepository.findAll()).thenReturn(List.of());

        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, 
            () -> customerService.getAllCustomers());

        assertEquals("Customer DB is empty", exception.getMessage());
    }

	// Test getCustomerById() when customer exists
	@Test
    void getCustomerById_ShouldReturnCustomer_WhenCustomerExists() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        CustomerResponse response = customerService.getCustomerById(1L);

        assertNotNull(response);
        assertEquals("John Doe", response.getName());
        assertEquals("john@example.com", response.getEmail());
    }

	// Test getCustomerById() when customer does not exist
	@Test
    void getCustomerById_ShouldThrowException_WhenCustomerNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, 
            () -> customerService.getCustomerById(1L));

        assertEquals("Customer with ID 1 not found", exception.getMessage());
    }

	// Test createCustomer() when email is unique
	@Test
    void createCustomer_ShouldSaveAndReturnCustomer_WhenEmailIsUnique() {
        when(customerRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerResponse response = customerService.createCustomer(customerRequest);

        assertNotNull(response);
        assertEquals("John Doe", response.getName());
        assertEquals("john@example.com", response.getEmail());
    }

	// Test createCustomer() when email already exists
	@Test
    void createCustomer_ShouldThrowException_WhenEmailAlreadyExists() {
        when(customerRepository.findByEmail("john@example.com")).thenReturn(Optional.of(customer));

        DuplicateCustomerException exception = assertThrows(DuplicateCustomerException.class, 
            () -> customerService.createCustomer(customerRequest));

        assertEquals("User with email already existjohn@example.com", exception.getMessage());
    }

	// Test updateCustomerById() when customer exists
	@Test
    void updateCustomerById_ShouldUpdateAndReturnCustomer_WhenCustomerExists() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerResponse response = customerService.updateCustomerById(1L, customerRequest);

        assertNotNull(response);
        assertEquals("John Doe", response.getName());
        assertEquals("john@example.com", response.getEmail());
    }

	// Test updateCustomerById() when customer does not exist
	@Test
    void updateCustomerById_ShouldThrowException_WhenCustomerNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, 
            () -> customerService.updateCustomerById(1L, customerRequest));

        assertEquals("Customer with ID 1 not found", exception.getMessage());
    }

	// Test deleteCustomerById() when customer exists
	@Test
    void deleteCustomerById_ShouldDeleteCustomer_WhenCustomerExists() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        doNothing().when(customerRepository).delete(customer);

        assertDoesNotThrow(() -> customerService.deleteCustomerById(1L));
        verify(customerRepository, times(1)).delete(customer);
    }

	// Test deleteCustomerById() when customer does not exist
	@Test
    void deleteCustomerById_ShouldThrowException_WhenCustomerNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, 
            () -> customerService.deleteCustomerById(1L));

        assertEquals("Customer with ID 1 not found", exception.getMessage());
    }
}
