package com.movietime.booking_service.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.movietime.booking_service.DTO.CreateBookingRequest;
import com.movietime.booking_service.Model.Booking;
import com.movietime.booking_service.Response.BookingResponse;
import com.movietime.booking_service.Response.BookingResponseDTO;
import com.movietime.booking_service.Response.SeatStatusResponse;
import com.movietime.booking_service.Service.BookingService;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;
    @PostMapping("/create")
    public ResponseEntity<BookingResponse> createBooking(@RequestHeader("X-User-Email") String userEmail,@RequestBody CreateBookingRequest req) {
        BookingResponse booking = bookingService.createBooking(req, userEmail);
        return ResponseEntity.ok(booking);
    }
    @PostMapping("/confirm/{bookingId}")
    public ResponseEntity<BookingResponse> confirmBooking(
            @PathVariable Long bookingId,
            @RequestParam String paymentId) {
        return ResponseEntity.ok(bookingService.confirmPayment(bookingId, paymentId));
    }
    @GetMapping("/show/{showId}/seats/status")
    public SeatStatusResponse getSeatStatus(@PathVariable Long showId) {
        return bookingService.getSeatStatus(showId);
    }
    @GetMapping("/my")
    public ResponseEntity<List<BookingResponseDTO>> getBookingsByUserId(
            @RequestHeader("X-User-Email") String userEmail) {
        List<BookingResponseDTO> bookings = bookingService.getBookingsByUserId(userEmail);
        return ResponseEntity.ok(bookings);
    }
}
