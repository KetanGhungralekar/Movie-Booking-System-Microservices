package com.movietime.booking_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.movietime.booking_service.Config.TestRedisConfig;

@SpringBootTest
@ActiveProfiles("test")
@Import(TestRedisConfig.class)
class BookingServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
