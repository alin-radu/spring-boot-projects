package com.dev.ecom_platform_2.mapper;

import com.dev.ecom_platform_2.domain.dtos.CategoryDto;
import com.dev.ecom_platform_2.domain.entities.Category;

public interface CategoryMapper {
    Category fromDto(CategoryDto categoryDto);
    CategoryDto toDto(Category category);
}
