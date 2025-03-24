package com.dev.ecom_platform_2.mapper;

import com.dev.ecom_platform_2.domain.dtos.CategoryRequestDto;
import com.dev.ecom_platform_2.domain.dtos.CategoryResponseDto;
import com.dev.ecom_platform_2.domain.entities.Category;

public interface CategoryMapper {
    Category fromDto(CategoryRequestDto categoryRequestDto);
    CategoryResponseDto toDto(Category category);
}
