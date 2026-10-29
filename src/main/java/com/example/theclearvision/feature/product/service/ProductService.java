package com.example.theclearvision.feature.product.service;

import com.example.theclearvision.common.dto.request.ProductRequest;
import com.example.theclearvision.common.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request);

    ProductResponse updateProduct(String id, ProductRequest request);

    ProductResponse getProductById(String id);

    ProductResponse getProductBySlug(String slug);

    Page<ProductResponse> getAllProducts(Pageable pageable);

    Page<ProductResponse> getProductsByCategory(Long categoryId, Pageable pageable);

    Page<ProductResponse> getProductsByGender(String gender, Pageable pageable);

    Page<ProductResponse> searchProducts(String query, Pageable pageable);

    Page<ProductResponse> getInStockProducts(Pageable pageable);

    void deleteProduct(String id);

    void toggleProductStatus(String id, boolean isActive);

    void updateStock(String productVariantId, Integer quantity);

    List<ProductResponse> getProductsByIds(List<String> ids);
}
