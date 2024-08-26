package com.rikkeisoft.inventory_management.repository;

import com.rikkeisoft.inventory_management.model.Car;
import com.rikkeisoft.inventory_management.model.Manufacturer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {
    boolean existsByNameAndManufacturerAndIsDeletedFalse(String name, Manufacturer manufacturer);

    Optional<Car> findByIdAndIsDeletedFalse(Long id);

    Page<Car> findByIsDeletedFalse(Pageable pageable);

    @Query("SELECT COUNT(ca) > 0 FROM CarAccessory ca WHERE ca.car.id = :carId AND ca.isDeleted = false")
    boolean existsActiveCarAccessories(@Param("carId") Long carId);

    @Query("SELECT COUNT(c) > 0 FROM Car c WHERE c.manufacturer = :manufacturer AND c.isDeleted = false AND EXISTS (SELECT ca FROM CarAccessory ca WHERE ca.car = c AND ca.isDeleted = false)")
    boolean existsByManufacturerAndCarAccessoriesIsNotEmpty(@Param("manufacturer") Manufacturer manufacturer);

    List<Car> findByManufacturerAndIsDeletedFalse(Manufacturer manufacturer);

}