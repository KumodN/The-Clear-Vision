package com.example.theclearvision.feature.order.repository;

import com.example.theclearvision.feature.order.domain.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository
        extends JpaRepository<OrderEntity, String>,
        org.springframework.data.jpa.repository.JpaSpecificationExecutor<OrderEntity> {
    List<OrderEntity> findByUserId(Long userId);

    boolean existsByOrderNumber(String orderNumber);
}
