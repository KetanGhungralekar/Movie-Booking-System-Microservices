package com.movietime.notification_service.Service;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.movietime.notification_service.Config.RabbitMQConfig;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingNotificationListener {

    private final EmailService emailService;

    @RabbitListener(queues = RabbitMQConfig.BOOKING_QUEUE)
    public void handleBookingNotification(Map<String, Object> message) {
        // Cast to String is usually safe if the sender sends strings
        String email = (String) message.get("userEmail"); 
        String status = (String) message.get("status");

        // Use Number type casting for robustness, then get the long value.
        // This is safer as Jackson often reads whole numbers as Integer by default.
        Long bookingId = ((Number) message.get("bookingId")).longValue(); 

        System.out.println("📩 Received booking event for bookingId=" + bookingId);

        // Assuming emailService is correctly injected
        emailService.sendBookingEmail(email, bookingId, status); 
    }
}