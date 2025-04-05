package com.dev.ecom_platform_2.service;

import com.dev.ecom_platform_2.domain.dtos.ProductListDto;
import com.dev.ecom_platform_2.domain.dtos.ProductRequest;
import com.dev.ecom_platform_2.domain.dtos.ProductDto;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface ProductService {
    ProductDto createProduct(UUID categoryId, ProductRequest productRequest);
    ProductListDto getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortDirection);
    ProductListDto getProductsByCategoryId(UUID categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortDirection);
    ProductListDto getProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortDirection);
    ProductDto updateProduct(UUID productId, @Valid ProductRequest productRequest);
    ProductDto updateProductImage(UUID productId, MultipartFile image);
    void deleteProductById(UUID productId);

}




