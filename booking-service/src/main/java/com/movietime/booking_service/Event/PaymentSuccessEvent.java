package com.movietime.booking_service.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentSuccessEvent {
    private Long bookingId;
    private String paymentId;
}