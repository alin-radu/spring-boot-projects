package com.dev.blog_platform.services;

import com.dev.blog_platform.domain.entities.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    List<Category> findAllCategoriesWithPostCount();
    Category createCategory(Category category);
    void deleteCategory(UUID categoryId);
    Category findCategoryById(UUID categoryId);
}
