package com.dev.ecom_platform_2.mapper;

import com.dev.ecom_platform_2.domain.dtos.CategoryRequest;
import com.dev.ecom_platform_2.domain.dtos.CategoryDto;
import com.dev.ecom_platform_2.domain.entities.Category;

public interface CategoryMapper {
    Category fromDto(CategoryRequest categoryRequest);
    CategoryDto toDto(Category category);
}
