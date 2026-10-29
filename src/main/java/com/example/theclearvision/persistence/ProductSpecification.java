package com.example.theclearvision.persistence;

import com.example.theclearvision.domain.Product;
import com.example.theclearvision.domain.ProductCategory;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    public static Specification<Product> getProducts(String search, String category, String gender, Boolean isActive) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Search (Title or SKU)
            if (StringUtils.hasText(search)) {
                String searchLike = "%" + search.toLowerCase() + "%";
                Predicate titlePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), searchLike);
                Predicate skuPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("sku")), searchLike);
                predicates.add(criteriaBuilder.or(titlePredicate, skuPredicate));
            }

            // Category
            if (StringUtils.hasText(category)) {
                Join<Product, ProductCategory> categoryJoin = root.join("category", JoinType.LEFT);
                predicates.add(
                        criteriaBuilder.equal(criteriaBuilder.lower(categoryJoin.get("name")), category.toLowerCase()));
            }

            // Gender
            if (StringUtils.hasText(gender)) {
                try {
                    Product.Gender genderEnum = Product.Gender.valueOf(gender.toUpperCase());
                    predicates.add(criteriaBuilder.equal(root.get("gender"), genderEnum));
                } catch (IllegalArgumentException e) {
                    // Ignore invalid gender filter or handle as needed
                }
            }

            // Is Active
            if (isActive != null) {
                predicates.add(criteriaBuilder.equal(root.get("isActive"), isActive));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
