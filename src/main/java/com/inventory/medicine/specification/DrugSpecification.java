package com.inventory.medicine.specification;

import com.inventory.medicine.model.Drug;
import com.inventory.medicine.model.DrugClassification;
import com.inventory.medicine.model.DrugForm;
import com.inventory.medicine.model.DrugStatus;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
//import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class  DrugSpecification {

    public static Specification<Drug> search(
            String keyword,
            DrugClassification classification,
            DrugForm form,
            DrugStatus status,
            boolean lowStock,

            Integer quantity,
            String quantityOperation,

            String dateField,
            String dateOperation,
            LocalDateTime from,
            LocalDateTime to

    ) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            /* ---------- Keyword ---------- */
            if (keyword != null && !keyword.isBlank()) {
                String like = "%" + keyword.toLowerCase() + "%";
                predicates.add(
                        cb.or(
                                cb.like(cb.lower(root.get("name")), like),
                                cb.like(cb.lower(root.get("drugCode")), like)
                        )
                );
            }

            /* ---------- Basic Filters ---------- */
            if (classification != null)
                predicates.add(cb.equal(root.get("drugClassification"), classification));

            if (form != null)
                predicates.add(cb.equal(root.get("drugForm"), form));

            if (status != null)
                predicates.add(cb.equal(root.get("drugStatus"), status));

            /* ---------- Low Stock ---------- */
            if (lowStock) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("quantityInStock"),
                                root.get("minStockLevel")
                        )
                );
            }

            /* ---------- Quantity ---------- */
//            if (quantity != null)
//                predicates.add(cb.equal(root.get("quantityInStock"), quantity));

            if (quantity != null && quantityOperation != null ){
                switch (quantityOperation) {
                    case "EQ" -> predicates.add(cb.equal(root.get("quantityInStock"), quantity));
                    case "GTE" -> predicates.add(cb.greaterThanOrEqualTo(root.get("quantityInStock"), quantity));
                    case "GT" -> predicates.add(cb.greaterThan(root.get("quantityInStock"), quantity));
                    case "LTE" -> predicates.add(cb.lessThanOrEqualTo(root.get("quantityInStock"), quantity));
                    case "LT" -> predicates.add(cb.lessThan(root.get("quantityInStock"), quantity));
                }
            }

//            if (quantityGte != null)
//                predicates.add(cb.greaterThanOrEqualTo(root.get("quantityInStock"), quantityGte));
//
//            if (quantityLte != null)
//                predicates.add(cb.lessThanOrEqualTo(root.get("quantityInStock"), quantityLte));
//

            /* ---------- Expiry ---------- */

            if (dateField != null && dateOperation != null) {

                Path<LocalDateTime> datePath = switch (dateField) {
                    case "EXPIRY" -> root.get("expiryDate");
                    case "CREATED" -> root.get("createdAt");
                    case "UPDATED" -> root.get("updatedAt");
                    default -> null;
                };

                if (datePath != null) {
                    switch (dateOperation) {
                        case "BEFORE" ->
                                predicates.add(cb.lessThan(datePath, from));
                        case "AFTER" ->
                                predicates.add(cb.greaterThan(datePath, from));
                        case "BETWEEN" ->
                                predicates.add(cb.between(datePath, from, to));
                    }
                }
            }


//            if (expiryBefore != null)
//                predicates.add(cb.lessThan(root.get("expiryDate"), expiryBefore));
//
//            if (expiryFrom != null)
//                predicates.add(cb.greaterThanOrEqualTo(root.get("expiryDate"), expiryFrom));
//
//            if (expiryTo != null)
//                predicates.add(cb.lessThanOrEqualTo(root.get("expiryDate"), expiryTo));
//
//            /* ---------- Created ---------- */
//            if (createdFrom != null)
//                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), createdFrom));
//
//            if (createdTo != null)
//                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), createdTo));
//
//            /* ---------- Updated ---------- */
//            if (updatedFrom != null)
//                predicates.add(cb.greaterThanOrEqualTo(root.get("updatedAt"), updatedFrom));
//
//            if (updatedTo != null)
//                predicates.add(cb.lessThanOrEqualTo(root.get("updatedAt"), updatedTo));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
