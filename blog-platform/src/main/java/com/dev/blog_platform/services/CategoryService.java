package com.dev.blog_platform.services;

import com.dev.blog_platform.domain.entities.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    Category createCategory(Category category);
    List<Category> findAllCategoriesWithPostCount();
    Category findCategoryById(UUID categoryId);
    void deleteCategory(UUID categoryId);
}
