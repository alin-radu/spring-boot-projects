package com.dev.ecom_platform_2.mapper;

import com.dev.ecom_platform_2.domain.dtos.ProductRequest;
import com.dev.ecom_platform_2.domain.dtos.ProductDto;
import com.dev.ecom_platform_2.domain.entities.Product;

public interface ProductMapper {
    Product fromDto(ProductRequest productRequest);
    ProductDto toDto(Product product);
}
