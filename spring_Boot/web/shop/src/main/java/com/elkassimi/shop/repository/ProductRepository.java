package com.elkassimi.shop.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.elkassimi.shop.model.Category;
import com.elkassimi.shop.model.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryCategoryId(Long categoryId);

    List<Product> findByCategory(Category category);

    List<Product> findByIsDesponibleTrue();

    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByDiscountGreaterThan(Double discount);

    List<Product> findByQuantityLessThan(Long threshold);

    @Query("SELECT AVG(p.price) FROM Product p")
    BigDecimal getAveragePrice();

    Page<Product> findAll(Pageable pageable);
}
