package com.rikkeisoft.inventory_management.mapper;

import com.rikkeisoft.inventory_management.dto.CategoryDTO;
import com.rikkeisoft.inventory_management.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryDTO toCategoryDTO(Category category);

    Category toCategory(CategoryDTO categoryDTO);
}
