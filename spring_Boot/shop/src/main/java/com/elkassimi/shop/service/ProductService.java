package com.elkassimi.shop.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.elkassimi.shop.dtos.requestDtos.ProductRequestDto;
import com.elkassimi.shop.dtos.requestDtos.ProductUpdateRequestDto;
import com.elkassimi.shop.dtos.responseDtos.ProductResponseDto;
import com.elkassimi.shop.model.Category;
import com.elkassimi.shop.model.Product;
import com.elkassimi.shop.repository.CategoryRepository;
import com.elkassimi.shop.repository.ProductRepository;
import com.elkassimi.shop.utils.UploadFileService;
import com.elkassimi.shop.exception.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UploadFileService fileService;

    @Value("${file.baseUrl}")
    private   String baseUrl;

    private static final String pathCompletion = "products";

    // ==================== CREATE ====================

    public ProductResponseDto createProduct(ProductRequestDto productRequest, MultipartFile photo) {
        log.info("Creating new product: {}", productRequest.name());

        Product product = Product.builder()
                .name(productRequest.name())
                .price(productRequest.price())
                .quantity(productRequest.newQuantity())
                .description(productRequest.description())
                .category(findCategoryOrThrow(productRequest.categoryName()))
                .discount(productRequest.discount())
                .isDesponible(productRequest.isDesponible())
                .build();

        Product saved = productRepository.save(product);

        if (photo != null && !photo.isEmpty()) {
            String fileName = fileService.storeMultipartFile(photo, saved.getProductId(), pathCompletion);
            saved.setImageUrl(fileName);
            saved = productRepository.save(saved);
        }

        log.info("Product created successfully with id: {}", saved.getProductId());
        return toProductResponse(saved);
    }

    // ==================== READ ====================

    public ProductResponseDto getProductById(Long productId) {
        log.info("Fetching product with id: {}", productId);
        Product product = findProductOrThrow(productId);
        return toProductResponse(product);
    }

    public List<ProductResponseDto> getAllProducts() {
        log.info("Fetching all products");
        return productRepository.findAll()
                .stream()
                .map(this::toProductResponse)
                .collect(Collectors.toList());
    }

    public Page<ProductResponseDto> getAllProductsPaginated(int page, int size, String sortBy, String sortDir) {
        log.info("Fetching products with pagination - page: {}, size: {}, sortBy: {}, sortDir: {}",
                page, size, sortBy, sortDir);

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return productRepository.findAll(pageable)
                .map(this::toProductResponse);
    }

    public List<ProductResponseDto> getProductsByCategory(String categoryName) {
        log.info("Fetching products by category: {}", categoryName);
        Category category = findCategoryOrThrow(categoryName);

        return productRepository.findByCategory(category)
                .stream()
                .map(this::toProductResponse)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDto> getAvailableProducts() {
        log.info("Fetching all available products");
        return productRepository.findByIsDesponibleTrue()
                .stream()
                .map(this::toProductResponse)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDto> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        log.info("Fetching products by price range: {} - {}", minPrice, maxPrice);
        return productRepository.findByPriceBetween(minPrice, maxPrice)
                .stream()
                .map(this::toProductResponse)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDto> searchProductsByName(String keyword) {
        log.info("Searching products by name keyword: {}", keyword);
        return productRepository.findByNameContainingIgnoreCase(keyword)
                .stream()
                .map(this::toProductResponse)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDto> getProductsWithDiscount() {
        log.info("Fetching products with discount");
        return productRepository.findByDiscountGreaterThan(0.0)
                .stream()
                .map(this::toProductResponse)
                .collect(Collectors.toList());
    }

    public BigDecimal getAverageProductPrice() {
        log.info("Calculating average product price");
        return productRepository.getAveragePrice();
    }

    public Long getTotalProductsCount() {
        log.info("Getting total products count");
        return productRepository.count();
    }

    // ==================== UPDATE ====================

    public ProductResponseDto updateProduct(Long productId, ProductUpdateRequestDto updateRequest,
            MultipartFile photo) {
        log.info("Updating product with id: {}", productId);

        Product product = findProductOrThrow(productId);

        if (updateRequest.name() != null) {
            product.setName(updateRequest.name());
        }

        if (updateRequest.price() != null) {
            product.setPrice(updateRequest.price());
        }

        if (updateRequest.quantity() != null) {
            product.setQuantity(updateRequest.quantity());
        }

        if (updateRequest.description() != null) {
            product.setDescription(updateRequest.description());
        }

        if (updateRequest.categoryName() != null) {
            product.setCategory(findCategoryOrThrow(updateRequest.categoryName()));
        }

        if (updateRequest.discount() != null) {
            product.setDiscount(updateRequest.discount());
        }

        if (updateRequest.isDesponible() != null) {
            product.setIsDesponible(updateRequest.isDesponible());
        }

        if (photo != null && !photo.isEmpty()) {
            String fileName = fileService.storeMultipartFile(photo, product.getProductId(), pathCompletion);
            product.setImageUrl(fileName);
        }

        Product updatedProduct = productRepository.save(product);
        log.info("Product updated successfully with id: {}", updatedProduct.getProductId());

        return toProductResponse(updatedProduct);
    }

    public ProductResponseDto updateProductAvailability(Long productId, Boolean isAvailable) {
        log.info("Updating availability for product id: {} to: {}", productId, isAvailable);

        Product product = findProductOrThrow(productId);
        product.setIsDesponible(isAvailable);

        Product updatedProduct = productRepository.save(product);
        return toProductResponse(updatedProduct);
    }

    public ProductResponseDto updateProductStock(Long productId, Long quantityToAdd) {
        log.info("Updating stock for product id: {} by adding: {}", productId, quantityToAdd);

        Product product = findProductOrThrow(productId);
        Long newQuantity = product.getQuantity() + quantityToAdd;

        if (newQuantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        product.setQuantity(newQuantity);

        if (newQuantity == 0) {
            product.setIsDesponible(false);
        } else if (newQuantity > 0 && !product.getIsDesponible()) {
            product.setIsDesponible(true);
        }

        Product updatedProduct = productRepository.save(product);
        return toProductResponse(updatedProduct);
    }

    public ProductResponseDto applyDiscountToProduct(Long productId, Double discountPercentage) {
        log.info("Applying {}% discount to product id: {}", discountPercentage, productId);

        if (discountPercentage < 0 || discountPercentage > 1) {
            throw new IllegalArgumentException("Discount must be between 0 and 1");
        }

        Product product = findProductOrThrow(productId);
        product.setDiscount(discountPercentage);

        Product updatedProduct = productRepository.save(product);
        return toProductResponse(updatedProduct);
    }

    // ==================== DELETE ====================

    public void deleteProduct(Long productId) {
        log.info("Deleting product with id: {}", productId);

        Product product = findProductOrThrow(productId);
        productRepository.delete(product);

        log.info("Product deleted successfully with id: {}", productId);
    }

    public void deleteAllProductsInCategory(String categoryName) {
        log.info("Deleting all products in category: {}", categoryName);

        Category category = findCategoryOrThrow(categoryName);
        List<Product> products = productRepository.findByCategory(category);

        if (products.isEmpty()) {
            throw new ProductNotFoundException("No products found in category: " + categoryName);

        }

        productRepository.deleteAll(products);
        log.info("Deleted {} products in category: {}", products.size(), categoryName);
    }

    public void deleteProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        log.info("Deleting products by price range: {} - {}", minPrice, maxPrice);

        List<Product> products = productRepository.findByPriceBetween(minPrice, maxPrice);

        if (products.isEmpty()) {
            throw new ProductNotFoundException("No products found in price range: " + minPrice + " - " + maxPrice);
        }

        productRepository.deleteAll(products);
        log.info("Deleted {} products in price range", products.size());
    }

    // ==================== BUSINESS LOGIC ====================

    public BigDecimal calculatePriceAfterDiscount(Long productId) {
        Product product = findProductOrThrow(productId);
        BigDecimal originalPrice = product.getPrice();
        Double discount = product.getDiscount();

        BigDecimal discountedAmount = originalPrice.multiply(BigDecimal.valueOf(discount));
        BigDecimal finalPrice = originalPrice.subtract(discountedAmount);

        log.info("Calculated price after discount for product {}: {} -> {}",
                productId, originalPrice, finalPrice);

        return finalPrice;
    }

    public boolean isProductInStock(Long productId, Long requestedQuantity) {
        Product product = findProductOrThrow(productId);
        boolean inStock = product.getIsDesponible() && product.getQuantity() >= requestedQuantity;

        log.info("Product {} stock check - requested: {}, available: {}, inStock: {}",
                productId, requestedQuantity, product.getQuantity(), inStock);

        return inStock;
    }

    public void reduceProductStock(Long productId, Long quantity) {
        log.info("Reducing stock for product id: {} by: {}", productId, quantity);

        Product product = findProductOrThrow(productId);

        if (product.getQuantity() < quantity) {
            throw new InsufficientStockException("Insufficient stock for product: " + product.getName() +
                    ". Available: " + product.getQuantity() + ", Requested: " + quantity);
        }

        Long newQuantity = product.getQuantity() - quantity;
        product.setQuantity(newQuantity);

        if (newQuantity == 0) {
            product.setIsDesponible(false);
        }

        productRepository.save(product);
        log.info("Stock reduced for product {}. New quantity: {}", productId, newQuantity);
    }

    public List<ProductResponseDto> getLowStockProducts(Long threshold) {
        log.info("Fetching products with stock below threshold: {}", threshold);

        return productRepository.findByQuantityLessThan(threshold)
                .stream()
                .map(this::toProductResponse)
                .collect(Collectors.toList());
    }

    public void bulkUpdateDiscount(List<Long> productIds, Double discountPercentage) {
        log.info("Applying {}% discount to multiple products", discountPercentage);

        if (discountPercentage < 0 || discountPercentage > 1) {
            throw new IllegalArgumentException("Discount must be between 0 and 1");
        }

        List<Product> products = productRepository.findAllById(productIds);

        for (Product product : products) {
            product.setDiscount(discountPercentage);
        }

        productRepository.saveAll(products);
        log.info("Discount applied to {} products", products.size());
    }

    // ==================== PRIVATE HELPER METHODS ====================

    private Product findProductOrThrow(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));
    }

    private Category findCategoryOrThrow(String categoryName) {
        return categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with Name: " + categoryName));
    }

    private ProductResponseDto toProductResponse(Product product) {
        return ProductResponseDto.builder()
                .isDesponible(product.getIsDesponible())
                .name(product.getName())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .productId(product.getProductId())
                .discount(product.getDiscount())
                .imageUrl(baseUrl+pathCompletion+"/"+product.getImageUrl())
                .createdAt(product.getCreatedAt())
                .description(product.getDescription())
                .categoryName(product.getCategory().getName())
                .build();
    }
}
