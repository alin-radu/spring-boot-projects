package com.dev.ecom_platform_2.controller;

import com.dev.ecom_platform_2.config.AppConstants;
import com.dev.ecom_platform_2.domain.dtos.CategoryListDto;
import com.dev.ecom_platform_2.domain.dtos.CategoryRequest;
import com.dev.ecom_platform_2.domain.dtos.CategoryDto;
import com.dev.ecom_platform_2.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // CREATE
    @PostMapping("/admin/categories")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        var savedCategory = categoryService.createCategory(categoryRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }

    // READ
    @GetMapping("/public/categories")
    public ResponseEntity<CategoryListDto> getAllCategories(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer page,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer limit,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY_ID) String sortBy,
            @RequestParam(name = "sortDirection", defaultValue = AppConstants.SORT_DIRECTION_ASC) String sortDirection
    ) {
        var categoryListResponseDto = categoryService.getAllCategories(page, limit, sortBy, sortDirection);

        return ResponseEntity.status(HttpStatus.OK).body(categoryListResponseDto);
    }

    // UPDATE
    @PutMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable UUID categoryId, @Valid @RequestBody CategoryRequest categoryRequest) {
        var updatedCategory = categoryService.updateCategory(categoryId, categoryRequest);

        return ResponseEntity.status(HttpStatus.OK).body(updatedCategory);
    }

    // DELETE
    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID categoryId) {
        categoryService.deleteCategory(categoryId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
