package com.dev.ecom_platform.controller;

import com.dev.ecom_platform.model.Product;
import com.dev.ecom_platform.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController {

    private ProductService service;

    @Autowired
    public void setService(ProductService service) {
        this.service = service;
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        return new ResponseEntity<>(service.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable int productId) {
        Product product = service.getProductById(productId);

        if (product != null) {
            return new ResponseEntity<>(product, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/products")
    public ResponseEntity<?> addProduct(@RequestPart Product product, @RequestPart MultipartFile imageFile) {

        try {
            Product newProduct = service.addProduct(product, imageFile);
            return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("products/{productId}/image")
    public ResponseEntity<byte[]> getImageByProductId(@PathVariable int productId) {

        Product product = service.getProductById(productId);
        byte[] imageFile = product.getImageData();

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(product.getImageType()))
                .body(imageFile);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<?> updateProduct(
            @PathVariable int productId,
            @RequestPart Product product,
            @RequestPart MultipartFile imageFile
    ) {
        if (service.getProductById(productId) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            Product newProduct = service.updateProduct(product, imageFile);
            return new ResponseEntity<>(newProduct, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id) {
        Product product = service.getProductById(id);
        if (product != null) {
            service.deleteProduct(id);
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        } else
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);

    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
        List<Product> products = service.searchProducts(keyword);
        System.out.println("searching with " + keyword);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

}
