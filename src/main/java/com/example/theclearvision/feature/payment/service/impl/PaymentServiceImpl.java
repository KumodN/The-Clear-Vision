package com.example.theclearvision.feature.payment.service.impl;

import com.example.theclearvision.feature.payment.domain.Payment;
import com.example.theclearvision.feature.payment.domain.PaymentMethod;
import com.example.theclearvision.feature.payment.domain.PaymentProvider;
import com.example.theclearvision.feature.payment.domain.PaymentStatus;
import com.example.theclearvision.feature.payment.repository.PaymentRepository;
import com.example.theclearvision.feature.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public Payment processPayment(BigDecimal amount, PaymentMethod method, String orderId) {
        log.info("Processing payment for order: {}, amount: {}, method: {}", orderId, amount, method);

        // STUB: Simulate payment processing
        // In a real app, this would call Stripe/PayHere API

        PaymentProvider provider = PaymentProvider.MANUAL;
        PaymentStatus status = PaymentStatus.PENDING;
        String transactionId = UUID.randomUUID().toString();

        switch (method) {
            case CARD:
                // Simulate Stripe payment
                provider = PaymentProvider.STRIPE;
                // In a real scenario, we would call Stripe API here.
                // For now, we assume success.
                status = PaymentStatus.PAID;
                break;
            case COD:
                provider = PaymentProvider.MANUAL;
                status = PaymentStatus.PENDING;
                break;
            case BANK_TRANSFER:
                provider = PaymentProvider.MANUAL;
                status = PaymentStatus.PENDING;
                break;
        }

        Payment payment = Payment.builder()
                .amount(amount)
                .method(method)
                .provider(provider)
                .providerPaymentId(transactionId)
                .status(status)
                .build();

        return paymentRepository.save(payment);
    }
}
