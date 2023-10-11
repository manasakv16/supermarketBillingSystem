package com.example.SalesApp.supermarketBillingSystem.Controller;

import com.example.SalesApp.supermarketBillingSystem.Entity.Product;
import com.example.SalesApp.supermarketBillingSystem.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @RequestMapping("/addProduct")
    public String addProduct(ModelMap modelMap){
        List<Product> productList = productService.getAllProducts();
        modelMap.addAttribute("product", productList);
        return "addProduct";
    }

    @RequestMapping("/saveProduct")
    public String saveProduct(@ModelAttribute("product") Product product, ModelMap modelMap) {
        System.out.println("in save Product");
        System.out.println("product: "+product);
        String msg;
        Product product1 = new Product();
        if(product.getProductName() != null && product.getProductCost() != null) {
            product1 = productService.addProduct(product);
            msg = "*** Created Product - " + product1.getProductName() + "***";
        }
        else {
            msg = "*** Failed to add product, Product name & cost information is compulsory ***";
        }
        List<Product> productList = productService.getAllProducts();
        modelMap.addAttribute("product", productList);
        modelMap.addAttribute("msg", msg);
        return "addProduct";
    }

}
