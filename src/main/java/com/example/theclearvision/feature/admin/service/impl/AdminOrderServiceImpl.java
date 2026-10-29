package com.example.theclearvision.feature.admin.service.impl;

import com.example.theclearvision.feature.admin.dto.AdminOrderDetailDTO;
import com.example.theclearvision.feature.admin.service.AdminOrderService;
import com.example.theclearvision.feature.order.dto.OrderDTO;
import com.example.theclearvision.feature.order.domain.OrderEntity;
import com.example.theclearvision.feature.order.domain.OrderItem;
import com.example.theclearvision.feature.order.domain.OrderStatus;
import com.example.theclearvision.feature.order.dto.OrderItemDTO;
import com.example.theclearvision.feature.order.repository.OrderRepository;
import com.example.theclearvision.feature.order.repository.OrderSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminOrderServiceImpl implements AdminOrderService {

    private final OrderRepository orderRepository;

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getOrders(String search, String status, String paymentMethod, String sortBy, String sortDir) {
        Sort sort = Sort.unsorted();
        if (sortBy != null && !sortBy.isBlank()) {
            Sort.Direction direction = "desc".equalsIgnoreCase(sortDir)
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC;

            String sortProperty = "createdAt"; // default
            if ("total".equalsIgnoreCase(sortBy)) {
                sortProperty = "total";
            } else if ("date".equalsIgnoreCase(sortBy)) {
                sortProperty = "createdAt";
            }
            sort = Sort.by(direction, sortProperty);
        }

        Specification<OrderEntity> spec = OrderSpecification.getOrders(search, status, paymentMethod);

        return orderRepository.findAll(spec, sort).stream()
                .map(this::toOrderDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AdminOrderDetailDTO getOrderDetail(String id) {
        OrderEntity order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        AdminOrderDetailDTO dto = new AdminOrderDetailDTO();
        dto.setId(order.getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setStatus(order.getStatus());
        dto.setTotal(order.getTotal());
        dto.setCreatedAt(order.getCreatedAt());
        if (order.getUser() != null) {
            dto.setCustomerName(order.getUser().getFirstName() + " " + order.getUser().getLastName());
            dto.setCustomerEmail(order.getUser().getEmail());
        }
        if (order.getPayment() != null) {
            dto.setPaymentMethod(order.getPayment().getMethod());
            dto.setPaymentStatus(order.getPayment().getStatus());
        }
        dto.setItems(order.getItems().stream()
                .map(this::toOrderItemDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    @Override
    @Transactional
    public OrderDTO updateOrderStatus(String id, String status) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        try {
            OrderStatus newStatus = OrderStatus.valueOf(status);
            order.setStatus(newStatus);
            order = orderRepository.save(order);
            return toOrderDTO(order);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid order status: " + status);
        }
    }

    @Override
    @Transactional
    public void deleteOrder(String id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Order not found");
        }
        orderRepository.deleteById(id);
    }

    private OrderDTO toOrderDTO(OrderEntity order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setStatus(order.getStatus());
        dto.setTotal(order.getTotal());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setItems(order.getItems().stream()
                .map(this::toOrderItemDTO)
                .collect(Collectors.toList()));
        if (order.getUser() != null) {
            dto.setCustomerName(order.getUser().getFirstName() + " " + order.getUser().getLastName());
            dto.setCustomerEmail(order.getUser().getEmail());
        }
        if (order.getPayment() != null) {
            dto.setPaymentStatus(order.getPayment().getStatus());
        }
        return dto;
    }

    private OrderItemDTO toOrderItemDTO(OrderItem item) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setProductVariantId(item.getProductVariant().getId());
        if (item.getProductVariant() != null && item.getProductVariant().getProduct() != null) {
            dto.setProductName(item.getProductVariant().getProduct().getTitle());
            dto.setVariantName(item.getProductVariant().getVariantName());
        }
        dto.setQuantity(item.getQuantity());
        dto.setUnitPrice(item.getUnitPrice());
        dto.setLineTotal(item.getLineTotal());
        return dto;
    }
}
