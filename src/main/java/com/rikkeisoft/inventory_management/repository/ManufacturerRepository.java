package com.rikkeisoft.inventory_management.repository;

import com.rikkeisoft.inventory_management.model.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {
    boolean existsByName(String name);
}
