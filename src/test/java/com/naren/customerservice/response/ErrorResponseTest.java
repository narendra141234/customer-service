package com.naren.customerservice.response;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void testErrorResponseGettersAndSetters() {
        LocalDateTime timestamp = LocalDateTime.now();
        int status = 404;
        String error = "Not Found";
        String message = "Customer not found";
        String path = "/customer/123";

        // Create an instance using constructor
        ErrorResponse errorResponse = new ErrorResponse(timestamp, status, error, message, path);

        // Validate constructor values
        assertEquals(timestamp, errorResponse.getTimestamp());
        assertEquals(status, errorResponse.getStatus());
        assertEquals(error, errorResponse.getError());
        assertEquals(message, errorResponse.getMessage());
        assertEquals(path, errorResponse.getPath());

        // Test setters
        LocalDateTime newTimestamp = LocalDateTime.now();
        errorResponse.setTimestamp(newTimestamp);
        errorResponse.setStatus(500);
        errorResponse.setError("Internal Server Error");
        errorResponse.setMessage("Unexpected error occurred");
        errorResponse.setPath("/error");

        // Validate new values
        assertEquals(newTimestamp, errorResponse.getTimestamp());
        assertEquals(500, errorResponse.getStatus());
        assertEquals("Internal Server Error", errorResponse.getError());
        assertEquals("Unexpected error occurred", errorResponse.getMessage());
        assertEquals("/error", errorResponse.getPath());
    }
}
