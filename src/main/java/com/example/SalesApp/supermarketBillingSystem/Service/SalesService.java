package com.example.SalesApp.supermarketBillingSystem.Service;

import com.example.SalesApp.supermarketBillingSystem.Entity.Sales;

import java.util.List;
import java.util.Optional;

public interface SalesService {

    Sales addSales(Sales sales);
    Sales editSales(Sales sales);
    void deleteSales(Sales sales);
    Optional<Sales> getSalesById(Long salesId);
    List<Sales> getAllSales();


}
