package com.rikkeisoft.inventory_management.repository;

import com.rikkeisoft.inventory_management.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
    Optional<Category> findByIdAndIsDeletedFalse(Long id);
    List<Category> findAllByIsDeletedFalse();
}