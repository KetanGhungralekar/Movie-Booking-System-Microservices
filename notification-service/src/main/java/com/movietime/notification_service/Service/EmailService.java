package com.movietime.notification_service.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.movietime.notification_service.DTO.ShowDetailsDTO;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendBookingEmail(String to, Long bookingId, String status, ShowDetailsDTO showDetails) {
        String movieName = showDetails.getMovie().getTitle();
        String genre = showDetails.getMovie().getGenre();
        String duration = showDetails.getMovie().getDuration();

        String startTime = showDetails.getStartTime().toString();
        String endTime = showDetails.getEndTime().toString();
        String auditorium = showDetails.getAuditorium();

        String subject = "🎬 Booking #" + bookingId + " - " + movieName + " (" + status + ")";

        StringBuilder body = new StringBuilder();
        body.append("Hello,\n\n");
        body.append("Your booking has been ").append(status).append(".\n\n");

        body.append("📌 Booking Details:\n");
        body.append("• Booking ID: ").append(bookingId).append("\n");
        body.append("• Status: ").append(status).append("\n\n");

        body.append("🎬 Movie Details:\n");
        body.append("• Title: ").append(movieName).append("\n");
        body.append("• Genre: ").append(genre).append("\n");
        body.append("• Duration: ").append(duration).append(" minutes\n\n");

        body.append("🎭 Show Details:\n");
        body.append("• Start Time: ").append(startTime).append("\n");
        body.append("• End Time: ").append(endTime).append("\n");
        body.append("• Auditorium: ").append(auditorium).append("\n\n");

        body.append("Thank you for using MovieTime! Enjoy your show 🍿\n");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("kghungralekar1234@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body.toString());

        mailSender.send(message);

        System.out.println("📨 Booking email sent to: " + to);
    }

}