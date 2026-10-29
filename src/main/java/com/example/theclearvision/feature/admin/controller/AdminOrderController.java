package com.example.theclearvision.feature.admin.controller;

import com.example.theclearvision.feature.admin.dto.AdminOrderDetailDTO;
import com.example.theclearvision.feature.admin.service.AdminOrderService;
import com.example.theclearvision.feature.order.dto.OrderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminOrderController {

    private final AdminOrderService adminOrderService;

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String paymentMethod,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir) {
        List<OrderDTO> orders = adminOrderService.getOrders(search, status, paymentMethod, sortBy, sortDir);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminOrderDetailDTO> getOrderDetail(@PathVariable String id) {
        AdminOrderDetailDTO detail = adminOrderService.getOrderDetail(id);
        return ResponseEntity.ok(detail);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrderDTO> updateStatus(
            @PathVariable String id,
            @RequestBody java.util.Map<String, String> payload) {
        String status = payload.get("status");
        OrderDTO updatedOrder = adminOrderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable String id) {
        adminOrderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
