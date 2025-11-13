package com.movietime.booking_service.DTO;
import java.math.BigDecimal;
import com.movietime.booking_service.Model.SeatType;
import lombok.Data;

@Data
public class SeatSelection {
    private Long seatId;
    private String rowLabel;
    private Integer seatNumber;
    private SeatType seatType;
    private BigDecimal price;
}
