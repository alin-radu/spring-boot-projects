package com.dev.ecom_platform_2.controller;

import com.dev.ecom_platform_2.domain.dtos.CategoryRequestDto;
import com.dev.ecom_platform_2.domain.dtos.CategoryResponseDto;
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

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // CREATE
    @PostMapping("/admin/categories")
    public ResponseEntity<CategoryResponseDto> createCategories(@Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        var savedCategory = categoryService.createCategory(categoryRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }

    // READ
    @GetMapping("/public/categories")
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        var categories = categoryService.getAllCategories();

        return ResponseEntity.status(HttpStatus.OK).body(categories);
    }

    // UPDATE
    @PutMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryResponseDto> updateCategory(@PathVariable UUID categoryId, @Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        var updatedCategory = categoryService.updateCategory(categoryId, categoryRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(updatedCategory);
    }

    // DELETE
    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID categoryId) {
        categoryService.deleteCategory(categoryId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
