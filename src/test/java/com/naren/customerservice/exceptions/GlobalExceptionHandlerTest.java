package com.naren.customerservice.exceptions;

import com.naren.customerservice.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
        request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/customer/123");
    }

    @Test
    void testHandleCustomerNotFoundException() {
        CustomerNotFoundException ex = new CustomerNotFoundException("Customer with ID 123 not found");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleCustomerNotFoundException(ex, request);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.NOT_FOUND.value(), errorResponse.getStatus());
        assertEquals("Not Found", errorResponse.getError());  // Dynamic message from HttpStatus
        assertEquals("Customer with ID 123 not found", errorResponse.getMessage());
        assertEquals("/customer/123", errorResponse.getPath());
    }

    @Test
    void testHandleDuplicateCustomerException() {
        DuplicateCustomerException ex = new DuplicateCustomerException("Customer already exists");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleDuplicateCustomerException(ex, request);

        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());

        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.CONFLICT.value(), errorResponse.getStatus());
        assertEquals("Conflict", errorResponse.getError());  // Dynamic message from HttpStatus
        assertEquals("Customer already exists", errorResponse.getMessage());
        assertEquals("/customer/123", errorResponse.getPath());
    }

    @Test
    void testHandleGlobalException() {
        Exception ex = new Exception("Unexpected server error");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGlobalException(ex, request);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse.getStatus());
        assertEquals("Internal Server Error", errorResponse.getError());  // Dynamic message from HttpStatus
        assertEquals("Unexpected server error", errorResponse.getMessage());
        assertEquals("/customer/123", errorResponse.getPath());
    }
}
