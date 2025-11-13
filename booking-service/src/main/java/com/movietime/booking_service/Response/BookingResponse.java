package com.movietime.booking_service.Response;
import com.movietime.booking_service.Model.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponse {
    private Long id;
    private Long showId;
    private String userId;
    private BookingStatus status;
    private BigDecimal totalAmount;
    private String currency;
    private String paymentId;
    private String lockId;
    private Instant createdAt;
    private Instant updatedAt;
}
