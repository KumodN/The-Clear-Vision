package com.example.theclearvision.feature.auth.repository;

import com.example.theclearvision.domain.Role;
import com.example.theclearvision.domain.User;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {

    public static Specification<User> getUsers(String search, String roleName) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(search)) {
                String searchLike = "%" + search.toLowerCase() + "%";

                Predicate firstNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")),
                        searchLike);
                Predicate lastNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")),
                        searchLike);
                Predicate emailPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), searchLike);

                predicates.add(criteriaBuilder.or(firstNamePredicate, lastNamePredicate, emailPredicate));
            }

            if (StringUtils.hasText(roleName)) {
                Join<User, Role> roleJoin = root.join("role", JoinType.LEFT);
                predicates.add(criteriaBuilder.equal(roleJoin.get("name"), roleName));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
