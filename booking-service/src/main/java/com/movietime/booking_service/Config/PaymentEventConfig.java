package com.movietime.booking_service.Config;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.amqp.support.converter.MessageConverter;

@Configuration
public class PaymentEventConfig {

    public static final String PAYMENT_EXCHANGE = "payment.exchange";
    public static final String PAYMENT_QUEUE = "payment.success.queue";
    public static final String PAYMENT_ROUTING_KEY = "payment.success";

    @Bean
    public TopicExchange paymentExchange() {
        return new TopicExchange(PAYMENT_EXCHANGE);
    }

    @Bean
    public Queue paymentQueue() {
        return new Queue(PAYMENT_QUEUE, true);
    }

    @Bean
    public Binding paymentBinding(Queue paymentQueue, TopicExchange paymentExchange) {
        return BindingBuilder.bind(paymentQueue)
                .to(paymentExchange)
                .with(PAYMENT_ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter2() {
        return new Jackson2JsonMessageConverter();
    }
}