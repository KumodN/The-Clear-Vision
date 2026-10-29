package com.example.theclearvision.common.dto.response;

import com.example.theclearvision.domain.Product.Gender;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProductResponse {
    private String id;
    private String sku;
    private String title;
    private String slug;
    private String description;
    private Long categoryId;
    private String categoryName;
    private Gender gender;
    private String material;
    private Double basePrice;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ProductVariantResponse> variants = new ArrayList<>();
    private String primaryImageUrl;
}
