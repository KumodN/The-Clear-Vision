package com.example.theclearvision.feature.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItemRequest {

    @NotNull(message = "Product variant ID is required")
    private String productVariantId;

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    private Long lensId; // Optional
}
