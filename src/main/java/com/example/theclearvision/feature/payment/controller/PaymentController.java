package com.example.theclearvision.feature.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook() {
        // STUB: Handle payment gateway webhooks
        return ResponseEntity.ok().build();
    }
}
