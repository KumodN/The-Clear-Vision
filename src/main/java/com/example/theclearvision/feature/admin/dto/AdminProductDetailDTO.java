package com.example.theclearvision.feature.admin.dto;

import com.example.theclearvision.common.dto.response.ProductVariantResponse;
import com.example.theclearvision.common.dto.response.ProductImageResponse;
import com.example.theclearvision.domain.Product;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AdminProductDetailDTO {
    private String id;
    private String sku;
    private String title;
    private String slug;
    private String description;
    private String categoryId;
    private String categoryName;
    private Product.Gender gender;
    private String material;
    private Double basePrice;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ProductVariantResponse> variants;
    private List<ProductImageResponse> images;
}
