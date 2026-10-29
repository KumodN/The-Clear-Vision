package com.example.theclearvision.common.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class ProductVariantRequest {

    @NotBlank(message = "Variant name is required")
    private String variantName;

    @NotBlank(message = "Frame color is required")
    private String frameColor;

    @NotBlank(message = "Frame size is required")
    private String frameSize;

    private String frameMaterial;

    @NotNull(message = "Price is required")
    @PositiveOrZero(message = "Price must be positive or zero")
    private Double price;

    @NotNull(message = "Stock quantity is required")
    @PositiveOrZero(message = "Stock quantity must be positive or zero")
    private Integer stockQuantity = 0;

    @NotBlank(message = "SKU suffix is required")
    private String skuSuffix;

    private Boolean isDefault = false;

    private String imageUrl;
    private String altText;
}
