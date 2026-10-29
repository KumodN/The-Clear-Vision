package com.example.theclearvision.feature.product.service.impl;

import com.example.theclearvision.common.dto.request.ProductRequest;
import com.example.theclearvision.common.dto.response.ProductResponse;
import com.example.theclearvision.common.exception.ResourceNotFoundException;
import com.example.theclearvision.domain.Product;
import com.example.theclearvision.domain.ProductCategory;
import com.example.theclearvision.domain.ProductImage;
import com.example.theclearvision.domain.ProductVariant;
import com.example.theclearvision.feature.product.mapper.ProductMapper;
import com.example.theclearvision.feature.product.service.ProductService;
import com.example.theclearvision.persistence.ProductCategoryRepository;
import com.example.theclearvision.persistence.ProductRepository;
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
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        log.info("Creating new product: {}", request.getTitle());

        // Validate unique constraints
        validateProductUniqueness(null, request.getSku(), request.getSlug());

        // Fetch category
        ProductCategory category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));

        Product product = productMapper.toEntity(request);
        product.setCategory(category);

        // Process variants and images
        processProductVariants(product, request);

        Product savedProduct = productRepository.save(product);
        log.info("Created product with id: {}", savedProduct.getId());

        return productMapper.toResponse(savedProduct);
    }

    @Override
    public ProductResponse updateProduct(String id, ProductRequest request) {
        log.info("Updating product with id: {}", id);

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        // Validate unique constraints
        validateProductUniqueness(id, request.getSku(), request.getSlug());

        // Fetch category if changed
        if (!existingProduct.getCategory().getId().equals(request.getCategoryId())) {
            ProductCategory category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.getCategoryId()));
            existingProduct.setCategory(category);
        }

        productMapper.updateEntityFromRequest(request, existingProduct);

        // Clear existing variants and images
        existingProduct.getVariants().clear();
        existingProduct.getImages().clear();

        // Process new variants and images
        processProductVariants(existingProduct, request);

        Product updatedProduct = productRepository.save(existingProduct);
        log.info("Updated product with id: {}", id);

        return productMapper.toResponse(updatedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(String id) {
        log.debug("Fetching product by id: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        return productMapper.toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductBySlug(String slug) {
        log.debug("Fetching product by slug: {}", slug);

        Product product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with slug: " + slug));

        return productMapper.toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        log.debug("Fetching all active products with pagination");

        Page<Product> products = productRepository.findByIsActiveTrue(pageable);
        return products.map(productMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductsByCategory(Long categoryId, Pageable pageable) {
        log.debug("Fetching products by category id: {}", categoryId);

        Page<Product> products = productRepository.findByCategoryIdAndIsActiveTrue(categoryId, pageable);
        return products.map(productMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductsByGender(String gender, Pageable pageable) {
        log.debug("Fetching products by gender: {}", gender);

        try {
            Product.Gender genderEnum = Product.Gender.valueOf(gender.toUpperCase());
            Page<Product> products = productRepository.findByGenderAndIsActiveTrue(genderEnum, pageable);
            return products.map(productMapper::toResponse);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid gender value: " + gender);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> searchProducts(String query, Pageable pageable) {
        log.debug("Searching products with query: {}", query);

        if (query == null || query.trim().isEmpty()) {
            return getAllProducts(pageable);
        }

        Page<Product> products = productRepository.searchActiveProducts(query.trim(), pageable);
        return products.map(productMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getInStockProducts(Pageable pageable) {
        log.debug("Fetching in-stock products");

        Page<Product> products = productRepository.findInStockProducts(pageable);
        return products.map(productMapper::toResponse);
    }

    @Override
    public void deleteProduct(String id) {
        log.info("Deleting product with id: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        productRepository.delete(product);
        log.info("Deleted product with id: {}", id);
    }

    @Override
    public void toggleProductStatus(String id, boolean isActive) {
        log.info("Toggling product status for id: {} to {}", id, isActive);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.setIsActive(isActive);
        productRepository.save(product);
        log.info("Updated product status for id: {} to {}", id, isActive);
    }

    @Override
    public void updateStock(String productVariantId, Integer quantity) {
        log.info("Updating stock for variant id: {} to {}", productVariantId, quantity);

        // This would typically find the variant and update its stock
        // For now, we'll implement this when we add inventory management
        log.warn("Stock update not yet implemented for variant: {}", productVariantId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsByIds(List<String> ids) {
        log.debug("Fetching products by ids: {}", ids);

        List<Product> products = productRepository.findByIdIn(ids);
        return productMapper.toResponseList(products);
    }

    private void validateProductUniqueness(String productId, String sku, String slug) {
        if (productId == null) {
            // Creating new product
            if (productRepository.existsBySku(sku)) {
                throw new IllegalArgumentException("Product with SKU '" + sku + "' already exists");
            }
            if (productRepository.existsBySlug(slug)) {
                throw new IllegalArgumentException("Product with slug '" + slug + "' already exists");
            }
        } else {
            // Updating existing product
            if (productRepository.existsBySkuAndIdNot(sku, productId)) {
                throw new IllegalArgumentException("Product with SKU '" + sku + "' already exists");
            }
            if (productRepository.existsBySlugAndIdNot(slug, productId)) {
                throw new IllegalArgumentException("Product with slug '" + slug + "' already exists");
            }
        }
    }

    private void processProductVariants(Product product, ProductRequest request) {
        if (request.getVariants() != null) {
            for (var variantRequest : request.getVariants()) {
                ProductVariant variant = new ProductVariant();
                variant.setVariantName(variantRequest.getVariantName());
                variant.setFrameColor(variantRequest.getFrameColor());
                variant.setFrameSize(variantRequest.getFrameSize());
                variant.setFrameMaterial(variantRequest.getFrameMaterial());
                variant.setPrice(variantRequest.getPrice());
                variant.setStockQuantity(variantRequest.getStockQuantity());
                variant.setSkuSuffix(variantRequest.getSkuSuffix());
                variant.setIsDefault(variantRequest.getIsDefault());
                variant.setProduct(product);

                product.getVariants().add(variant);

                // Create image for variant if provided
                if (variantRequest.getImageUrl() != null) {
                    ProductImage image = new ProductImage();
                    image.setImageUrl(variantRequest.getImageUrl());
                    image.setAltText(variantRequest.getAltText());
                    image.setProduct(product);
                    image.setVariant(variant);
                    image.setIsPrimary(true);
                    image.setDisplayOrder(0);

                    product.getImages().add(image);
                    variant.getImages().add(image);
                }
            }
        }
    }
}