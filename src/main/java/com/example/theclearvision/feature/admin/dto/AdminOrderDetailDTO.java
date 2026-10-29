package com.example.theclearvision.feature.admin.dto;

import com.example.theclearvision.feature.order.domain.OrderStatus;
import com.example.theclearvision.feature.order.dto.OrderItemDTO;
import com.example.theclearvision.feature.payment.domain.PaymentMethod;
import com.example.theclearvision.feature.payment.domain.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AdminOrderDetailDTO {
    private String id;
    private String orderNumber;
    private OrderStatus status;
    private BigDecimal total;
    private LocalDateTime createdAt;
    private String customerName;
    private String customerEmail;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private List<OrderItemDTO> items;
}
