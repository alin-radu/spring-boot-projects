package com.dev.blog_platform.controllers;

import com.dev.blog_platform.domain.dtos.CategoryResponseDto;
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
    public ResponseEntity<List<CategoryResponseDto>> findAllCategories() {
        List<CategoryResponseDto> categoriesResponseDto =
                categoryService.findAllCategoriesWithPostCount().stream()
                        .map(categoryMapper::toCategoryResponseDto)
                        .toList();

        return ResponseEntity.ok(categoriesResponseDto);

    }

    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(
            @Valid @RequestBody CreateCategoryRequestDto createCategoryRequestDto) {
        Category categoryToCreate = categoryMapper.toCategoryEntity(createCategoryRequestDto);
        Category savedCategory = categoryService.createCategory(categoryToCreate);

        return new ResponseEntity<>(
                categoryMapper.toCategoryResponseDto(savedCategory),
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
