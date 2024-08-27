package com.rikkeisoft.inventory_management.specification;

import com.rikkeisoft.inventory_management.model.Car;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CarSpecification {

    public static Specification<Car> filterCars(String name, Long carId, List<Long> manufacturerIds, Date createdFrom, Date createdTo) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.isFalse(root.get("isDeleted")));

            if (name != null && !name.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            if (carId != null) {
                predicates.add(criteriaBuilder.equal(root.get("id"), carId));
            }

            if (manufacturerIds != null && !manufacturerIds.isEmpty()) {
                predicates.add(root.get("manufacturer").get("id").in(manufacturerIds));
            }

            if (createdFrom != null && createdTo != null) {
                predicates.add(criteriaBuilder.between(root.get("creationDate"), createdFrom, createdTo));
            } else if (createdFrom != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("creationDate"), createdFrom));
            } else if (createdTo != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("creationDate"), createdTo));
            }

            query.orderBy(criteriaBuilder.desc(root.get("updateDate")));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

