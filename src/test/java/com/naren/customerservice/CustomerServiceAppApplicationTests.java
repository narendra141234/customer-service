package com.naren.customerservice;


import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.*;
@SpringBootTest
class CustomerServiceAppApplicationTests {

    @Test
    void mainMethodTest() {
        // Mock SpringApplication.run() to ensure main method is covered
        try (var mock = mockStatic(SpringApplication.class)) {
            CustomerServiceAppApplication.main(new String[]{});
            mock.verify(() -> SpringApplication.run(CustomerServiceAppApplication.class, new String[]{}));
        }
    }
}
