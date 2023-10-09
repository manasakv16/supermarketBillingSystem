package com.example.SalesApp.supermarketBillingSystem.Controller;

import com.example.SalesApp.supermarketBillingSystem.Entity.Sales;
import com.example.SalesApp.supermarketBillingSystem.Service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SalesController {

    @Autowired
    private SalesService salesService;

    @RequestMapping("/addSale")
    public String addSale(Sales sales){
        return "addSale";
    }

    @RequestMapping("/saveSale")
    public String saveSales(Sales sales){
        Sales sales1 = salesService.addSales(sales);
        return "addSale";
    }

    @RequestMapping("/salesReport")
    public String getSalesReport(){
        salesService.getAllSales();
        return "salesReport";
    }


}
