package com.movietime.notification_service.Service;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.movietime.notification_service.Config.RabbitMQConfig;
import com.movietime.notification_service.DTO.ShowDetailsDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingNotificationListener {

    private final EmailService emailService;
    private final RestTemplate restTemplate;

    @Value("${movie.service.url}")
    private String movieServiceUrl;

    @RabbitListener(queues = RabbitMQConfig.BOOKING_QUEUE)
    public void handleBookingNotification(Map<String, Object> message) {

        Long bookingId = ((Number) message.get("bookingId")).longValue();
        String email = (String) message.get("userEmail");
        String status = (String) message.get("status");
        Long showId = ((Number) message.get("showId")).longValue();

        System.out.println("📩 Received booking event bookingId=" + bookingId);

        // Build URL
        String url = movieServiceUrl + "/movies/shows/" + showId;
        System.out.println("Calling movie-service at URL: " + url);
        // Call movie-service (this works perfectly in Docker)
        ShowDetailsDTO showDetails = restTemplate.getForObject(url, ShowDetailsDTO.class);

        // Send full email
        emailService.sendBookingEmail(email, bookingId, status, showDetails);
    }
}
