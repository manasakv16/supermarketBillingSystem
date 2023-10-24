package com.example.SalesApp.supermarketBillingSystem.Service;

import com.example.SalesApp.supermarketBillingSystem.Entity.Product;
import com.example.SalesApp.supermarketBillingSystem.Entity.Sales;

import java.util.List;

public interface SalesService {

    Sales addSales(Sales sales);
    Sales editSales(Sales sales);
    void deleteSales(Sales sales);
    Sales getSalesById(Sales sales);
    Sales getSalesById(Long salesId);
    List<Sales> getAllSales();


}
