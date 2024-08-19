package com.rikkeisoft.inventory_management.repository;

import com.rikkeisoft.inventory_management.model.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {
    boolean existsByName(String name);
    Optional<Manufacturer> findByIdAndIsDeletedFalse(Long id);
    List<Manufacturer> findAllByIsDeletedFalse();
}
