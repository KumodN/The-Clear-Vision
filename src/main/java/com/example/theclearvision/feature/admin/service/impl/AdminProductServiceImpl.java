package com.example.theclearvision.feature.admin.service.impl;

import com.example.theclearvision.common.dto.request.ProductRequest;
import com.example.theclearvision.common.dto.response.ProductImageResponse;
import com.example.theclearvision.common.dto.response.ProductVariantResponse;
import com.example.theclearvision.domain.Product;
import com.example.theclearvision.domain.ProductCategory;
import com.example.theclearvision.domain.ProductVariant;
import com.example.theclearvision.feature.admin.dto.AdminProductDTO;
import com.example.theclearvision.feature.admin.dto.AdminProductDetailDTO;
import com.example.theclearvision.feature.admin.service.AdminProductService;
import com.example.theclearvision.feature.product.mapper.ProductMapper;
import com.example.theclearvision.feature.product.mapper.ProductVariantMapper;
import com.example.theclearvision.persistence.ProductCategoryRepository;
import com.example.theclearvision.persistence.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminProductServiceImpl implements AdminProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final ProductVariantMapper variantMapper;

    @Override
    @Transactional(readOnly = true)
    public List<AdminProductDTO> getProducts(String search, String category, String gender, Boolean isActive,
            String sortBy, String sortDir) {

        org.springframework.data.domain.Sort sort = org.springframework.data.domain.Sort.unsorted();
        if (sortBy != null && !sortBy.isBlank()) {
            org.springframework.data.domain.Sort.Direction direction = "desc".equalsIgnoreCase(sortDir)
                    ? org.springframework.data.domain.Sort.Direction.DESC
                    : org.springframework.data.domain.Sort.Direction.ASC;
            sort = org.springframework.data.domain.Sort.by(direction, sortBy);
        }

        org.springframework.data.jpa.domain.Specification<Product> spec = com.example.theclearvision.persistence.ProductSpecification
                .getProducts(search, category, gender, isActive);

        return productRepository.findAll(spec, sort).stream()
                .map(this::toAdminProductDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AdminProductDetailDTO getProductDetail(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return toAdminProductDetailDTO(product);
    }

    @Override
    @Transactional
    public AdminProductDetailDTO createProduct(ProductRequest request) {
        ProductCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = new Product();
        product.setSku(request.getSku());
        product.setTitle(request.getTitle());
        product.setSlug(request.getSlug());
        product.setDescription(request.getDescription());
        product.setCategory(category);
        product.setGender(request.getGender());
        product.setMaterial(request.getMaterial());
        product.setBasePrice(request.getBasePrice());
        product.setIsActive(request.getIsActive());

        Product savedProduct = productRepository.save(product);
        return toAdminProductDetailDTO(savedProduct);
    }

    @Override
    @Transactional
    public AdminProductDetailDTO updateProduct(String id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        product.setSku(request.getSku());
        product.setTitle(request.getTitle());
        product.setSlug(request.getSlug());
        product.setDescription(request.getDescription());
        product.setCategory(category);
        product.setGender(request.getGender());
        product.setMaterial(request.getMaterial());
        product.setBasePrice(request.getBasePrice());
        product.setIsActive(request.getIsActive());

        Product updatedProduct = productRepository.save(product);
        return toAdminProductDetailDTO(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(String id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found");
        }
        productRepository.deleteById(id);
    }

    private AdminProductDTO toAdminProductDTO(Product product) {
        AdminProductDTO dto = new AdminProductDTO();
        dto.setId(product.getId());
        dto.setSku(product.getSku());
        dto.setTitle(product.getTitle());
        dto.setSlug(product.getSlug());
        dto.setDescription(product.getDescription());
        dto.setCategoryName(product.getCategory() != null ? product.getCategory().getName() : "Uncategorized");
        dto.setGender(product.getGender());
        dto.setMaterial(product.getMaterial());
        dto.setBasePrice(product.getBasePrice());
        dto.setIsActive(product.getIsActive());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        dto.setVariantCount(product.getVariants().size());
        dto.setTotalStock(product.getVariants().stream()
                .mapToInt(v -> v.getStockQuantity() != null ? v.getStockQuantity() : 0)
                .sum());
        return dto;
    }

    private AdminProductDetailDTO toAdminProductDetailDTO(Product product) {
        AdminProductDetailDTO dto = new AdminProductDetailDTO();
        dto.setId(product.getId());
        dto.setSku(product.getSku());
        dto.setTitle(product.getTitle());
        dto.setSlug(product.getSlug());
        dto.setDescription(product.getDescription());
        dto.setCategoryId(product.getCategory() != null ? product.getCategory().getId().toString() : null);
        dto.setCategoryName(product.getCategory() != null ? product.getCategory().getName() : "Uncategorized");
        dto.setGender(product.getGender());
        dto.setMaterial(product.getMaterial());
        dto.setBasePrice(product.getBasePrice());
        dto.setIsActive(product.getIsActive());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());

        // Map variants and images using existing mappers
        dto.setVariants(product.getVariants().stream()
                .map(variantMapper::toResponse)
                .collect(Collectors.toList()));

        dto.setImages(product.getImages().stream()
                .map(this::toImageResponse)
                .collect(Collectors.toList()));

        return dto;
    }

    private ProductImageResponse toImageResponse(com.example.theclearvision.domain.ProductImage image) {
        ProductImageResponse response = new ProductImageResponse();
        response.setId(image.getId());
        response.setImageUrl(image.getImageUrl());
        response.setAltText(image.getAltText());
        response.setIsPrimary(image.getIsPrimary());
        response.setDisplayOrder(image.getDisplayOrder());
        return response;
    }
}
