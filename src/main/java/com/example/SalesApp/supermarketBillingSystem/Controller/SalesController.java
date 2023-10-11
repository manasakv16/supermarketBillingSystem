package com.example.SalesApp.supermarketBillingSystem.Controller;

import com.example.SalesApp.supermarketBillingSystem.Entity.Product;
import com.example.SalesApp.supermarketBillingSystem.Entity.Sales;
import com.example.SalesApp.supermarketBillingSystem.Service.ProductService;
import com.example.SalesApp.supermarketBillingSystem.Service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class SalesController {

    @Autowired
    private SalesService salesService;

    @Autowired
    private ProductService productService;

    @RequestMapping("/addSale")
    public String addSale(){
        return "addSale";
    }
/*
    @RequestMapping("/saveSale")
    public String saveSale(@ModelAttribute("sales") Sales sales, ModelMap modelMap) {
        System.out.println("in save sale");
        System.out.println("sale: "+sales);
        String msg;
        Sales sales1 = new Sales();
        if(sales.getCustomerMobile() != null && sales.getTotal() != null) {
            sales1 = salesService.addSales(sales);
            msg = "Completed Sale - " + sales1.getSalesId() + "*** \n Thanks for Shopping with us. ";
        }
        else {
            msg = "*** Failed to create Sale, Customer ID & total cost is compulsory ***";
        }
        List<Sales> salesList = salesService.getAllSales();
        modelMap.addAttribute("salesId", salesList);
        modelMap.addAttribute("msg", msg);
        return "addSale";
    }
*/
    @RequestMapping("/saveSale")
    public String saveSale(@ModelAttribute ("sales") Sales sales,
                           @ModelAttribute("product") Product product, ModelMap modelMap){
        System.out.println("sales: "+sales);
        System.out.println("product: "+product);
        String msg="";
        Sales sales1 = new Sales();
        String productIds ="";
        if(sales.getSalesId() != null) {
            sales1 = salesService.getSalesById(sales.getSalesId());
            if(sales1.getProductId() != null && !sales1.getProductId().isEmpty()) {
                productIds = sales1.getProductId();
                productIds += "," + sales.getProductId();
                sales1.setProductId(productIds);
                sales1.setUnitCount(product.getProductUnit());
            }
            else sales1.setProductId(product.getProductId().toString());
            Sales sales2 = salesService.editSales(sales1);
            List<Product> productList = new ArrayList<>();
            String ids = sales2.getProductId();
            System.out.println("ids: "+ids);
            if(ids != null && ids.contains(",")){
                    for (String id : sales2.getProductId().split(",")) {
                        Optional<Product> productById = productService.getProductById(Long.valueOf(id));
                        productList.add(productById.orElseGet(Product::new));
                    }
            }
            else {
                Long id = Long.valueOf(ids);
                Optional<Product> productById = productService.getProductById(id);
                productList.add(productById.orElseGet(Product::new));
            }

            modelMap.addAttribute("product", productList);
            msg = " updated cart, keep shopping!";
            modelMap.addAttribute("msg", msg);
            }
        else if(sales.getCustomerMobile() != null && !sales.getCustomerMobile().isEmpty() ) {
            sales1 = salesService.addSales(sales);
            msg = "Created Sale, Start Shopping.!!";
        }
        else {
            msg = "*** Failed to create Bill, Customer Mobile is compulsory ***";
        }

        modelMap.addAttribute("sales", sales);
        modelMap.addAttribute("msg", msg);

        return "updateCart";
    }

    @RequestMapping("/updateCart")
    public String updateCart(@ModelAttribute("productId") Long productId, @ModelAttribute("unit") Integer unit,
                             ModelMap modelMap){

        return "addSale";

    }

    @RequestMapping("/salesReport")
    public String getSalesReport(){
        salesService.getAllSales();
        return "salesReport";
    }


}
