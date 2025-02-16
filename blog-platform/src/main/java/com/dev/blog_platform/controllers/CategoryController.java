package com.dev.blog_platform.controllers;

import com.dev.blog_platform.domain.dtos.CategoryDto;
import com.dev.blog_platform.domain.dtos.CreateCategoryRequestDto;
import com.dev.blog_platform.domain.entities.Category;
import com.dev.blog_platform.mappers.CategoryMapper;
import com.dev.blog_platform.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> findAllCategories() {
        List<CategoryDto> categoriesDto = categoryService.findAllCategoriesWithPostCount().stream()
                .map(categoryMapper::toDto)
                .toList();

        return ResponseEntity.ok(categoriesDto);

    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(
            @Valid @RequestBody CreateCategoryRequestDto createCategoryRequestDto) {
        Category categoryToCreate = categoryMapper.toEntity(createCategoryRequestDto);
        Category savedCategory = categoryService.createCategory(categoryToCreate);

        return new ResponseEntity<>(
                categoryMapper.toDto(savedCategory),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping(path = "/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID categoryId) {
        categoryService.deleteCategory(categoryId);

        return new ResponseEntity<>(
                HttpStatus.NO_CONTENT
        );
    }
}
