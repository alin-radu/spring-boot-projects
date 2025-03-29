package com.dev.ecom_platform_2.service.impl;

import com.dev.ecom_platform_2.domain.dtos.ProductListResponseDto;
import com.dev.ecom_platform_2.domain.dtos.ProductRequestDto;
import com.dev.ecom_platform_2.domain.dtos.ProductResponseDto;
import com.dev.ecom_platform_2.domain.entities.Product;
import com.dev.ecom_platform_2.exception.ResourceNotFoundException;
import com.dev.ecom_platform_2.mapper.ProductMapper;
import com.dev.ecom_platform_2.repositories.CategoryRepository;
import com.dev.ecom_platform_2.repositories.ProductRepository;
import com.dev.ecom_platform_2.service.CategoryService;
import com.dev.ecom_platform_2.service.ProductService;
import com.dev.ecom_platform_2.utilities.Utility;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryService categoryService;
    public ProductServiceImpl(CategoryRepository categoryRepository, ProductRepository productRepository, ProductMapper productMapper, CategoryService categoryService) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.categoryService = categoryService;
    }

    // CREATE
    @Override
    public ProductResponseDto createProduct(UUID categoryId, ProductRequestDto productRequestDto) {

        var productToBeSaved = productMapper.fromDto(productRequestDto);

        if (productToBeSaved.getId() != null) {
            throw new IllegalArgumentException("Invalid request arguments!");
        }
        var category = categoryService.findCategoryById(categoryId);

        productToBeSaved.setImage("default.png");
        productToBeSaved.setCategory(category);
        double specialPrice = calculateSpecialPrice(productRequestDto.getPrice(), productRequestDto.getDiscount());
        productToBeSaved.setSpecialPrice(specialPrice);
        var savedProduct = productRepository.save(productToBeSaved);

        return productMapper.toDto(savedProduct);
    }
    @Override
    public Product findProductById(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("%s with the id %s not found.", "Product", productId)));
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

        var existingProduct = findProductById(productId);

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

    @Override
    public ProductResponseDto updateProductImage(UUID productId, MultipartFile image) {
        Product existingProduct = findProductById(productId);

        String path = "images/";
        String filename;
        try {
            filename = uploadImage(path, image, existingProduct.getId());
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

    private String uploadImage(String path, MultipartFile image, UUID productId) throws IOException {
        String originalFileName = image.getOriginalFilename();
        String extension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }

        var newFileName = UUID.randomUUID() + "-" + productId + extension;

        String filePath = path + File.separator + newFileName;

        File folder = new File(path);
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                throw new IOException(String.format("Failed to create directory: %s.", path));
            }
        }

        Files.copy(image.getInputStream(), Paths.get(filePath));

        return newFileName;
    }
}
