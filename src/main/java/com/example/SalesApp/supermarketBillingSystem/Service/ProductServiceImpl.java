package com.example.SalesApp.supermarketBillingSystem.Service;

import com.example.SalesApp.supermarketBillingSystem.Entity.Product;
import com.example.SalesApp.supermarketBillingSystem.Repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements  ProductService{

    @Autowired
    private ProductRepo productRepo;

    @Override
    public Product addProduct(Product product) {
        return productRepo.save(product);
    }

    @Override
    public Product editProduct(Product product) {
        return productRepo.save(product);
    }

    @Override
    public void deleteProduct(Product product) {
        productRepo.delete(product);
    }

    @Override
    public Product getProductById(Product product) {
        return productRepo.getReferenceById(product.getProductId());
    }

    @Override
    public Optional<Product> getProductById(Long productId) {
        return productRepo.findById(productId);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }
}
