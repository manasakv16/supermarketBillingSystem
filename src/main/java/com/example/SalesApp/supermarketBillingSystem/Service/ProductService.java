package com.example.SalesApp.supermarketBillingSystem.Service;

import com.example.SalesApp.supermarketBillingSystem.Entity.Product;

import java.util.List;

public interface ProductService {

    Product addProduct(Product product);
    Product editProduct(Product product);
    void deleteProduct(Product product);
    Product getProductById(Product product);
    List<Product> getAllProducts();
}
