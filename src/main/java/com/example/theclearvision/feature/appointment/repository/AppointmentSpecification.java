package com.example.theclearvision.feature.appointment.repository;

import com.example.theclearvision.domain.User;
import com.example.theclearvision.feature.appointment.Appointment;
import com.example.theclearvision.feature.appointment.AppointmentStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentSpecification {

    public static Specification<Appointment> getAppointments(String search, String status, LocalDateTime startDate,
            LocalDateTime endDate) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(search)) {
                String searchLike = "%" + search.toLowerCase() + "%";
                Join<Appointment, User> userJoin = root.join("user", JoinType.LEFT);

                Predicate firstNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(userJoin.get("firstName")),
                        searchLike);
                Predicate lastNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(userJoin.get("lastName")),
                        searchLike);
                Predicate notesPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("notes")), searchLike);

                predicates.add(criteriaBuilder.or(firstNamePredicate, lastNamePredicate, notesPredicate));
            }

            if (StringUtils.hasText(status)) {
                try {
                    AppointmentStatus appointmentStatus = AppointmentStatus.valueOf(status.toUpperCase());
                    predicates.add(criteriaBuilder.equal(root.get("status"), appointmentStatus));
                } catch (IllegalArgumentException e) {
                    // Ignore invalid status
                }
            }

            if (startDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("appointmentTime"), startDate));
            }

            if (endDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("appointmentTime"), endDate));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
