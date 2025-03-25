package com.dev.ecom_platform_2.service;

import com.dev.ecom_platform_2.domain.dtos.CategoryRequestDto;
import com.dev.ecom_platform_2.domain.dtos.CategoryResponseDto;
import com.dev.ecom_platform_2.domain.entities.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto);
    List<CategoryResponseDto> getAllCategories();
    Category findCategoryById (UUID categoryId);
    CategoryResponseDto updateCategory(UUID categoryId, CategoryRequestDto categoryRequestDto);
    void deleteCategory(UUID categoryId);
}
