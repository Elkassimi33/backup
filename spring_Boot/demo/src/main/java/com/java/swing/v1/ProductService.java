package com.java.swing.v1;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
@Service
public class ProductService {

    public  List<Product> getAllProducts() {
        return Arrays.asList(new Product("q",13.5,40),new Product("stywd",13.5,40));
    }

    public  Product saveProduct(Product product) {
        return new Product("q",13.5,40);
    }

    public Product/*Optional<Object>*/ getProductById(Long id) {
        return new Product("q",13.5,40);
    }
}
