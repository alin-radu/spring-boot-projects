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
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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
                .orElseThrow(() -> new ResourceNotFoundException(String.format("%s with the id %s not found.", "Category", categoryId)));

        productToBeSaved.setImage("default.png");
        productToBeSaved.setCategory(category);
        double specialPrice = calculateSpecialPrice(productRequestDto.getPrice(), productRequestDto.getDiscount());
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

        return getProductListResponseDto(productsResponseDto, productPage);
    }

    @Override
    public ProductListResponseDto getProductsByCategoryId(
            UUID categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortDirection) {

        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException(String.format("%s with the id %s not found.", "Category", categoryId));
        }

        var pageable = Utility.createPageableWithValidation(Product.class, pageNumber, pageSize, sortBy, sortDirection);
        var productPage = productRepository.findAllByCategoryId(categoryId, pageable);
        var products = productPage.getContent();
        var productsResponseDto = products.stream().map(productMapper::toDto).toList();

        return getProductListResponseDto(productsResponseDto, productPage);
    }

    @Override
    public ProductListResponseDto getProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortDirection) {
        var pageable = Utility.createPageableWithValidation(Product.class, pageNumber, pageSize, sortBy, sortDirection);
        var productPage = productRepository.findAllByNameContainingIgnoreCase(keyword, pageable);
        var products = productPage.getContent();
        var productsResponseDto = products.stream().map(productMapper::toDto).toList();

        return getProductListResponseDto(productsResponseDto, productPage);
    }

    // UPDATE
    @Override
    public ProductResponseDto updateProduct(UUID productId, ProductRequestDto productRequestDto) {
        var productToBeUpdated = productMapper.fromDto(productRequestDto);

        if (productToBeUpdated.getId() == null) {
            throw new IllegalArgumentException("Product must have an ID!");
        }
        if (!Objects.equals(productId, productToBeUpdated.getId())) {
            throw new IllegalArgumentException("Operation not allowed!");
        }

        var existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("%s with the id %s not found.", "Product", productId)));

        existingProduct.setName(productToBeUpdated.getName());
        existingProduct.setDescription(productToBeUpdated.getDescription());
        existingProduct.setQuantity(productToBeUpdated.getQuantity());
        existingProduct.setPrice(productToBeUpdated.getPrice());
        existingProduct.setDiscount(productToBeUpdated.getDiscount());
        var specialPrice = calculateSpecialPrice(productToBeUpdated.getPrice(), productToBeUpdated.getDiscount());
        existingProduct.setSpecialPrice(specialPrice);

        productRepository.save(existingProduct);

        return productMapper.toDto(existingProduct);
    }

    // HELPERS
    private static ProductListResponseDto getProductListResponseDto(List<ProductResponseDto> productsResponseDto, Page<Product> productPage) {
        return ProductListResponseDto.builder()
                .content(productsResponseDto)
                .pageNumber(productPage.getNumber())
                .pageSize(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .lastPage(productPage.isLast())
                .build();
    }

    private double calculateSpecialPrice(double actualPrice, double discount) {
        return actualPrice - ((discount * 0.01) * actualPrice);
    }
}
