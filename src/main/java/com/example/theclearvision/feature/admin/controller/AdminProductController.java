package com.example.theclearvision.feature.admin.controller;

import com.example.theclearvision.common.dto.request.ProductRequest;
import com.example.theclearvision.feature.admin.dto.AdminProductDTO;
import com.example.theclearvision.feature.admin.dto.AdminProductDetailDTO;
import com.example.theclearvision.feature.admin.service.AdminProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminProductController {

    private final AdminProductService adminProductService;

    @GetMapping
    public ResponseEntity<List<AdminProductDTO>> getAllProducts(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortDir) {
        List<AdminProductDTO> products = adminProductService.getProducts(search, category, gender, isActive, sortBy,
                sortDir);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminProductDetailDTO> getProductDetail(@PathVariable String id) {
        AdminProductDetailDTO product = adminProductService.getProductDetail(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<AdminProductDetailDTO> createProduct(@Valid @RequestBody ProductRequest request) {
        AdminProductDetailDTO product = adminProductService.createProduct(request);
        return ResponseEntity.ok(product);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminProductDetailDTO> updateProduct(
            @PathVariable String id,
            @Valid @RequestBody ProductRequest request) {
        AdminProductDetailDTO product = adminProductService.updateProduct(id, request);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        adminProductService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
