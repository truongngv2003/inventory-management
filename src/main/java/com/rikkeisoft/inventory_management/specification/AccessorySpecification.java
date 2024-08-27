package com.rikkeisoft.inventory_management.specification;

import com.rikkeisoft.inventory_management.model.Accessory;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class AccessorySpecification {

    public static Specification<Accessory> filterAccessories(
            String name, String description, Long accessoryId,
            BigDecimal minPrice, BigDecimal maxPrice,List<Long> categoryIds,
            List<Long> manufacturerIds, List<Long> carIds) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.isFalse(root.get("isDeleted")));

            if (name != null && !name.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            if (description != null && !description.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + description.toLowerCase() + "%"));
            }

            if (accessoryId != null) {
                predicates.add(criteriaBuilder.equal(root.get("id"), accessoryId));
            }

            if (minPrice != null & maxPrice != null) {
                predicates.add(criteriaBuilder.between(root.get("price"), minPrice, maxPrice));
            } else if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice));
            } else if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice));
            }

            if (categoryIds != null && !categoryIds.isEmpty()) {
                predicates.add(root.get("category").get("id").in(categoryIds));
            }

            if (manufacturerIds != null && !manufacturerIds.isEmpty()) {
                predicates.add(root.get("manufacturer").get("id").in(manufacturerIds));
            }

            if (carIds != null && !carIds.isEmpty()) {
                Join<Object, Object> carAccessoriesJoin = root.join("carAccessories");

                Join<Object, Object> carJoin = carAccessoriesJoin.join("car");

                predicates.add(carJoin.get("id").in(carIds));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

    }
}
