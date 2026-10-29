package com.example.theclearvision.feature.payment.service;

import com.example.theclearvision.feature.payment.domain.Payment;
import com.example.theclearvision.feature.payment.domain.PaymentMethod;

import java.math.BigDecimal;

public interface PaymentService {
    Payment processPayment(BigDecimal amount, PaymentMethod method, String orderId);
}
