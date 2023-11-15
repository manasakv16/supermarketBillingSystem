package com.example.SalesApp.supermarketBillingSystem.Controller;

import com.example.SalesApp.supermarketBillingSystem.Entity.*;
import com.example.SalesApp.supermarketBillingSystem.Service.CartService;
import com.example.SalesApp.supermarketBillingSystem.Service.CustomerService;
import com.example.SalesApp.supermarketBillingSystem.Service.ProductService;
import com.example.SalesApp.supermarketBillingSystem.Service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Controller
public class SalesController {

    @Autowired
    private SalesService salesService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CartService cartService;

    @RequestMapping("/addSale")
    public String addSale(){
        return "addSale";
    }

    // CUSTOMER VALIDATION
    // If customer valid - proceed with sale
    // Else - ask to register customer
    @RequestMapping("/startSale")
    public String startSale(@ModelAttribute ("sales")Sales sales, final ModelMap modelMap) {
        final Optional<Customer> getCustomer = customerService.getCustomerById(sales.getCustomerId());
        String msg = "";
        if(customerService.isCustomerValid(getCustomer.get())) {
            final Sales newSales = salesService.addSales(sales);
            modelMap.addAttribute("sales", newSales);
            return "updateCart";
        }
        msg = "Customer not registered, please register - " + sales.getCustomerId();
        modelMap.addAttribute("msg", msg);
        return "addSale";
    }

    // UPDATE THE CART FOR A PARTICULAR SALE
    @RequestMapping("/saveSale")
    public String saveSale(@ModelAttribute Sales sales, @ModelAttribute Product product, ModelMap modelMap) {
        final AtomicBoolean cartUpdated = new AtomicBoolean(false);
        final List<Cart> userCartList = getUserCartList(sales.getSalesId());

        // ADD PRODUCT TO CART
        // UPDATING CART OF EXISTING ITEM
        if(userCartList.size()!=0) {
            for(final Cart userCart : userCartList) {
                if (userCart.getProductId() == product.getProductId()) {
                    userCart.setProductCount(userCart.getProductCount() + product.getProductUnit());
                    final Cart editedCart = cartService.editCart(userCart);
                    cartUpdated.set(true);
                }
            }
        }
        // UPDATING CART FOR NEW ITEM
        if(!cartUpdated.get()){
            final Cart cart = cartService.addCart(
                    new Cart(sales.getSalesId(), product.getProductId(), product.getProductUnit()));
            userCartList.add(new Cart(sales.getSalesId(), product.getProductId(), product.getProductUnit()));
        }

        // CALCULATE & SAVE THE NEW TOTAL OF THE SALE
        final Sales addedSales = salesService.editSales(getUserNewTotal(product, sales));

        // GET THE PRODUCT LIST TO DISPLAY BACK TO THE USER
        final List<Product> productList = getUserProductList(userCartList);

        modelMap.addAttribute("sales", addedSales);
        modelMap.addAttribute("product", productList);
        return "updateCart";
    }

    @RequestMapping("/payAndGenerateBill")
    public String payAndGenerateBill(@RequestParam("id") Long salesID, final ModelMap modelMap){

        final Optional<Sales> sale = salesService.getSalesById(salesID);
        final List<Product> productList = getUserProductList(getUserCartList(salesID));
        final Optional<Customer> customer = customerService.getCustomerById(sale.get().getCustomerId());
        for(final Product product: productList){
            product.setTotalProductCost(product.getProductCost() * product.getProductUnit());
        }
        modelMap.addAttribute("customer", customer.get());
        modelMap.addAttribute("sales", sale);
        modelMap.addAttribute("product", productList);
        modelMap.addAttribute("msg", "Thanks for shopping with us. <3 ");

        return "payAndGenerateBill";
    }


    @RequestMapping("/salesReport")
    public String getSalesReport(final ModelMap modelMap){
        final List<Sales> allSales = salesService.getAllSales();
        modelMap.addAttribute("sales", allSales);
        return "salesReport";
    }

    @RequestMapping("/editSales")
    public String editSale(@RequestParam("id") Long salesId, @RequestParam("pid") Long productId, final ModelMap modelMap){
        final Optional<Cart> cart = cartService.findCartById(new CartId(salesId, productId));
        final Optional<Product> product = productService.getProductById(productId);
        modelMap.addAttribute("product", product.get());
        modelMap.addAttribute("cart", cart);
        return "editSale";
    }

    @RequestMapping("/updateSalesProduct")
    public String updateSalesProduct(@ModelAttribute("product") Product product, @ModelAttribute("cart") Cart cart,
                                     final ModelMap modelMap){

        final Optional<Cart> oldCart = cartService.findCartById(new CartId(cart.getSalesId(), cart.getProductId()));
        final Cart newCart = new Cart(cart.getSalesId(), cart.getProductId(), cart.getProductCount());
        final double amount = (newCart.getProductCount() - oldCart.get().getProductCount()) * product.getProductCost();
        final Cart updatedCart = cartService.editCart(newCart);

        // update total
        final Optional<Sales> sales = salesService.getSalesById(cart.getSalesId());
        sales.get().setTotal(sales.get().getTotal() + amount);
        Sales updatedSales = salesService.editSales(sales.get());

        // return cart back to user
        final List<Product> productList = getUserProductList(getUserCartList(newCart.getSalesId()));
        modelMap.addAttribute("sales", updatedSales);
        modelMap.addAttribute("product", productList);

        final String msg = "Updated Product - " + product.getProductName();
        modelMap.addAttribute("msg", msg);
        return "updateCart";
    }

    @RequestMapping("/deleteProductInSale")
    public String deleteProduct(@RequestParam("id") Long salesId, @RequestParam("pid") Long productId, final ModelMap modelMap) {
        final Optional<Cart> cart = cartService.findCartById(new CartId(salesId, productId));
        cartService.deleteCart(cart.get());

        // update the total
        final Optional<Product> product = productService.getProductById(cart.get().getProductId());
        final double amount = cart.get().getProductCount() * product.get().getProductCost();
        final Optional<Sales> sales = salesService.getSalesById(salesId);
        sales.get().setTotal(sales.get().getTotal() - amount);
        Sales updatedSales = salesService.editSales(sales.get());

        // return cart back to user
        final List<Product> productList = getUserProductList(getUserCartList(salesId));
        modelMap.addAttribute("sales", updatedSales);
        modelMap.addAttribute("product", productList);
        return "updateCart";
    }

    @RequestMapping("/deleteSales")
    public String deleteSale(@RequestParam("id") Long salesId, final ModelMap modelMap){

        final List<Cart> cartList = getUserCartList(salesId);
        for(final Cart cart: cartList){
            cartService.deleteCart(cart);
        }
        final Optional<Sales> salesById = salesService.getSalesById(salesId);
        salesService.deleteSales(salesById.get());
        modelMap.addAttribute("msg", "Deleted sale - " + salesId);
        return "addSale";
    }

    public List<Cart> getUserCartList(final Long salesId) {
        return cartService.getEntireUserCart(salesId);
    }
    public List<Product> getUserProductList(final List<Cart> userCartList){
        final List<Product> productList = new ArrayList<>();

        for (final Cart cart : userCartList) {
            final Optional<Product> findProduct = productService.getProductById(cart.getProductId());
            final Product product1 = new Product(cart.getProductId(), findProduct.get().getProductName(),
                    cart.getProductCount(), findProduct.get().getProductCost(),
                    (cart.getProductCount() * findProduct.get().getProductCost()));
            productList.add(product1);

        }
        return productList;
    }

    public Sales getUserNewTotal(final Product product, final Sales sales){
        final Optional<Product> getProduct = productService.getProductById(product.getProductId());
        final Optional<Sales> getSale = salesService.getSalesById(sales.getSalesId());
        if(getSale.get().getTotal() != null) {
            getSale.get().setTotal(getSale.get().getTotal() + (getProduct.get().getProductCost() * product.getProductUnit()));
        }
        else  {
            getSale.get().setTotal((getProduct.get().getProductCost() * product.getProductUnit()));
        }
        return getSale.get();
    }

}
