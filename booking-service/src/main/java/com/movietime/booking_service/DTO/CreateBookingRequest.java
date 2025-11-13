package com.movietime.booking_service.DTO;
import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class CreateBookingRequest {
    private Long showId;
    private BigDecimal totalAmount;
    private List<SeatSelection> seats;
}