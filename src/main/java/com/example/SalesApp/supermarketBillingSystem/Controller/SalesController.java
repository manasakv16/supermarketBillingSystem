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
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

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

        System.out.println("Sales: " + sales);
        System.out.println("product: " + product);

        String msg="";
        Sales savedSale = new Sales();
        String productIds ="";
        String productCounts = "";
        double total = 0;
        double newTotal = 0;

        // existing sale, add items
        if(sales.getSalesId() != null) {
            savedSale = salesService.getSalesById(sales.getSalesId());

            if (savedSale.getProductId() != null && !savedSale.getProductId().isEmpty()) {
                productIds = savedSale.getProductId() + "," + sales.getProductId();
                productCounts = savedSale.getUnitCount() + "," + product.getProductUnit();
                total = savedSale.getTotal();
            }
            else {
                productIds = product.getProductId().toString();
                productCounts = product.getProductUnit().toString();
            }

            savedSale.setProductId(productIds);
            savedSale.setUnitCount(productCounts);
            total += (productService.getProductById(product.getProductId()).get().getProductCost() * Double.valueOf(product.getProductUnit()));
            savedSale.setTotal(total);

            // update the cart
            Sales sales2 = salesService.editSales(savedSale);

            final List<Product> productList = new ArrayList<>();
            salesService.getProductList(sales2.getSalesId(), productList);

            modelMap.addAttribute("product", productList);
            msg = " updated cart, keep shopping!";
            modelMap.addAttribute("msg", msg);
//            final String[] ids = sales2.getProductId().split(",");
//            String[] productUnit = productCounts.split(",");
//            int i = 0;
//
//            for (String id : ids) {
//                Optional<Product> productById = productService.getProductById(Long.valueOf(id));
//                productById.get().setProductUnit(Integer.valueOf(productUnit[i++]));
//                productList.add(productById.orElseGet(Product::new));
//            }

//            System.out.println("ids: " + Arrays.toString(ids));


        }
        // Create a new sale
        else if(sales.getCustomerMobile() != null && !sales.getCustomerMobile().isEmpty() ) {
            savedSale = salesService.addSales(sales);
            msg = "Created Sale, Start Shopping.!!";
        }
        // customer mobile is compulsory
        else {
            msg = "*** Failed to create Bill, Customer Mobile is compulsory ***";
            modelMap.addAttribute("msg", msg);
            return "addSale";
        }

        modelMap.addAttribute("sales", savedSale);
        modelMap.addAttribute("msg", msg);

        return "updateCart";
    }

    @RequestMapping("/payAndGenerateBill")
    public String payAndGenerateBill(@RequestParam("id") Long salesID, ModelMap modelMap){

        System.out.println("Sales ID: " + salesID);
        System.out.println("date: "  + String.valueOf(Date.from(Instant.now())));
        LocalDate now = LocalDate.now();
        System.out.println("date 2: " + now);
        Sales sale = salesService.getSalesById(salesID);
        System.out.println("SaLe: " + sale);
        final List<Product> productList = new ArrayList<>();
        salesService.getProductList(salesID, productList);

        modelMap.addAttribute("sales", sale);
        modelMap.addAttribute("product", productList);
        modelMap.addAttribute("msg", "Thanks for shopping with us. <3 ");

        return "payAndGenerateBill";
    }


    @RequestMapping("/salesReport")
    public String getSalesReport(ModelMap modelMap){
        List<Sales> allSales = salesService.getAllSales();
        modelMap.addAttribute("sales", allSales);
        return "salesReport";
    }

    @RequestMapping("/editSales")
    public String editSale(@RequestParam("id") Long salesId, ModelMap modelMap){
        final List<Product> productList = new ArrayList<>();
        salesService.getProductList(salesId, productList);
        modelMap.addAttribute("product", productList);
        String msg = " Edit product list for Sale ID - " + salesId;
        modelMap.addAttribute("msg", msg);
        modelMap.addAttribute("product", productList);
        return "editSale";
    }

//    public String updateSale(@ModelAttribute("sales") Sales sales, ModelMap modelMap) {
//        Sales savedSale = salesService.editSales(sales);
//        final List<Product> productList = new ArrayList<>();
//        getProductList(sales.getSalesId(), productList);
//        modelMap.addAttribute("product", productList);
//        String msg = " updated cart, keep shopping!";
//        modelMap.addAttribute("msg", msg);
//        return "updateCart";
//    }

    @RequestMapping("/updateSalesProduct")
    public String updateSalesProduct(@ModelAttribute("product") Product product, ModelMap modelMap){
        // Logic to update product
        String msg = "Update Product - " + product.getProductName();
        modelMap.addAttribute("msg", msg);
        return "editSalesProduct";
    }

    @RequestMapping("/deleteProductInSale")
    public String deleteProduct(@RequestParam("id") Long salesId, @RequestParam("pid") Long productId, ModelMap modelMap){
        System.out.println("id: " + salesId);
        System.out.println("pid: " + productId);

        Sales sales = salesService.getSalesById(salesId);

        System.out.println("sales: " + sales);
        if(sales.getProductId().contains(String.valueOf(productId))){
            sales.setProductId(sales.getProductId().replaceFirst(String.valueOf(productId), "").replaceAll(",,",","));
        }
        Sales deletedSales = salesService.editSales(sales);
        System.out.println("deletedSales: " + deletedSales);

        String msg = "Update Sale - " + salesId + "\n Deleted Product - " +
                productService.getProductById(productId).get().getProductName();
        List<Product> productList = new ArrayList<>();
        salesService.getProductList(salesId, productList);
        Double newTotal = productList.stream().mapToDouble(Product::getTotalProductCost).sum();
        modelMap.addAttribute("sales", deletedSales);
        modelMap.addAttribute("product", productList);
        modelMap.addAttribute("msg", msg);
        return "updateCart";
    }


}
