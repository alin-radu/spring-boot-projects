package com.dev.ecom_platform_2.service.impl;

import com.dev.ecom_platform_2.config.ProjectSettings;
import com.dev.ecom_platform_2.domain.dtos.ProductListDto;
import com.dev.ecom_platform_2.domain.dtos.ProductRequest;
import com.dev.ecom_platform_2.domain.dtos.ProductDto;
import com.dev.ecom_platform_2.domain.entities.Product;
import com.dev.ecom_platform_2.exception.ResourceNotFoundException;
import com.dev.ecom_platform_2.mapper.ProductMapper;
import com.dev.ecom_platform_2.repositories.CategoryRepository;
import com.dev.ecom_platform_2.repositories.ProductRepository;
import com.dev.ecom_platform_2.service.CategoryService;
import com.dev.ecom_platform_2.service.FileService;
import com.dev.ecom_platform_2.service.ProductService;
import com.dev.ecom_platform_2.util.PaginationUtil;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    private final CategoryService categoryService;
    private final FileService fileService;

    private final ProjectSettings projectSettings;
    private final ProductMapper productMapper;

    public ProductServiceImpl(CategoryRepository categoryRepository, ProductRepository productRepository, CategoryService categoryService, ProductMapper productMapper, FileService fileService, ProjectSettings projectSettings) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.productMapper = productMapper;
        this.fileService = fileService;
        this.projectSettings = projectSettings;
    }

    // CREATE
    @Override
    public ProductDto createProduct(UUID categoryId, ProductRequest productRequest) {

        System.out.println(productRequest);

        var productToBeSaved = productMapper.fromDto(productRequest);

        if (productToBeSaved.getId() != null) {
            throw new IllegalArgumentException("Invalid request arguments!");
        }
        var category = categoryService.findCategoryById(categoryId);

        productToBeSaved.setImage("default.png");
        productToBeSaved.setCategory(category);
        double specialPrice = calculateSpecialPrice(productRequest.getPrice(), productRequest.getDiscount());
        System.out.println(specialPrice);
        productToBeSaved.setSpecialPrice(specialPrice);
        var savedProduct = productRepository.save(productToBeSaved);

        return productMapper.toDto(savedProduct);
    }

    // READ
    private Product findProductById(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("%s with the id %s not found.", "Product", productId)));
    }

    @Override
    public ProductListDto getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortDirection) {

        var pageable = PaginationUtil.createPageableWithValidation(Product.class, pageNumber, pageSize, sortBy, sortDirection);
        var productPage = productRepository.findAll(pageable);
        var products = productPage.getContent();
        var productsResponseDto = products.stream().map(productMapper::toDto).toList();

        return getProductListResponseDto(productsResponseDto, productPage);
    }

    @Override
    public ProductListDto getProductsByCategoryId(
            UUID categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortDirection) {

        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException(String.format("%s with the id %s not found.", "Category", categoryId));
        }

        var pageable = PaginationUtil.createPageableWithValidation(Product.class, pageNumber, pageSize, sortBy, sortDirection);
        var productPage = productRepository.findAllByCategoryId(categoryId, pageable);
        var products = productPage.getContent();
        var productsResponseDto = products.stream().map(productMapper::toDto).toList();

        return getProductListResponseDto(productsResponseDto, productPage);
    }

    @Override
    public ProductListDto getProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortDirection) {
        var pageable = PaginationUtil.createPageableWithValidation(Product.class, pageNumber, pageSize, sortBy, sortDirection);
        var productPage = productRepository.findAllByNameContainingIgnoreCase(keyword, pageable);
        var products = productPage.getContent();
        var productsResponseDto = products.stream().map(productMapper::toDto).toList();

        return getProductListResponseDto(productsResponseDto, productPage);
    }

    // UPDATE
    @Override
    public ProductDto updateProduct(UUID productId, ProductRequest productRequest) {
        var productToBeUpdated = productMapper.fromDto(productRequest);

        if (productToBeUpdated.getId() == null) {
            throw new IllegalArgumentException("Product must have an ID!");
        }
        if (!Objects.equals(productId, productToBeUpdated.getId())) {
            throw new IllegalArgumentException("Operation not allowed!");
        }

        var productToBeSaved = Product.builder()
                .name(productToBeUpdated.getName())
                .description(productToBeUpdated.getDescription())
                .quantity(productToBeUpdated.getQuantity())
                .price(productToBeUpdated.getPrice())
                .discount(productToBeUpdated.getDiscount())
                .specialPrice(calculateSpecialPrice(productToBeUpdated.getPrice(), productToBeUpdated.getDiscount()))
                .build();

        productRepository.save(productToBeSaved);

        return productMapper.toDto(productToBeSaved);
    }

    @Override
    public ProductDto updateProductImage(UUID productId, MultipartFile image) {
        Product existingProduct = findProductById(productId);

        String filename;
        try {
            filename = fileService.uploadImage(projectSettings.getImage().getPath(), image, projectSettings.getImage().getAllowedTypes());
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong with the image upload.");
        }

        existingProduct.setImage(filename);
        var updatedProduct = productRepository.save(existingProduct);

        return productMapper.toDto(updatedProduct);
    }

    // DELETE
    @Override
    public void deleteProductById(UUID productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException(String.format("%s with the id %s not found.", "Product", productId));
        }

        productRepository.deleteById(productId);
    }

    // HELPERS
    private static ProductListDto getProductListResponseDto(List<ProductDto> productsResponseDto, Page<Product> productPage) {
        return ProductListDto.builder()
                .content(productsResponseDto)
                .pageNumber(productPage.getNumber())
                .pageSize(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .lastPage(productPage.isLast())
                .build();
    }

    private double calculateSpecialPrice(double actualPrice, double discount) {
        if (discount < 0 || discount > 100) {
            throw new IllegalArgumentException("Discount must be between 0 and 100");
        }
        return actualPrice * (1 - discount / 100);
    }
}
