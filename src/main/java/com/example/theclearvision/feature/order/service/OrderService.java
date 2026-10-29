package com.example.theclearvision.feature.order.service;

import com.example.theclearvision.feature.order.dto.OrderRequest;
import com.example.theclearvision.feature.order.dto.OrderDTO;

import java.util.List;

public interface OrderService {
    OrderDTO createOrder(Long userId, OrderRequest request);

    List<OrderDTO> getUserOrders(Long userId);

    OrderDTO getOrder(String id);
}
