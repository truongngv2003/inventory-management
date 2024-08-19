package com.rikkeisoft.inventory_management.service;

import com.rikkeisoft.inventory_management.dto.CategoryDTO;
import com.rikkeisoft.inventory_management.exception.NotFoundException;
import com.rikkeisoft.inventory_management.mapper.CategoryMapper;
import com.rikkeisoft.inventory_management.model.Category;
import com.rikkeisoft.inventory_management.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryDTO getCategoryById(Long id){
        Category category = categoryRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Category not found or has been deleted"));

        return CategoryMapper.INSTANCE.toCategoryDTO(category);
    }


    public List<CategoryDTO> getCategories() {
        return categoryRepository.findAllByIsDeletedFalse().stream()
                .map(CategoryMapper.INSTANCE::toCategoryDTO)
                .toList();
    }


    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        if (categoryRepository.existsByName(categoryDTO.getName())) {
            throw new DataIntegrityViolationException("Category with the same name already exists");
        }

        Category category = CategoryMapper.INSTANCE.toCategory(categoryDTO);
        category = categoryRepository.save(category);

        return CategoryMapper.INSTANCE.toCategoryDTO(category);
    }


    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Category not found or has been delete"));

        if (!category.getName().equals(categoryDTO.getName()) && categoryRepository.existsByName(categoryDTO.getName())) {
            throw new DataIntegrityViolationException("Category with the same name already exists");
        }

        category.setName(categoryDTO.getName());

        category = categoryRepository.save(category);

        return CategoryMapper.INSTANCE.toCategoryDTO(category);
    }

    public CategoryDTO deleteCategory(Long id) {
        Category category = categoryRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Category not found or has been deleted"));

        category.setIsDeleted(true);
        categoryRepository.save(category);

        return CategoryMapper.INSTANCE.toCategoryDTO(category);
    }
}