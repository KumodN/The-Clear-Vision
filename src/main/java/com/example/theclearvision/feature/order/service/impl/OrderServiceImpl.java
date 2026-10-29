package com.example.theclearvision.feature.order.service.impl;

import com.example.theclearvision.common.exception.ResourceNotFoundException;
import com.example.theclearvision.domain.Address;
import com.example.theclearvision.domain.ProductVariant;
import com.example.theclearvision.domain.User;
import com.example.theclearvision.feature.auth.repository.AddressRepository;
import com.example.theclearvision.feature.auth.repository.UserRepository;
import com.example.theclearvision.feature.order.domain.OrderEntity;
import com.example.theclearvision.feature.order.domain.OrderItem;
import com.example.theclearvision.feature.order.domain.OrderStatus;
import com.example.theclearvision.feature.order.dto.OrderRequest;
import com.example.theclearvision.feature.order.dto.OrderDTO;
import com.example.theclearvision.feature.order.dto.OrderItemRequest;
import com.example.theclearvision.feature.order.mapper.OrderMapper;
import com.example.theclearvision.feature.order.repository.OrderRepository;
import com.example.theclearvision.feature.order.service.OrderService;
import com.example.theclearvision.feature.payment.domain.Payment;
import com.example.theclearvision.feature.payment.service.PaymentService;
import com.example.theclearvision.feature.product.domain.Lens;
import com.example.theclearvision.feature.product.repository.LensRepository;
import com.example.theclearvision.persistence.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final ProductVariantRepository productVariantRepository;
    private final LensRepository lensRepository;
    private final PaymentService paymentService;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderDTO createOrder(Long userId, OrderRequest request) {
        log.info("Creating order for user: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Address shippingAddress = addressRepository.findById(request.getShippingAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Shipping address not found"));

        Address billingAddress = addressRepository.findById(request.getBillingAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Billing address not found"));

        OrderEntity order = OrderEntity.builder()
                .orderNumber("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .user(user)
                .shippingAddress(shippingAddress)
                .billingAddress(billingAddress)
                .status(OrderStatus.PENDING)
                .items(new ArrayList<>())
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.getItems()) {
            ProductVariant variant = productVariantRepository.findById(itemRequest.getProductVariantId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Product variant not found: " + itemRequest.getProductVariantId()));

            // Basic stock check
            if (variant.getStockQuantity() < itemRequest.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + variant.getVariantName());
            }

            // Decrement stock
            variant.setStockQuantity(variant.getStockQuantity() - itemRequest.getQuantity());
            productVariantRepository.save(variant);

            BigDecimal unitPrice = BigDecimal.valueOf(variant.getPrice());
            Lens lens = null;

            if (itemRequest.getLensId() != null) {
                lens = lensRepository.findById(itemRequest.getLensId())
                        .orElseThrow(() -> new ResourceNotFoundException("Lens not found: " + itemRequest.getLensId()));
                unitPrice = unitPrice.add(lens.getPriceModifier());
            }

            BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            total = total.add(lineTotal);

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .productVariant(variant)
                    .lens(lens)
                    .quantity(itemRequest.getQuantity())
                    .unitPrice(unitPrice)
                    .lineTotal(lineTotal)
                    .build();

            order.getItems().add(orderItem);
        }

        order.setTotal(total);

        // Process Payment
        Payment payment = paymentService.processPayment(total, request.getPaymentMethod(), order.getOrderNumber());
        order.setPayment(payment);

        if (payment.getStatus() == com.example.theclearvision.feature.payment.domain.PaymentStatus.PAID) {
            order.setStatus(OrderStatus.PAID);
        }

        OrderEntity savedOrder = orderRepository.save(order);
        log.info("Order created successfully: {}", savedOrder.getOrderNumber());

        return orderMapper.toDTO(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getUserOrders(Long userId) {
        return orderMapper.toDTOList(orderRepository.findByUserId(userId));
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrder(String id) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        return orderMapper.toDTO(order);
    }
}
