package com.rikkeisoft.inventory_management.repository;

import com.rikkeisoft.inventory_management.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
    boolean existsByName(String name);
}