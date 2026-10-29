package com.example.theclearvision.feature.order.dto;

import com.example.theclearvision.feature.payment.domain.PaymentMethod;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {
    private Long shippingAddressId;
    private Long billingAddressId;
    private PaymentMethod paymentMethod;
    private List<OrderItemRequest> items;
}
