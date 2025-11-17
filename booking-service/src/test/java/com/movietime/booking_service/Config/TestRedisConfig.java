package com.movietime.booking_service.Config;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestRedisConfig {

    @Bean
    public StringRedisTemplate stringRedisTemplate() {
        return mock(StringRedisTemplate.class);
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return mock(RabbitTemplate.class);
    }
}