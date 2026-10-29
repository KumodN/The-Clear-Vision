package com.example.theclearvision.feature.order.repository;

import com.example.theclearvision.domain.User;
import com.example.theclearvision.feature.order.domain.OrderEntity;
import com.example.theclearvision.feature.order.domain.OrderStatus;
import com.example.theclearvision.feature.payment.domain.Payment;
import com.example.theclearvision.feature.payment.domain.PaymentMethod;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class OrderSpecification {

    public static Specification<OrderEntity> getOrders(String search, String status, String paymentMethod) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(search)) {
                String searchLike = "%" + search.toLowerCase() + "%";
                Join<OrderEntity, User> userJoin = root.join("user", JoinType.LEFT);

                Predicate orderNumberPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("orderNumber")),
                        searchLike);
                Predicate firstNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(userJoin.get("firstName")),
                        searchLike);
                Predicate lastNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(userJoin.get("lastName")),
                        searchLike);
                Predicate emailPredicate = criteriaBuilder.like(criteriaBuilder.lower(userJoin.get("email")),
                        searchLike);

                predicates.add(criteriaBuilder.or(orderNumberPredicate, firstNamePredicate, lastNamePredicate,
                        emailPredicate));
            }

            if (StringUtils.hasText(status)) {
                try {
                    OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
                    predicates.add(criteriaBuilder.equal(root.get("status"), orderStatus));
                } catch (IllegalArgumentException e) {
                    // Ignore invalid status
                }
            }

            if (StringUtils.hasText(paymentMethod)) {
                try {
                    PaymentMethod method = PaymentMethod.valueOf(paymentMethod.toUpperCase());
                    Join<OrderEntity, Payment> paymentJoin = root.join("payment", JoinType.LEFT);
                    predicates.add(criteriaBuilder.equal(paymentJoin.get("method"), method));
                } catch (IllegalArgumentException e) {
                    // Ignore invalid payment method
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
