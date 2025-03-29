package com.dev.ecom_platform_2.service.impl;

import com.dev.ecom_platform_2.domain.dtos.CategoryListResponseDto;
import com.dev.ecom_platform_2.domain.dtos.CategoryRequestDto;
import com.dev.ecom_platform_2.domain.dtos.CategoryResponseDto;
import com.dev.ecom_platform_2.domain.entities.Category;
import com.dev.ecom_platform_2.exception.ResourceNotFoundException;
import com.dev.ecom_platform_2.mapper.CategoryMapper;
import com.dev.ecom_platform_2.repositories.CategoryRepository;
import com.dev.ecom_platform_2.service.CategoryService;
import com.dev.ecom_platform_2.utilities.Utility;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    // CREATE
    @Override
    public CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto) {
        var categoryToBeSaved = categoryMapper.fromDto(categoryRequestDto);

        if (categoryToBeSaved.getId() != null) {
            throw new IllegalArgumentException("Invalid category!");
        }

        String categoryName = categoryToBeSaved.getName();
        if (categoryRepository.existsByNameIgnoreCase(categoryName)) {
            throw new IllegalArgumentException("Category already exists with name: " + categoryName);
        }

        var savedCategory = categoryRepository.save(categoryToBeSaved);

        return categoryMapper.toDto(savedCategory);
    }

    // READ
    @Override
    public Category findCategoryById(UUID categoryId) {

        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + categoryId));
    }

    @Override
    public CategoryListResponseDto getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortDirection) {
        var pageable = Utility.createPageableWithValidation(Category.class, pageNumber, pageSize, sortBy, sortDirection);
        var categoryPage = categoryRepository.findAll(pageable);
        var categories = categoryPage.getContent();
        var categoriesResponseDto = categories.stream().map(categoryMapper::toDto).toList();

        return CategoryListResponseDto.builder()
                .content(categoriesResponseDto)
                .pageNumber(categoryPage.getNumber())
                .pageSize(categoryPage.getSize())
                .totalElements(categoryPage.getTotalElements())
                .totalPages(categoryPage.getTotalPages())
                .lastPage(categoryPage.isLast())
                .build();
    }

    // UPDATE
    @Override
    public CategoryResponseDto updateCategory(UUID categoryId, CategoryRequestDto categoryRequestDto) {
        var categoryToUpdate = categoryMapper.fromDto(categoryRequestDto);

        if (categoryToUpdate.getId() == null) {
            throw new IllegalArgumentException("Category must have an ID!");
        }
        if (!Objects.equals(categoryId, categoryToUpdate.getId())) {
            throw new IllegalArgumentException("Operation not allowed!");
        }
        if (categoryRepository.existsByNameIgnoreCase(categoryToUpdate.getName())) {
            throw new IllegalArgumentException("Category already exists with name: " + categoryToUpdate.getName());
        }

        Category existingCategory = findCategoryById(categoryId);
        existingCategory.setName(categoryToUpdate.getName());
        var updatedCategory = categoryRepository.save(existingCategory);

        return categoryMapper.toDto(updatedCategory);
    }

    // DELETE
    @Override
    public void deleteCategory(UUID categoryId) {
        Category existingCategory = findCategoryById(categoryId);

        categoryRepository.delete(existingCategory);
    }
}
