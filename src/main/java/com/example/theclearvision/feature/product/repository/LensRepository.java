package com.example.theclearvision.feature.product.repository;

import com.example.theclearvision.feature.product.domain.Lens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LensRepository extends JpaRepository<Lens, Long> {
}
