package com.dev.ecom_platform_2.service;

import com.dev.ecom_platform_2.domain.dtos.ProductListResponseDto;
import com.dev.ecom_platform_2.domain.dtos.ProductRequestDto;
import com.dev.ecom_platform_2.domain.dtos.ProductResponseDto;
import jakarta.validation.Valid;

import java.util.UUID;

public interface ProductService {
    ProductResponseDto createProduct(UUID categoryId, ProductRequestDto productRequestDto);
    ProductListResponseDto getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortDirection);
    ProductListResponseDto getProductsByCategoryId(UUID categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortDirection);
    ProductListResponseDto getProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortDirection);
    ProductResponseDto updateProduct(UUID productId, @Valid ProductRequestDto productRequestDto);
}



