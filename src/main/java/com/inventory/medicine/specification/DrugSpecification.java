package com.inventory.medicine.specification;

import com.inventory.medicine.dto.drug.DrugSearchCriteria;
import com.inventory.medicine.model.drug.Drug;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DrugSpecification {
    public static Specification<Drug> search(DrugSearchCriteria criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Keyword Search
            if (criteria.keyword() != null && !criteria.keyword().isBlank()) {
                String like = "%" + criteria.keyword().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("name")), like),
                        cb.like(cb.lower(root.get("drugCode")), like)
                ));
            }

            // Basic Filters
            if (criteria.classification() != null)
                predicates.add(cb.equal(root.get("drugClassification"), criteria.classification()));
            if (criteria.form() != null)
                predicates.add(cb.equal(root.get("drugForm"), criteria.form()));
            if (criteria.status() != null)
                predicates.add(cb.equal(root.get("drugStatus"), criteria.status()));

            // Low Stock
            if (criteria.lowStock()) {
                predicates.add(cb.lessThanOrEqualTo(root.get("quantityInStock"), root.get("minStockLevel")));
            }

            // Quantity Logic
            if (criteria.quantity() != null && criteria.quantityOperation() != null) {
                switch (criteria.quantityOperation()) {
                    case "EQ" -> predicates.add(cb.equal(root.get("quantityInStock"), criteria.quantity()));
                    case "GTE" -> predicates.add(cb.greaterThanOrEqualTo(root.get("quantityInStock"), criteria.quantity()));
                    case "LTE" -> predicates.add(cb.lessThanOrEqualTo(root.get("quantityInStock"), criteria.quantity()));
                    // ... other cases
                }
            }

            // Dynamic Date Range (Improved Suggestion #1)
            if (criteria.dateField() != null) {
                Path<LocalDateTime> datePath = switch (criteria.dateField()) {
                    case "EXPIRY" -> root.get("expiryDate");
                    case "CREATED" -> root.get("createdAt");
                    default -> root.get("updatedAt");
                };

                if (criteria.from() != null)
                    predicates.add(cb.greaterThanOrEqualTo(datePath, criteria.from()));
                if (criteria.to() != null)
                    predicates.add(cb.lessThanOrEqualTo(datePath, criteria.to()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}