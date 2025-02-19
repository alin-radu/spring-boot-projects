package com.dev.blog_platform.services.impl;

import com.dev.blog_platform.domain.entities.Category;
import com.dev.blog_platform.repositories.CategoryRepository;
import com.dev.blog_platform.services.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> findAllCategoriesWithPostCount() {
        return categoryRepository.findAllWithPostCount();
    }

    @Override
    @Transactional
    public Category createCategory(Category category) {
        String categoryName = category.getName();

        if (categoryRepository.existsByNameIgnoreCase(categoryName)) {
            throw new IllegalArgumentException("Category already exists with name: " + categoryName);
        }

        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(UUID categoryId) {

        // v1
        categoryRepository.findById(categoryId).ifPresent(tag -> {
                    if (!tag.getPosts().isEmpty()) {
                        throw new IllegalStateException("Category has posts associated with it.");
                    }
                    categoryRepository.deleteById(categoryId);
                }
        );

        // v2
//        Optional<Category> category = categoryRepository.findById(categoryId);
//        if (category.isPresent()) {
//            if (!category.get().getPosts().isEmpty()) {
//                throw new IllegalStateException("Category has posts associated with it.");
//            }
//
//            categoryRepository.deleteById(categoryId);
//        }
    }

    @Override
    public Category findCategoryById(UUID categoryId) {

        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id " + categoryId));
    }
}
