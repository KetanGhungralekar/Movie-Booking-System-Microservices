package com.movietime.payment_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.movietime.payment_service.Config.TestMockConfig;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestMockConfig.class)
class PaymentServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
