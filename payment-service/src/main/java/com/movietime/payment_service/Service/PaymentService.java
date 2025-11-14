package com.movietime.payment_service.Service;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.movietime.payment_service.Event.PaymentSuccessEvent;
import com.movietime.payment_service.Request.PaymentRequest;

@Service
public class PaymentService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void processPayment(PaymentRequest request) {

        System.out.println("Payment Started for Booking ID: " + request.getBookingId());

        // Simulate payment gateway delay
        try {
            Thread.sleep(2000); // 2-second delay
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Prepare event
        PaymentSuccessEvent event = new PaymentSuccessEvent(
                request.getBookingId(),
                "SUCCESS"
        );
        System.out.println("Payment Success Event Created for Payment ID " + event.getPaymentId());
        // Publish event
        rabbitTemplate.convertAndSend(
                "payment.exchange",
                "payment.success",
                event
        );

        System.out.println("Payment Success Event Published → " + event.getPaymentId() + " for Booking ID: " + request.getBookingId());
    }
}
