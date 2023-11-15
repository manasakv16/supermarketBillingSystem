package com.example.SalesApp.supermarketBillingSystem.Service;

import com.example.SalesApp.supermarketBillingSystem.Entity.Sales;
import com.example.SalesApp.supermarketBillingSystem.Repository.CustomerRepo;
import com.example.SalesApp.supermarketBillingSystem.Repository.ProductRepo;
import com.example.SalesApp.supermarketBillingSystem.Repository.SalesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SalesServiceImpl implements SalesService{

    @Autowired
    private SalesRepo salesRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CustomerRepo customerRepo;

    @Override
    public Sales addSales(Sales sales) {
        return salesRepo.save(sales);
    }

    @Override
    public Sales editSales(Sales sales) {
        return salesRepo.save(sales);
    }

    @Override
    public void deleteSales(Sales sales) {
        salesRepo.delete(sales);
    }

    @Override
    public Optional<Sales> getSalesById(Long salesId) {
        return salesRepo.findById(salesId);
    }

    @Override
    public List<Sales> getAllSales() {
        return salesRepo.findAll();
    }
}
