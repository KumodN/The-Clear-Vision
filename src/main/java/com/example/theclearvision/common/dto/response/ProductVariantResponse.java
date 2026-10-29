package com.example.theclearvision.common.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProductVariantResponse {
    private String id;
    private String productId;
    private String variantName;
    private String frameColor;
    private String frameSize;
    private String frameMaterial;
    private Double price;
    private Integer stockQuantity;
    private String skuSuffix;
    private Boolean isDefault;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ProductImageResponse> images = new ArrayList<>();
}
