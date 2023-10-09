package com.example.SalesApp.supermarketBillingSystem.Service;

import com.example.SalesApp.supermarketBillingSystem.Entity.Sales;
import com.example.SalesApp.supermarketBillingSystem.Repository.SalesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalesServiceImpl implements SalesService{

    @Autowired
    private SalesRepo salesRepo;

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
    public Sales getSalesById(Sales sales) {
        return salesRepo.getReferenceById(sales.getSalesId());
    }

    @Override
    public List<Sales> getAllSales() {
        return salesRepo.findAll();
    }
}
