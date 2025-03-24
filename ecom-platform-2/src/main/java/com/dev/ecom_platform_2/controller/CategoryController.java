package com.dev.ecom_platform_2.controller;

import com.dev.ecom_platform_2.domain.dtos.CategoryRequestDto;
import com.dev.ecom_platform_2.domain.dtos.CategoryResponseDto;
import com.dev.ecom_platform_2.mapper.CategoryMapper;
import com.dev.ecom_platform_2.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    public CategoryController(CategoryService categoryService, CategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    // CREATE
    @PostMapping("/admin/categories")
    public ResponseEntity<CategoryResponseDto> createCategories(@Valid @RequestBody CategoryRequestDto categoryRequestDto) {

        var category = categoryMapper.fromDto(categoryRequestDto);
        var savedCategory = categoryService.createCategory(category);
        var savedCategoryDto = categoryMapper.toDto(savedCategory);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategoryDto);
    }

    // READ
    @GetMapping("/public/categories")
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {

        var categories = categoryService.getAllCategories();
        var categoriesDto = categories.stream().map(categoryMapper::toDto).toList();

        return ResponseEntity.status(HttpStatus.OK).body(categoriesDto);
    }

    // UPDATE
    @PutMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryResponseDto> updateCategory(@PathVariable UUID categoryId, @Valid @RequestBody CategoryRequestDto categoryRequestDto) {

        var categoryToUpdate = categoryMapper.fromDto(categoryRequestDto);
        var updatedCategory = categoryService.updateCategory(categoryId, categoryToUpdate);
        var updatedCategoryDto = categoryMapper.toDto(updatedCategory);

        return ResponseEntity.status(HttpStatus.OK).body(updatedCategoryDto);
    }

    // DELETE
    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID categoryId) {

        categoryService.deleteCategory(categoryId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
