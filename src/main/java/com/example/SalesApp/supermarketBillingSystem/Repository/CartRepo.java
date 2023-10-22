package com.example.SalesApp.supermarketBillingSystem.Repository;

import com.example.SalesApp.supermarketBillingSystem.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepo extends JpaRepository<Cart, Long> {
}
