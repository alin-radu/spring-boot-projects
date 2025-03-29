package com.dev.ecom_platform_2.controller;

import com.dev.ecom_platform_2.config.AppConstants;
import com.dev.ecom_platform_2.domain.dtos.ProductListResponseDto;
import com.dev.ecom_platform_2.domain.dtos.ProductRequestDto;
import com.dev.ecom_platform_2.domain.dtos.ProductResponseDto;
import com.dev.ecom_platform_2.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class ProductController {

    private final ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // CREATE
    @PostMapping("/admin/categories/{categoryId}/products")
    public ResponseEntity<ProductResponseDto> createProduct(
            @PathVariable UUID categoryId,
            @Valid @RequestBody ProductRequestDto productRequestDto) {

        var savedProduct = productService.createProduct(categoryId, productRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    // READ
    @GetMapping("/public/products")
    public ResponseEntity<ProductListResponseDto> getAllProducts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer page,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer limit,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY_ID) String sortBy,
            @RequestParam(name = "sortDirection", defaultValue = AppConstants.SORT_DIRECTION_ASC) String sortDirection
    ) {

        var productListResponseDto = productService.getAllProducts(page, limit, sortBy, sortDirection);

        return ResponseEntity.status(HttpStatus.OK).body(productListResponseDto);
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductListResponseDto> getProductsByCategory(
            @PathVariable UUID categoryId,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer page,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer limit,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY_ID) String sortBy,
            @RequestParam(name = "sortDirection", defaultValue = AppConstants.SORT_DIRECTION_ASC) String sortDirection) {

        var productListResponseDto = productService.getProductsByCategoryId(categoryId, page, limit, sortBy, sortDirection);

        return ResponseEntity.status(HttpStatus.OK).body(productListResponseDto);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductListResponseDto> getProductsByKeyword(
            @PathVariable String keyword,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer page,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer limit,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY_ID) String sortBy,
            @RequestParam(name = "sortDirection", defaultValue = AppConstants.SORT_DIRECTION_ASC) String sortDirection) {

        var productListResponseDto = productService.getProductsByKeyword(keyword, page, limit, sortBy, sortDirection);

        return ResponseEntity.status(HttpStatus.OK).body(productListResponseDto);
    }

    // UPDATE
    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductResponseDto> updateProduct(
            @PathVariable UUID productId,
            @Valid @RequestBody ProductRequestDto productRequestDto
    ) {
        var productResponseDto = productService.updateProduct(productId, productRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(productResponseDto);
    }
}
