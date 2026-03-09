package com.inventory.medicine.specification;

import com.inventory.medicine.model.prescription.Prescription;
import org.springframework.data.jpa.domain.Specification;
import java.time.Instant;

public class PrescriptionSpecification {

    public static Specification<Prescription> filter(
            Long patientId,
            Long doctorId,
            String diagnosis,
            Instant fromDate,
            Instant toDate) {

        return (root, query, cb) -> {
            var predicates = cb.conjunction();

            if (patientId != null) {
                predicates = cb.and(predicates, cb.equal(root.get("patient").get("id"), patientId));
            }
            if (doctorId != null) {
                predicates = cb.and(predicates, cb.equal(root.get("doctor").get("id"), doctorId));
            }
            if (diagnosis != null && !diagnosis.isEmpty()) {
                // Case-insensitive partial search for diagnosis
                predicates = cb.and(predicates, cb.like(cb.lower(root.get("diagnosis")), "%" + diagnosis.toLowerCase() + "%"));
            }
            if (fromDate != null) {
                predicates = cb.and(predicates, cb.greaterThanOrEqualTo(root.get("createdAt"), fromDate));
            }
            if (toDate != null) {
                predicates = cb.and(predicates, cb.lessThanOrEqualTo(root.get("createdAt"), toDate));
            }

            return predicates;
        };
    }
}