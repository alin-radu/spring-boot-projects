package com.dev.ecom_platform_2.controller;

import com.dev.ecom_platform_2.config.AppConstants;
import com.dev.ecom_platform_2.domain.dtos.ProductListDto;
import com.dev.ecom_platform_2.domain.dtos.ProductRequest;
import com.dev.ecom_platform_2.domain.dtos.ProductDto;
import com.dev.ecom_platform_2.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<ProductDto> createProduct(
            @PathVariable UUID categoryId,
            @Valid @RequestBody ProductRequest productRequest) {

        var savedProduct = productService.createProduct(categoryId, productRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    // READ
    @GetMapping("/public/products")
    public ResponseEntity<ProductListDto> getAllProducts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer page,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer limit,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY_ID) String sortBy,
            @RequestParam(name = "sortDirection", defaultValue = AppConstants.SORT_DIRECTION_ASC) String sortDirection
    ) {

        var productListResponseDto = productService.getAllProducts(page, limit, sortBy, sortDirection);

        return ResponseEntity.status(HttpStatus.OK).body(productListResponseDto);
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductListDto> getProductsByCategory(
            @PathVariable UUID categoryId,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer page,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer limit,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_BY_ID) String sortBy,
            @RequestParam(name = "sortDirection", defaultValue = AppConstants.SORT_DIRECTION_ASC) String sortDirection) {

        var productListResponseDto = productService.getProductsByCategoryId(categoryId, page, limit, sortBy, sortDirection);

        return ResponseEntity.status(HttpStatus.OK).body(productListResponseDto);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductListDto> getProductsByKeyword(
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
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable UUID productId,
            @Valid @RequestBody ProductRequest productRequest
    ) {
        var productResponseDto = productService.updateProduct(productId, productRequest);

        return ResponseEntity.status(HttpStatus.OK).body(productResponseDto);
    }

    @PutMapping("/admin/products/{productId}/image")
    public ResponseEntity<ProductDto> updateImage(
            @PathVariable UUID productId,
            @RequestParam("image") MultipartFile image
    ) {
        var productResponseDto = productService.updateProductImage(productId, image);

        return ResponseEntity.status(HttpStatus.OK).body(productResponseDto);
    }

    // Delete
    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID productId) {
        productService.deleteProductById(productId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
