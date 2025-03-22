package com.dev.ecom_platform_2.service.impl;

import com.dev.ecom_platform_2.domain.entities.Category;
import com.dev.ecom_platform_2.exception.ResourceNotFoundException;
import com.dev.ecom_platform_2.repositories.CategoryRepository;
import com.dev.ecom_platform_2.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // CREATE
    @Override
    public Category createCategory(Category category) {
        if (category.getId() != null) {
            throw new IllegalArgumentException("Invalid category!");
        }

        String categoryName = category.getName();
        if (categoryRepository.existsByNameIgnoreCase(categoryName)) {
            throw new IllegalArgumentException("Category already exists with name: " + categoryName);
        }

        categoryRepository.save(category);

        return category;
    }

    // READ
    @Override
    public List<Category> getAllCategories() {

        return categoryRepository.findAll();
    }

    @Override
    public Category findCategoryById(UUID categoryId) {

        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + categoryId));
    }

    // UPDATE
    @Override
    public Category updateCategory(UUID categoryId, Category updatedCategoryRequest) {
        if (updatedCategoryRequest.getId() == null) {
            throw new IllegalArgumentException("Category must have an ID!");
        }
        if (!Objects.equals(categoryId, updatedCategoryRequest.getId())) {
            throw new IllegalArgumentException("Operation not allowed!");
        }
        if (categoryRepository.existsByNameIgnoreCase(updatedCategoryRequest.getName())) {
            throw new IllegalArgumentException("Category already exists with name: " + updatedCategoryRequest.getName());
        }

        Category existingCategory = findCategoryById(categoryId);

        existingCategory.setName(updatedCategoryRequest.getName());

        return categoryRepository.save(existingCategory);
    }

    // DELETE
    @Override
    public void deleteCategory(UUID categoryId) {
        Category existingCategory = findCategoryById(categoryId);

        categoryRepository.delete(existingCategory);
    }
}
