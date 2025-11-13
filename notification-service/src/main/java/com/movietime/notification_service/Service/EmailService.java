package com.movietime.notification_service.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendBookingEmail(String to, Long bookingId, String status) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Your Booking #" + bookingId + " - " + status);
        message.setText("Dear user,\n\nYour booking #" + bookingId + " is now " + status + ".\n\nThank you!");
        mailSender.send(message);

        System.out.println("Email sent to: " + to);
    }
}