package com.dev.ecom_platform_2.service;

import com.dev.ecom_platform_2.domain.entities.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    Category createCategory(Category category);
    List<Category> getAllCategories();
    Category findCategoryById (UUID categoryId);
    Category updateCategory(UUID categoryId, Category category);
    void deleteCategory(UUID categoryId);
}
