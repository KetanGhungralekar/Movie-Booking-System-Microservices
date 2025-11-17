package com.movietime.booking_service.Event;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class BookingConfirmedEvent {
    private Long bookingId;
    private String userEmail;
    private Long showId;
    private BigDecimal totalAmount;
    private String paymentId;

    private List<SeatDTO> seats;

    @Data
    public static class SeatDTO {
        private Long seatId;       // primary key in seat-service
        private String seatNumber; // A10, B7 etc (if you load this)
        private BigDecimal price;
    }
}
