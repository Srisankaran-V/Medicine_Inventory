package com.inventory.medicine.specification;

import com.inventory.medicine.dto.doctor.DoctorSearchCriteria;
import com.inventory.medicine.model.doctor.Doctor;
import com.inventory.medicine.model.doctor.Specialization;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class DoctorSpecification {

    public static Specification<Doctor> search(
            DoctorSearchCriteria criteria
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<Object, Object> user = root.join("user");

            // 1. Multi-column Keyword Search (Name or License)
            if (criteria.keyword() != null && !criteria.keyword().isBlank()) {
                String pattern = "%" + criteria.keyword().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(user.get("fullName")), pattern),
                        cb.like(cb.lower(root.get("licenseNumber")), pattern)
                ));
            }

            // 2. Exact Match for Specialization
            if (criteria.specialization() != null) {
                predicates.add(cb.equal(root.get("specialization"), criteria.specialization()));
            }

            // 3. Status Filter (Active/Inactive)
            if (criteria.active() != null) {
                predicates.add(cb.equal(root.get("active"), criteria.active()));
            }

             /* ---------- Date Range Filter ----------
               SQL: ... AND created_at BETWEEN 'from' AND 'to'
            */
            if (criteria.from() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), criteria.from()));
            }
            if (criteria.to() != null){
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), criteria.to()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}