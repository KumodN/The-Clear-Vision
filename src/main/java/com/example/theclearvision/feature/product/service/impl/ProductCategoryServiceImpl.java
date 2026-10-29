package com.example.theclearvision.feature.product.service.impl;

import com.example.theclearvision.common.dto.request.ProductCategoryRequest;
import com.example.theclearvision.common.dto.response.ProductCategoryResponse;
import com.example.theclearvision.common.exception.ResourceNotFoundException;
import com.example.theclearvision.domain.ProductCategory;
import com.example.theclearvision.feature.product.mapper.ProductCategoryMapper;
import com.example.theclearvision.feature.product.service.ProductCategoryService;
import com.example.theclearvision.persistence.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private final ProductCategoryRepository categoryRepository;
    private final ProductCategoryMapper categoryMapper;

    @Override
    public ProductCategoryResponse createCategory(ProductCategoryRequest request) {
        log.info("Creating new product category: {}", request.getName());

        // Check if slug already exists
        if (categoryRepository.existsBySlug(request.getSlug())) {
            throw new IllegalArgumentException("Category with slug '" + request.getSlug() + "' already exists");
        }

        ProductCategory category = categoryMapper.toEntity(request);

        // Set parent category if provided
        if (request.getParentId() != null) {
            ProductCategory parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent category not found with id: " + request.getParentId()));
            category.setParent(parent);
        }

        ProductCategory savedCategory = categoryRepository.save(category);
        log.info("Created product category with id: {}", savedCategory.getId());

        return categoryMapper.toResponse(savedCategory);
    }

    @Override
    public ProductCategoryResponse updateCategory(Long id, ProductCategoryRequest request) {
        log.info("Updating product category with id: {}", id);

        ProductCategory existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product category not found with id: " + id));

        // Check if slug is taken by another category
        if (!existingCategory.getSlug().equals(request.getSlug()) &&
                categoryRepository.existsBySlugAndIdNot(request.getSlug(), id)) {
            throw new IllegalArgumentException("Category with slug '" + request.getSlug() + "' already exists");
        }

        categoryMapper.updateEntityFromRequest(request, existingCategory);

        // Update parent category if changed
        if (request.getParentId() != null) {
            if (existingCategory.getParent() == null || !existingCategory.getParent().getId().equals(request.getParentId())) {
                ProductCategory parent = categoryRepository.findById(request.getParentId())
                        .orElseThrow(() -> new ResourceNotFoundException("Parent category not found with id: " + request.getParentId()));
                existingCategory.setParent(parent);
            }
        } else {
            existingCategory.setParent(null);
        }

        ProductCategory updatedCategory = categoryRepository.save(existingCategory);
        log.info("Updated product category with id: {}", id);

        return categoryMapper.toResponse(updatedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductCategoryResponse getCategoryById(Long id) {
        log.debug("Fetching product category by id: {}", id);

        ProductCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product category not found with id: " + id));

        return categoryMapper.toResponse(category);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductCategoryResponse getCategoryBySlug(String slug) {
        log.debug("Fetching product category by slug: {}", slug);

        ProductCategory category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Product category not found with slug: " + slug));

        return categoryMapper.toResponse(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductCategoryResponse> getAllCategories() {
        log.debug("Fetching all active product categories");

        List<ProductCategory> categories = categoryRepository.findAllActiveCategories();
        return categoryMapper.toResponseList(categories);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductCategoryResponse> getRootCategories() {
        log.debug("Fetching root product categories");

        List<ProductCategory> categories = categoryRepository.findByParentIsNullAndIsActiveTrue();
        return categoryMapper.toResponseList(categories);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductCategoryResponse> getChildCategories(Long parentId) {
        log.debug("Fetching child categories for parent id: {}", parentId);

        List<ProductCategory> categories = categoryRepository.findByParentIdAndIsActiveTrue(parentId);
        return categoryMapper.toResponseList(categories);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductCategoryResponse> getCategories(Pageable pageable) {
        log.debug("Fetching product categories with pagination");

        Page<ProductCategory> categories = categoryRepository.findAll(pageable);
        return categories.map(categoryMapper::toResponse);
    }

    @Override
    public void deleteCategory(Long id) {
        log.info("Deleting product category with id: {}", id);

        ProductCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product category not found with id: " + id));

        // Check if category has products
        if (!category.getProducts().isEmpty()) {
            throw new IllegalStateException("Cannot delete category with associated products");
        }

        // Check if category has children
        if (!category.getChildren().isEmpty()) {
            throw new IllegalStateException("Cannot delete category with child categories");
        }

        categoryRepository.delete(category);
        log.info("Deleted product category with id: {}", id);
    }

    @Override
    public void toggleCategoryStatus(Long id, boolean isActive) {
        log.info("Toggling category status for id: {} to {}", id, isActive);

        ProductCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product category not found with id: " + id));

        category.setIsActive(isActive);
        categoryRepository.save(category);
        log.info("Updated category status for id: {} to {}", id, isActive);
    }
}
