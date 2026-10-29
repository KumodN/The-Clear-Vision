package com.example.theclearvision.persistence;

import com.example.theclearvision.domain.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

    Optional<ProductCategory> findBySlug(String slug);

    Optional<ProductCategory> findByName(String name);

    List<ProductCategory> findByParentIsNullAndIsActiveTrue();

    List<ProductCategory> findByParentIdAndIsActiveTrue(Long parentId);

    List<ProductCategory> findByIsActiveTrue();

    @Query("SELECT c FROM ProductCategory c WHERE c.isActive = true ORDER BY c.name")
    List<ProductCategory> findAllActiveCategories();

    boolean existsBySlug(String slug);

    boolean existsBySlugAndIdNot(String slug, Long id);
}
