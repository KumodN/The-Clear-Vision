package com.example.theclearvision.feature.admin.service;

import com.example.theclearvision.feature.admin.dto.AdminProductDTO;
import com.example.theclearvision.feature.admin.dto.AdminProductDetailDTO;
import com.example.theclearvision.common.dto.request.ProductRequest;

import java.util.List;

public interface AdminProductService {
    List<AdminProductDTO> getProducts(String search, String category, String gender, Boolean isActive, String sortBy,
            String sortDir);

    AdminProductDetailDTO getProductDetail(String id);

    AdminProductDetailDTO createProduct(ProductRequest request);

    AdminProductDetailDTO updateProduct(String id, ProductRequest request);

    void deleteProduct(String id);
}
