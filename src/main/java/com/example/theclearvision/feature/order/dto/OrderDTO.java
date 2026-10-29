package com.example.theclearvision.feature.order.dto;

import com.example.theclearvision.feature.order.domain.OrderStatus;
import com.example.theclearvision.feature.payment.domain.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {
    private String id;
    private String orderNumber;
    private OrderStatus status;
    private BigDecimal total;
    private LocalDateTime createdAt;
    private List<OrderItemDTO> items;
    private PaymentStatus paymentStatus;
    private String customerName;
    private String customerEmail;
}
