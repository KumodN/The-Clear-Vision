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
public class ProductStatsDTO {
    private Long totalProducts;
    private Long totalVariants;
    private Long activeProducts;
    private Long lowStockProducts;
    private Long outOfStockProducts;
    private List<CategoryCount> productsByCategory;
    private BigDecimal averagePrice;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryCount {
        private String category;
        private Long count;
    }
}
