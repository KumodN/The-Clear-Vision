package com.example.theclearvision.common.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductCategoryResponse {
    private Long id;
    private String name;
    private String description;
    private String slug;
    private Long parentId;
    private String parentName;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}