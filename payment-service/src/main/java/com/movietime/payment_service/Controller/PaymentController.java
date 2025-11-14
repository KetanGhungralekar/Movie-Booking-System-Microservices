package com.movietime.payment_service.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movietime.payment_service.Request.PaymentRequest;
import com.movietime.payment_service.Service.PaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/pay")
    public ResponseEntity<String> makePayment(@RequestBody PaymentRequest request) {
        paymentService.processPayment(request);
        return ResponseEntity.ok("Payment started");
    }
}
