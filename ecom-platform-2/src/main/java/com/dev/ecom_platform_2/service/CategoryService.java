package com.dev.ecom_platform_2.service;

import com.dev.ecom_platform_2.domain.dtos.CategoryListDto;
import com.dev.ecom_platform_2.domain.dtos.CategoryRequest;
import com.dev.ecom_platform_2.domain.dtos.CategoryDto;
import com.dev.ecom_platform_2.domain.entities.Category;

import java.util.UUID;

public interface CategoryService {
    CategoryDto createCategory(CategoryRequest categoryRequest);
    Category findCategoryById(UUID categoryId);
    CategoryListDto getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortDirection);
    CategoryDto updateCategory(UUID categoryId, CategoryRequest categoryRequest);
    void deleteCategory(UUID categoryId);
}
