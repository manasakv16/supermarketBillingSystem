package com.example.SalesApp.supermarketBillingSystem.Repository;

import com.example.SalesApp.supermarketBillingSystem.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
}
