package com.elkassimi.shop.controller;

import com.elkassimi.shop.dtos.requestDtos.CategoryRequest;
import com.elkassimi.shop.dtos.responseDtos.CategoryResponseDto;
import com.elkassimi.shop.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CategoryResponseDto> createCategory(
            @RequestPart("category") @Valid CategoryRequest categoryRequest,
            @RequestPart(value = "photo", required = false) MultipartFile photo) {
        log.info("REST request to create category: {}", categoryRequest.name());
        CategoryResponseDto response = categoryService.createCategory(categoryRequest, photo);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable Long id) {
        log.info("REST request to get category by id: {}", id);
        CategoryResponseDto response = categoryService.getCategoryById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<CategoryResponseDto> getCategoryByName(@PathVariable String name) {
        log.info("REST request to get category by name: {}", name);
        CategoryResponseDto response = categoryService.getCategoryByName(name);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        log.info("REST request to get all categories");
        List<CategoryResponseDto> response = categoryService.getAllCategories();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<CategoryResponseDto>> getAllCategoriesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "categoryId") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        log.info("REST request to get categories paginated");
        Page<CategoryResponseDto> response = categoryService.getAllCategoriesPaginated(page, size, sortBy, sortDir);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<CategoryResponseDto>> searchCategoriesByName(@RequestParam String keyword) {
        log.info("REST request to search categories by keyword: {}", keyword);
        List<CategoryResponseDto> response = categoryService.searchCategoriesByName(keyword);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getTotalCategoriesCount() {
        log.info("REST request to get total categories count");
        Long count = categoryService.getTotalCategoriesCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/exists/{name}")
    public ResponseEntity<Boolean> categoryExists(@PathVariable String name) {
        log.info("REST request to check if category exists: {}", name);
        Boolean exists = categoryService.categoryExists(name);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/with-products")
    public ResponseEntity<List<CategoryResponseDto>> getCategoriesWithProducts() {
        log.info("REST request to get categories with products");
        List<CategoryResponseDto> response = categoryService.getCategoriesWithProducts();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/without-products")
    public ResponseEntity<List<CategoryResponseDto>> getCategoriesWithoutProducts() {
        log.info("REST request to get categories without products");
        List<CategoryResponseDto> response = categoryService.getCategoriesWithoutProducts();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/most-products")
    public ResponseEntity<CategoryResponseDto> getCategoryWithMostProducts() {
        log.info("REST request to get category with most products");
        CategoryResponseDto response = categoryService.getCategoryWithMostProducts();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{categoryName}/product-count")
    public ResponseEntity<Long> getProductCountByCategory(@PathVariable String categoryName) {
        log.info("REST request to get product count for category: {}", categoryName);
        Long count = categoryService.getProductCountByCategory(categoryName);
        return ResponseEntity.ok(count);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CategoryResponseDto> updateCategory(
            @PathVariable Long id,
            @RequestPart("category") @Valid CategoryRequest categoryRequest,
            @RequestPart(value = "photo", required = false) MultipartFile photo) {
        log.info("REST request to update category: {}", id);
        CategoryResponseDto response = categoryService.updateCategory(id, categoryRequest, photo);
        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CategoryResponseDto> updateCategoryImage(
            @PathVariable Long id,
            @RequestPart("photo") MultipartFile photo) {
        log.info("REST request to update category image: {}", id);
        CategoryResponseDto response = categoryService.updateCategoryImage(id, photo);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        log.info("REST request to delete category: {}", id);
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/move/{newId}")
    public ResponseEntity<Void> deleteCategoryAndKeepProducts(
            @PathVariable Long id,
            @PathVariable Long newId) {
        log.info("REST request to delete category {} and move products to {}", id, newId);
        categoryService.deleteCategoryAndKeepProducts(id, newId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllCategories() {
        log.info("REST request to delete all categories");
        categoryService.deleteAllCategories();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/merge/{sourceId}/{targetId}")
    public ResponseEntity<CategoryResponseDto> mergeCategories(
            @PathVariable Long sourceId,
            @PathVariable Long targetId) {
        log.info("REST request to merge category {} into {}", sourceId, targetId);
        CategoryResponseDto response = categoryService.mergeCategories(sourceId, targetId);
        return ResponseEntity.ok(response);
    }
}
