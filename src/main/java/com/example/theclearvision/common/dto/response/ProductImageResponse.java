package com.example.theclearvision.common.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductImageResponse {
    private Long id;
    private String productVariantId;
    private String productId;
    private String imageUrl;
    private String altText;
    private Integer displayOrder;
    private Boolean isPrimary;
    private LocalDateTime createdAt;
}
