package com.example.SalesApp.supermarketBillingSystem.Repository;

import com.example.SalesApp.supermarketBillingSystem.Entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepo extends JpaRepository<Customer, String> {
}
