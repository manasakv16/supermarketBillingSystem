package com.example.SalesApp.supermarketBillingSystem.Service;

import com.example.SalesApp.supermarketBillingSystem.Entity.Product;
import com.example.SalesApp.supermarketBillingSystem.Entity.Sales;
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
    ProductRepo productRepo;

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
    public Sales getSalesById(Long salesId) {
        return salesRepo.getReferenceById(salesId);
    }

    @Override
    public List<Sales> getAllSales() {
        return salesRepo.findAll();
    }

    @Override
    public Sales generateBill(String customerId, String customerName, String customerMobile,
                             String customerEmail, List<Product> productList) {
        Double total = 0.0;//productList.mapToDouble(Product::getProductCost).sum();
        Sales sales = new Sales();
        sales.setTotal(total);
       // sales.setProductList(productList);
        sales.setCustomerName(customerName);
        sales.setCustomerEmail(customerEmail);
        sales.setCustomerMobile(customerMobile);
        return sales;
    }

    public void getProductList(Long salesId, List<Product> productList){
        Sales sale = salesRepo.getReferenceById(salesId);
        final String[] ids = sale.getProductId().split(",");
        String[] productUnit = sale.getUnitCount().split(",");
        int i = 0;

        for (String id : ids) {
            if(!id.isEmpty()) {
                Optional<Product> productById = Optional.of(productRepo.getReferenceById(Long.valueOf(id)));
                productById.get().setProductUnit(Integer.valueOf(productUnit[i++]));
                productById.get().setTotalProductCost(productById.get().getProductCost() *
                        productById.get().getProductUnit());
                productList.add(productById.orElseGet(Product::new));
            }
        }

    }




}
