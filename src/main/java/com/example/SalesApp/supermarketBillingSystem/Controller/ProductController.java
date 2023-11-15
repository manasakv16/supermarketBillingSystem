package com.example.SalesApp.supermarketBillingSystem.Controller;

import com.example.SalesApp.supermarketBillingSystem.Entity.Product;
import com.example.SalesApp.supermarketBillingSystem.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
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

        String msg;
        product.setTotalProductCost(product.getProductCost() * product.getProductUnit());
        Product product1 = new Product();
        if(product.getProductName() != null && !product.getProductName().isEmpty()
                && product.getProductCost() != null && product.getProductCost() > 0) {
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

    @RequestMapping("/editProduct")
    public String editProduct(@RequestParam("id") Long productId, ModelMap modelMap){
        Optional<Product> product = productService.getProductById(productId);
        modelMap.addAttribute("product", product.get());
        return "editProduct";
    }

    @RequestMapping("/updateProduct")
    public String updateProduct(@ModelAttribute("product") Product product, ModelMap modelMap){
        Product savedProduct = productService.editProduct(product);
        String msg = "Update Product - " + product.getProductName();
        modelMap.addAttribute("msg", msg);
        return "editProduct";
    }

    @RequestMapping("/deleteProduct")
    public String deleteProduct(@RequestParam("id") Long productId, ModelMap modelMap){
        Optional<Product> product = productService.getProductById(productId);
        productService.deleteProduct(product.get());
        String msg = "Deleted Product - " + product.get().getProductName();
        List<Product> productList = productService.getAllProducts();
        modelMap.addAttribute("product", productList);
        modelMap.addAttribute("msg", msg);
        return "addProduct";
    }

}
