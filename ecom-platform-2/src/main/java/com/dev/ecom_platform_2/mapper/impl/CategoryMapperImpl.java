package com.dev.ecom_platform_2.mapper.impl;

import com.dev.ecom_platform_2.domain.dtos.CategoryRequest;
import com.dev.ecom_platform_2.domain.dtos.CategoryDto;
import com.dev.ecom_platform_2.domain.entities.Category;
import com.dev.ecom_platform_2.mapper.CategoryMapper;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public Category fromDto(CategoryRequest categoryRequest) {
        return new Category(
                categoryRequest.getId(),
                categoryRequest.getName()
        );
    }

    @Override
    public CategoryDto toDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName()
        );
    }
}
