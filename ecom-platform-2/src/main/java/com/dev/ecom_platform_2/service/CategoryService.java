package com.dev.ecom_platform_2.service;

import com.dev.ecom_platform_2.domain.dtos.CategoryListResponseDto;
import com.dev.ecom_platform_2.domain.dtos.CategoryRequestDto;
import com.dev.ecom_platform_2.domain.dtos.CategoryResponseDto;
import com.dev.ecom_platform_2.domain.entities.Category;

import java.util.UUID;

public interface CategoryService {
    CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto);
    CategoryListResponseDto getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortDirection);
    Category findCategoryById(UUID categoryId);
    CategoryResponseDto updateCategory(UUID categoryId, CategoryRequestDto categoryRequestDto);
    void deleteCategory(UUID categoryId);
}
