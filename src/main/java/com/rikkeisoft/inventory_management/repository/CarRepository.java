package com.rikkeisoft.inventory_management.repository;

import com.rikkeisoft.inventory_management.model.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {
    boolean existsByName(String name);
    Optional<Car> findByIdAndIsDeletedFalse(Long id);
    Page<Car> findByIsDeletedFalse(Pageable pageable);
}