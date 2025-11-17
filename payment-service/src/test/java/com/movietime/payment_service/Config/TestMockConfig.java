package com.movietime.payment_service.Config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestMockConfig {

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return mock(RabbitTemplate.class);
    }
}
