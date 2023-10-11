package com.example.SalesApp.supermarketBillingSystem.Service;

import com.example.SalesApp.supermarketBillingSystem.Entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    Product addProduct(Product product);
    Product editProduct(Product product);
    void deleteProduct(Product product);
    Product getProductById(Product product);
    Optional<Product> getProductById(Long productId);
    List<Product> getAllProducts();
}
