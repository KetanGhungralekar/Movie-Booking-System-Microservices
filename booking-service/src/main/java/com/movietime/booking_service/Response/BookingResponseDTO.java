package com.movietime.booking_service.Response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class BookingResponseDTO {
    private Long id;
    private String userId;
    private Long showId;
    private String status;
    private BigDecimal totalAmount;
    private String paymentId;
    private List<SeatDTO> seats;

    @Data
    public static class SeatDTO {
        private Integer seatNumber;
        private BigDecimal price;
    }
}