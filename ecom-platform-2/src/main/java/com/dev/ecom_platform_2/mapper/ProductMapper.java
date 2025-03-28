package com.dev.ecom_platform_2.mapper;

import com.dev.ecom_platform_2.domain.dtos.ProductRequestDto;
import com.dev.ecom_platform_2.domain.dtos.ProductResponseDto;
import com.dev.ecom_platform_2.domain.entities.Product;

public interface ProductMapper {
    Product fromDto(ProductRequestDto productRequestDto);
    ProductResponseDto toDto(Product product);
}
