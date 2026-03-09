package com.inventory.medicine.specification;

import com.inventory.medicine.model.doctor.Doctor;
import com.inventory.medicine.model.doctor.Specialization;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class DoctorSpecification {

    public static Specification<Doctor> search(
            String keyword,
            Specialization specialization,
            Boolean active
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<Object, Object> user = root.join("user");

            // 1. Multi-column Keyword Search (Name or License)
            if (keyword != null && !keyword.isBlank()) {
                String pattern = "%" + keyword.toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(user.get("name")), pattern),
                        cb.like(cb.lower(root.get("licenseNumber")), pattern)
                ));
            }

            // 2. Exact Match for Specialization
            if (specialization != null) {
                predicates.add(cb.equal(root.get("specialization"), specialization));
            }

            // 3. Status Filter (Active/Inactive)
            if (active != null) {
                predicates.add(cb.equal(root.get("active"), active));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}