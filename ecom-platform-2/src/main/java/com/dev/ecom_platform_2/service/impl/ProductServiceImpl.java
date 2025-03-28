package com.dev.ecom_platform_2.service.impl;

import com.dev.ecom_platform_2.domain.dtos.ProductListResponseDto;
import com.dev.ecom_platform_2.domain.dtos.ProductRequestDto;
import com.dev.ecom_platform_2.domain.dtos.ProductResponseDto;
import com.dev.ecom_platform_2.domain.entities.Product;
import com.dev.ecom_platform_2.exception.ResourceNotFoundException;
import com.dev.ecom_platform_2.mapper.ProductMapper;
import com.dev.ecom_platform_2.repositories.CategoryRepository;
import com.dev.ecom_platform_2.repositories.ProductRepository;
import com.dev.ecom_platform_2.service.ProductService;
import com.dev.ecom_platform_2.utilities.Utility;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    public ProductServiceImpl(CategoryRepository categoryRepository, ProductRepository productRepository, ProductMapper productMapper) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    // CREATE
    @Override
    public ProductResponseDto createProduct(UUID categoryId, ProductRequestDto productRequestDto) {
        var productToBeSaved = productMapper.fromDto(productRequestDto);

        if (productToBeSaved.getId() != null) {
            throw new IllegalArgumentException("Invalid request arguments!");
        }
        var category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with the id " + categoryId + " ."));

        productToBeSaved.setImage("default.png");
        productToBeSaved.setCategory(category);
        double specialPrice = productRequestDto.getPrice() -
                ((productRequestDto.getDiscount() * 0.01) * productRequestDto.getPrice());
        productToBeSaved.setSpecialPrice(specialPrice);
        var savedProduct = productRepository.save(productToBeSaved);

        return productMapper.toDto(savedProduct);
    }

    // READ
    @Override
    public ProductListResponseDto getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortDirection) {
        var pageable = Utility.createPageableWithValidation(Product.class, pageNumber, pageSize, sortBy, sortDirection);
        var productPage = productRepository.findAll(pageable);
        var products = productPage.getContent();
        var productsResponseDto = products.stream().map(productMapper::toDto).toList();

        return ProductListResponseDto.builder()
                .content(productsResponseDto)
                .pageNumber(productPage.getNumber())
                .pageSize(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .lastPage(productPage.isLast())
                .build();
    }
}
