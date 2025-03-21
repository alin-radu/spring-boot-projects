package com.dev.ecom_platform_2.controller;

import com.dev.ecom_platform_2.domain.dtos.CategoryDto;
import com.dev.ecom_platform_2.domain.entities.Category;
import com.dev.ecom_platform_2.mapper.CategoryMapper;
import com.dev.ecom_platform_2.service.CategoryService;
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
    public ResponseEntity<Category> createCategories(@RequestBody CategoryDto categoryDto) {

        Category category = categoryMapper.fromDto(categoryDto);
        Category savedCategory = categoryService.createCategory(category);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }

    // READ
    @GetMapping("/public/categories")
    public ResponseEntity<List<Category>> getAllCategories() {

        var categories = categoryService.getAllCategories();

        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }

    // UPDATE
    @PutMapping("/admin/categories/{categoryId}")
    public ResponseEntity<Category> updateCategory(@PathVariable UUID categoryId, @RequestBody CategoryDto categoryDto) {

        Category categoryToUpdate = categoryMapper.fromDto(categoryDto);
        Category updatedCategory = categoryService.updateCategory(categoryId, categoryToUpdate);

        return ResponseEntity.status(HttpStatus.OK).body(updatedCategory);
    }

    // DELETE
    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID categoryId) {

        categoryService.deleteCategory(categoryId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
