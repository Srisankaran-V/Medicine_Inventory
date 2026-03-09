package com.inventory.medicine.specification;

import com.inventory.medicine.model.billing.Bill;
import com.inventory.medicine.model.billing.BillStatus;
import org.springframework.data.jpa.domain.Specification;
import java.time.Instant;

public class BillSpecification {

    public static Specification<Bill> filter(
            Long patientId,
            BillStatus status,
            Instant startDate,
            Instant endDate) {

        return (root, query, cb) -> {
            var predicates = cb.conjunction();

            if (patientId != null) {
                predicates = cb.and(predicates, cb.equal(root.get("patient").get("id"), patientId));
            }
            if (status != null) {
                predicates = cb.and(predicates, cb.equal(root.get("status"), status));
            }
            if (startDate != null) {
                predicates = cb.and(predicates, cb.greaterThanOrEqualTo(root.get("createdAt"), startDate));
            }
            if (endDate != null) {
                predicates = cb.and(predicates, cb.lessThanOrEqualTo(root.get("createdAt"), endDate));
            }

            return predicates;
        };
    }
}