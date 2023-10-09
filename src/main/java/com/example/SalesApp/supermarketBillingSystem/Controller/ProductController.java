package com.example.SalesApp.supermarketBillingSystem.Controller;

import com.example.SalesApp.supermarketBillingSystem.Entity.Product;
import com.example.SalesApp.supermarketBillingSystem.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @RequestMapping("/addProduct")
    public String addProduct(){
        return "addProduct";
    }

    @RequestMapping("/")
    public String saveProduct(Product product){
        productService.addProduct(product);
        return "addProduct";
    }

}
