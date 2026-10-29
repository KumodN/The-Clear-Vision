package com.example.theclearvision.feature.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerStatsDTO {
    private Long totalCustomers;
    private Long newCustomersThisMonth;
    private Long activeCustomers;
    private Long customersWithOrders;
    private Double averageOrdersPerCustomer;
    private List<TopCustomer> topCustomers;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopCustomer {
        private Long userId;
        private String name;
        private Long totalOrders;
        private BigDecimal totalSpent;
    }
}
