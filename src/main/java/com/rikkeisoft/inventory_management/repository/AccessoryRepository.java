package com.rikkeisoft.inventory_management.repository;

import com.rikkeisoft.inventory_management.model.Accessory;
import com.rikkeisoft.inventory_management.model.Car;
import com.rikkeisoft.inventory_management.model.Manufacturer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccessoryRepository extends JpaRepository<Accessory, Long>, JpaSpecificationExecutor<Accessory> {

    Optional<Accessory> findByIdAndIsDeletedFalse(Long id);

    Page<Accessory> findByIsDeletedFalse(Pageable pageable);

    @Query("SELECT COUNT(ca) > 0 " +
            "FROM CarAccessory ca " +
            "JOIN ca.accessory a " +
            "WHERE a.name = :name " +
            "AND ca.car = :car " +
            "AND a.manufacturer = :manufacturer " +
            "AND a.isDeleted = false")
    boolean existsByAccessoryNameAndCarAndManufacturer(
            @Param("name") String name,
            @Param("car") Car car,
            @Param("manufacturer") Manufacturer manufacturer);


    @Query("SELECT COUNT(ca) > 0 " +
            "FROM CarAccessory ca " +
            "JOIN ca.accessory a " +
            "WHERE a.code = :code " +
            "AND ca.car = :car " +
            "AND a.manufacturer = :manufacturer " +
            "AND a.isDeleted = false")
    boolean existsByAccessoryCodeAndCarAndManufacturer(
            @Param("code") String code,
            @Param("car") Car car,
            @Param("manufacturer") Manufacturer manufacturer);


}