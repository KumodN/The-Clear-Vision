package com.example.theclearvision.feature.product.service;

import com.example.theclearvision.common.dto.request.ProductCategoryRequest;
import com.example.theclearvision.common.dto.response.ProductCategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductCategoryService {

    ProductCategoryResponse createCategory(ProductCategoryRequest request);

    ProductCategoryResponse updateCategory(Long id, ProductCategoryRequest request);

    ProductCategoryResponse getCategoryById(Long id);

    ProductCategoryResponse getCategoryBySlug(String slug);

    List<ProductCategoryResponse> getAllCategories();

    List<ProductCategoryResponse> getRootCategories();

    List<ProductCategoryResponse> getChildCategories(Long parentId);

    Page<ProductCategoryResponse> getCategories(Pageable pageable);

    void deleteCategory(Long id);

    void toggleCategoryStatus(Long id, boolean isActive);
}
