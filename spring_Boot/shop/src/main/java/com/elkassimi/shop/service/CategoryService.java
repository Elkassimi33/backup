package com.elkassimi.shop.service;

import com.elkassimi.shop.dtos.requestDtos.CategoryRequest;
import com.elkassimi.shop.dtos.responseDtos.CategoryResponseDto;
import com.elkassimi.shop.exception.CategoryHasProductsException;
import com.elkassimi.shop.exception.CategoryNotFoundException;
import com.elkassimi.shop.exception.DuplicateCategoryException;
import com.elkassimi.shop.exception.EmptyCategoryListException;
import com.elkassimi.shop.model.Category;
import com.elkassimi.shop.model.Product;
import com.elkassimi.shop.repository.CategoryRepository;
import com.elkassimi.shop.repository.ProductRepository;
import com.elkassimi.shop.utils.UploadFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final UploadFileService fileService;

    private static final String pathCompletion = "categories";

    @Value("${file.baseUrl}")
    private   String baseUrl;

    // ==================== CREATE ====================

    public CategoryResponseDto createCategory(CategoryRequest categoryRequest, MultipartFile photo) {
        log.info("Creating new category: {}", categoryRequest.name());

        // Check if category already exists
        if (categoryRepository.existsByName(categoryRequest.name())) {
            throw new DuplicateCategoryException("Category already exists with name: " + categoryRequest.name());
        }

        Category category = Category.builder()
                .name(categoryRequest.name())
                .description(categoryRequest.description())
                .build();

        Category savedCategory = categoryRepository.save(category);

        if (photo != null && !photo.isEmpty()) {
            String fileName = fileService.storeMultipartFile(photo, savedCategory.getCategoryId(), pathCompletion);
            savedCategory.setImageUrl(fileName);
            savedCategory = categoryRepository.save(savedCategory);
        }

        log.info("Category created successfully with id: {}", savedCategory.getCategoryId());
        return toCategoryResponse(savedCategory);
    }

    // ==================== READ ====================

    public CategoryResponseDto getCategoryById(Long categoryId) {
        log.info("Fetching category with id: {}", categoryId);
        Category category = findCategoryOrThrow(categoryId);
        return toCategoryResponse(category);
    }

    public CategoryResponseDto getCategoryByName(String name) {
        log.info("Fetching category by name: {}", name);
        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with name: " + name));
        return toCategoryResponse(category);
    }

    public List<CategoryResponseDto> getAllCategories() {
        log.info("Fetching all categories");
        List<Category> categories = categoryRepository.findAll();

        if (categories.isEmpty()) {
            throw new EmptyCategoryListException("No categories found");
        }

        return categories.stream()
                .map(this::toCategoryResponse)
                .collect(Collectors.toList());
    }

    public Page<CategoryResponseDto> getAllCategoriesPaginated(int page, int size, String sortBy, String sortDir) {
        log.info("Fetching categories with pagination - page: {}, size: {}, sortBy: {}, sortDir: {}",
                page, size, sortBy, sortDir);

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return categoryRepository.findAll(pageable)
                .map(this::toCategoryResponse);
    }

    public List<CategoryResponseDto> searchCategoriesByName(String keyword) {
        log.info("Searching categories by name keyword: {}", keyword);
        List<Category> categories = categoryRepository.findByNameContainingIgnoreCase(keyword);

        if (categories.isEmpty()) {
            throw new CategoryNotFoundException("No categories found with name containing: " + keyword);
        }

        return categories.stream()
                .map(this::toCategoryResponse)
                .collect(Collectors.toList());
    }

    public Long getTotalCategoriesCount() {
        log.info("Getting total categories count");
        return categoryRepository.count();
    }

    public boolean categoryExists(String categoryName) {
        log.info("Checking if category exists: {}", categoryName);
        return categoryRepository.existsByName(categoryName);
    }

    // ==================== UPDATE ====================

    public CategoryResponseDto updateCategory(Long categoryId, CategoryRequest categoryRequest, MultipartFile photo) {
        log.info("Updating category with id: {}", categoryId);

        Category category = findCategoryOrThrow(categoryId);

        if (categoryRequest.name() != null && !categoryRequest.name().equals(category.getName())) {
            if (categoryRepository.existsByName(categoryRequest.name())) {
                throw new DuplicateCategoryException("Category already exists with name: " + categoryRequest.name());
            }
            category.setName(categoryRequest.name());
        }

        if (categoryRequest.description() != null) {
            category.setDescription(categoryRequest.description());
        }

        if (photo != null && !photo.isEmpty()) {
            String fileName = fileService.storeMultipartFile(photo, category.getCategoryId(), pathCompletion);
            category.setImageUrl(fileName);
        }

        Category updatedCategory = categoryRepository.save(category);
        log.info("Category updated successfully with id: {}", updatedCategory.getCategoryId());

        return toCategoryResponse(updatedCategory);
    }

    public CategoryResponseDto updateCategoryImage(Long categoryId, MultipartFile photo) {
        log.info("Updating image for category with id: {}", categoryId);

        Category category = findCategoryOrThrow(categoryId);

        if (photo == null || photo.isEmpty()) {
            throw new IllegalArgumentException("Photo cannot be null or empty");
        }

        String fileName = fileService.storeMultipartFile(photo, category.getCategoryId(), pathCompletion);
        category.setImageUrl(fileName);

        Category updatedCategory = categoryRepository.save(category);
        log.info("Category image updated successfully for id: {}", categoryId);

        return toCategoryResponse(updatedCategory);
    }

    // ==================== DELETE ====================

    @Transactional
    public void deleteCategory(Long categoryId) {
        log.info("Deleting category with id: {}", categoryId);

        Category category = findCategoryOrThrow(categoryId);

        List<Product> products = productRepository.findByCategory(category);
        if (!products.isEmpty()) {
            log.warn("Category {} has {} products. Deleting category will also delete all associated products.",
                    category.getName(), products.size());

             throw new CategoryHasProductsException("Cannot delete category with existing " +
                     "Products count: " + products.size());
        }

        categoryRepository.delete(category);
        log.info("Category deleted successfully with id: {}", categoryId);
    }

    @Transactional
    public void deleteCategoryAndKeepProducts(Long categoryId, Long newCategoryId) {
        log.info("Deleting category {} and moving products to category {}", categoryId, newCategoryId);

        Category categoryToDelete = findCategoryOrThrow(categoryId);
        Category targetCategory = findCategoryOrThrow(newCategoryId);

        List<Product> products = productRepository.findByCategory(categoryToDelete);

        if (!products.isEmpty()) {
            for (Product product : products) {
                product.setCategory(targetCategory);
            }
            productRepository.saveAll(products);
            log.info("Moved {} products from category {} to category {}",
                    products.size(), categoryToDelete.getName(), targetCategory.getName());
        }

        categoryRepository.delete(categoryToDelete);
        log.info("Category deleted successfully: {}", categoryToDelete.getName());
    }

    @Transactional
    public void deleteAllCategories() {
        log.warn("Deleting all categories and their associated products");

        long categoryCount = categoryRepository.count();
        if (categoryCount == 0) {
            throw new EmptyCategoryListException("No categories to delete");
        }

        categoryRepository.deleteAll();
        log.info("Deleted all {} categories", categoryCount);
    }

    // ==================== BUSINESS LOGIC ====================

    public Long getProductCountByCategory(String  categoryName) {
        log.info("Getting product count for category id: {}", categoryName);
        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(()->new CategoryNotFoundException("Category not found with name: " + categoryName));
        return (long) productRepository.findByCategory(category).size();
    }

    public List<CategoryResponseDto> getCategoriesWithProducts() {
        log.info("Fetching categories that have at least one product");

        List<Category> allCategories = categoryRepository.findAll();

        List<Category> categoriesWithProducts = allCategories.stream()
                .filter(category -> !productRepository.findByCategory(category).isEmpty())
                .toList();

        if (categoriesWithProducts.isEmpty()) {
            throw new EmptyCategoryListException("No categories with products found");
        }

        return categoriesWithProducts.stream()
                .map(this::toCategoryResponse)
                .collect(Collectors.toList());
    }

    public List<CategoryResponseDto> getCategoriesWithoutProducts() {
        log.info("Fetching categories that have no products");

        List<Category> allCategories = categoryRepository.findAll();

        List<Category> categoriesWithoutProducts = allCategories.stream()
                .filter(category -> productRepository.findByCategory(category).isEmpty())
                .toList();

        return categoriesWithoutProducts.stream()
                .map(this::toCategoryResponse)
                .collect(Collectors.toList());
    }

    public CategoryResponseDto getCategoryWithMostProducts() {
        log.info("Fetching category with most products");

        List<Category> allCategories = categoryRepository.findAll();

        if (allCategories.isEmpty()) {
            throw new EmptyCategoryListException("No categories found");
        }

        Category categoryWithMostProducts = allCategories.stream()
                .max((c1, c2) -> {
                    int size1 = productRepository.findByCategory(c1).size();
                    int size2 = productRepository.findByCategory(c2).size();
                    return Integer.compare(size1, size2);
                })
                .orElseThrow(() -> new CategoryNotFoundException("No categories found"));

        log.info("Category with most products: {} ({} products)",
                categoryWithMostProducts.getName(),
                productRepository.findByCategory(categoryWithMostProducts).size());

        return toCategoryResponse(categoryWithMostProducts);
    }

    @Transactional
    public CategoryResponseDto mergeCategories(Long sourceCategoryId, Long targetCategoryId) {
        log.info("Merging category {} into category {}", sourceCategoryId, targetCategoryId);

        Category sourceCategory = findCategoryOrThrow(sourceCategoryId);
        Category targetCategory = findCategoryOrThrow(targetCategoryId);

        if (sourceCategoryId.equals(targetCategoryId)) {
            throw new IllegalArgumentException("Cannot merge a category with itself");
        }

        List<Product> sourceProducts = productRepository.findByCategory(sourceCategory);

        for (Product product : sourceProducts) {
            product.setCategory(targetCategory);
        }

        productRepository.saveAll(sourceProducts);
        log.info("Moved {} products from category '{}' to category '{}'",
                sourceProducts.size(), sourceCategory.getName(), targetCategory.getName());

        categoryRepository.delete(sourceCategory);
        log.info("Deleted source category: {}", sourceCategory.getName());

        return toCategoryResponse(targetCategory);
    }


    public void validateCategoryName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be null or empty");
        }

        if (name.length() > 100) {
            throw new IllegalArgumentException("Category name cannot exceed 100 characters");
        }
    }


    private Category findCategoryOrThrow(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + categoryId));
    }

    private CategoryResponseDto toCategoryResponse(Category category) {
        return new CategoryResponseDto(
                category.getCategoryId(),
                category.getName(),
                category.getDescription(),
                baseUrl+pathCompletion+"/"+category.getImageUrl());
    }
}
