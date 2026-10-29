package com.example.theclearvision.feature.admin.service;

import com.example.theclearvision.feature.order.dto.OrderDTO;
import com.example.theclearvision.feature.admin.dto.AdminOrderDetailDTO;
import java.util.List;

public interface AdminOrderService {
    List<OrderDTO> getOrders(String search, String status, String paymentMethod, String sortBy, String sortDir);

    AdminOrderDetailDTO getOrderDetail(String id);

    OrderDTO updateOrderStatus(String id, String status);

    void deleteOrder(String id);
}
