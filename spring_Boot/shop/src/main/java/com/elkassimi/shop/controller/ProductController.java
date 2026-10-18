package com.elkassimi.shop.controller;

import com.elkassimi.shop.dtos.requestDtos.ProductRequestDto;
import com.elkassimi.shop.dtos.requestDtos.ProductUpdateRequestDto;
import com.elkassimi.shop.dtos.responseDtos.ProductResponseDto;
import com.elkassimi.shop.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponseDto> createProduct(
            @RequestPart("product") @Valid ProductRequestDto productRequest,
            @RequestPart(value = "photo", required = false) MultipartFile photo) {
        log.info("REST request to create product: {}", productRequest.name());
        ProductResponseDto response = productService.createProduct(productRequest, photo);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        log.info("REST request to get product by id: {}", id);
        ProductResponseDto response = productService.getProductById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        log.info("REST request to get all products");
        List<ProductResponseDto> response = productService.getAllProducts();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<ProductResponseDto>> getAllProductsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "productId") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        log.info("REST request to get products paginated");
        Page<ProductResponseDto> response = productService.getAllProductsPaginated(page, size, sortBy, sortDir);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{categoryName}")
    public ResponseEntity<List<ProductResponseDto>> getProductsByCategory(@PathVariable String categoryName) {
        log.info("REST request to get products by category: {}", categoryName);
        List<ProductResponseDto> response = productService.getProductsByCategory(categoryName);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/available")
    public ResponseEntity<List<ProductResponseDto>> getAvailableProducts() {
        log.info("REST request to get available products");
        List<ProductResponseDto> response = productService.getAvailableProducts();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<ProductResponseDto>> getProductsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        log.info("REST request to get products by price range: {} - {}", minPrice, maxPrice);
        List<ProductResponseDto> response = productService.getProductsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponseDto>> searchProductsByName(@RequestParam String keyword) {
        log.info("REST request to search products by keyword: {}", keyword);
        List<ProductResponseDto> response = productService.searchProductsByName(keyword);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/discount")
    public ResponseEntity<List<ProductResponseDto>> getProductsWithDiscount() {
        log.info("REST request to get products with discount");
        List<ProductResponseDto> response = productService.getProductsWithDiscount();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/average-price")
    public ResponseEntity<BigDecimal> getAverageProductPrice() {
        log.info("REST request to get average product price");
        BigDecimal average = productService.getAverageProductPrice();
        return ResponseEntity.ok(average);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getTotalProductsCount() {
        log.info("REST request to get total products count");
        Long count = productService.getTotalProductsCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductResponseDto>> getLowStockProducts(
            @RequestParam(defaultValue = "10") Long threshold) {
        log.info("REST request to get low stock products (threshold: {})", threshold);
        List<ProductResponseDto> response = productService.getLowStockProducts(threshold);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/price-after-discount")
    public ResponseEntity<BigDecimal> getPriceAfterDiscount(@PathVariable Long id) {
        log.info("REST request to get price after discount for product: {}", id);
        BigDecimal price = productService.calculatePriceAfterDiscount(id);
        return ResponseEntity.ok(price);
    }

    @GetMapping("/{id}/in-stock")
    public ResponseEntity<Boolean> isProductInStock(
            @PathVariable Long id,
            @RequestParam Long quantity) {
        log.info("REST request to check if product {} has {} in stock", id, quantity);
        Boolean inStock = productService.isProductInStock(id, quantity);
        return ResponseEntity.ok(inStock);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponseDto> updateProduct(
            @PathVariable Long id,
            @RequestPart("product") @Valid ProductUpdateRequestDto updateRequest,
            @RequestPart(value = "photo", required = false) MultipartFile photo) {
        log.info("REST request to update product: {}", id);
        ProductResponseDto response = productService.updateProduct(id, updateRequest, photo);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/availability")
    public ResponseEntity<ProductResponseDto> updateProductAvailability(
            @PathVariable Long id,
            @RequestParam Boolean isAvailable) {
        log.info("REST request to update product availability: {} -> {}", id, isAvailable);
        ProductResponseDto response = productService.updateProductAvailability(id, isAvailable);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<ProductResponseDto> updateProductStock(
            @PathVariable Long id,
            @RequestParam Long quantityToAdd) {
        log.info("REST request to update product stock: {} add {}", id, quantityToAdd);
        ProductResponseDto response = productService.updateProductStock(id, quantityToAdd);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/discount")
    public ResponseEntity<ProductResponseDto> applyDiscountToProduct(
            @PathVariable Long id,
            @RequestParam Double discountPercentage) {
        log.info("REST request to apply {}% discount to product: {}", discountPercentage, id);
        ProductResponseDto response = productService.applyDiscountToProduct(id, discountPercentage);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/reduce-stock")
    public ResponseEntity<Void> reduceProductStock(
            @PathVariable Long id,
            @RequestParam Long quantity) {
        log.info("REST request to reduce product stock: {} by {}", id, quantity);
        productService.reduceProductStock(id, quantity);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/bulk-discount")
    public ResponseEntity<Void> bulkUpdateDiscount(
            @RequestParam List<Long> productIds,
            @RequestParam Double discountPercentage) {
        log.info("REST request to bulk update discount for {} products", productIds.size());
        productService.bulkUpdateDiscount(productIds, discountPercentage);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("REST request to delete product: {}", id);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/category/{categoryName}")
    public ResponseEntity<Void> deleteAllProductsInCategory(@PathVariable String categoryName) {
        log.info("REST request to delete all products in category: {}", categoryName);
        productService.deleteAllProductsInCategory(categoryName);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/price-range")
    public ResponseEntity<Void> deleteProductsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        log.info("REST request to delete products by price range: {} - {}", minPrice, maxPrice);
        productService.deleteProductsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.noContent().build();
    }
}
