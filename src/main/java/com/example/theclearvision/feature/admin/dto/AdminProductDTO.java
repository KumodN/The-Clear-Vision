package com.example.theclearvision.feature.admin.dto;

import com.example.theclearvision.domain.Product;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminProductDTO {
    private String id;
    private String sku;
    private String title;
    private String slug;
    private String description;
    private String categoryName;
    private Product.Gender gender;
    private String material;
    private Double basePrice;
    private Boolean isActive;
    private Integer totalStock;
    private Integer variantCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
