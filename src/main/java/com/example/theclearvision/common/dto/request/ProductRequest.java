package com.example.theclearvision.common.dto.request;

import com.example.theclearvision.domain.Product.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductRequest {

    @NotBlank(message = "SKU is required")
    @Size(max = 100, message = "SKU must not exceed 100 characters")
    private String sku;

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @NotBlank(message = "Slug is required")
    @Size(max = 255, message = "Slug must not exceed 255 characters")
    private String slug;

    private String description;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    private Gender gender = Gender.UNISEX;

    @Size(max = 100, message = "Material must not exceed 100 characters")
    private String material;

    @NotNull(message = "Base price is required")
    @Positive(message = "Base price must be positive")
    private Double basePrice;

    private Boolean isActive = true;

    private List<ProductVariantRequest> variants = new ArrayList<>();
}
