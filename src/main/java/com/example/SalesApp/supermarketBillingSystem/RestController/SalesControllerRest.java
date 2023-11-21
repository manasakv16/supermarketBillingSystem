package com.example.SalesApp.supermarketBillingSystem.RestController;

import com.example.SalesApp.supermarketBillingSystem.Entity.*;
import com.example.SalesApp.supermarketBillingSystem.Service.*;
import com.example.SalesApp.supermarketBillingSystem.security.dto.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Controller
@RequestMapping("/sale")
public class SalesControllerRest {

    @Autowired
    private SalesService salesService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CartService cartService;
    @Autowired
    private PdfGenerationService pdfGenerationService;

    @PostMapping("/")
    public ResponseEntity<JsonResponse> startSale(@RequestBody Sales sales) {
        final Optional<Customer> getCustomer = customerService.getCustomerById(sales.getCustomerId());
        String msg = "";
        sales.setTotal(0.0);
        final JsonResponse jsonResponse = new JsonResponse();
        final Sales newSales;
        if (getCustomer.isPresent() && getCustomer.get().getCustomerMobile().length() == 10) {
            msg = "sale created, start shopping.";
            newSales = salesService.addSales(sales);
        }
        else {
            if (sales.getCustomerId().length() == 10) {
                Customer customer = customerService.addCustomer(new Customer("", sales.getCustomerId(), ""));
                msg = "Customer registered - " + sales.getCustomerId() + ", start shopping- ";
                newSales = salesService.addSales(sales);
            }
            else {
                msg = "Invalid mobile number";
                newSales = null;
            }
        }
        jsonResponse.setMessage(msg);
        jsonResponse.setObject(newSales);
        jsonResponse.setResponseCode("200");
        return ResponseEntity.ok(jsonResponse);
    }

    @GetMapping("/bill/{id}")
    public ResponseEntity<JsonResponse> payAndGenerateBill(@PathVariable("id") Long salesID) {
        final Optional<Sales> sale = salesService.getSalesById(salesID);
        final JsonResponse jsonResponse = new JsonResponse();
        if (sale.isPresent()) {
            final List<Object> list = new ArrayList<>();
            list.add(sale);
            final List<Product> productList = getUserProductList(getUserCartList(salesID));
            list.addAll(productList);
            jsonResponse.setObject(list);
            jsonResponse.setMessage("Bill of user - " + sale.get().getCustomerId());
        }
        else {
            jsonResponse.setMessage("No sale found with ID - " + salesID);
        }
        return ResponseEntity.ok(jsonResponse);
    }

    @GetMapping("/")
    public ResponseEntity<JsonResponse> getSalesReport() {
        final List<Sales> allSales = salesService.getAllSales();
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.setObject(allSales);
        return ResponseEntity.ok(jsonResponse);
    }

    @GetMapping("{id}")
    public ResponseEntity<JsonResponse> editSale(@PathVariable("id") Long salesId) {
        final JsonResponse jsonResponse = new JsonResponse();
        final Optional<Sales> sales = salesService.getSalesById(salesId);
        if (sales.isPresent()) {
            List<Cart> entireUserCart = cartService.getEntireUserCart(salesId);
            jsonResponse.setObject(entireUserCart);
        }
        else {
            jsonResponse.setMessage("No sale found for given ID: " + salesId);
        }
        return  ResponseEntity.ok(jsonResponse);
    }

    @GetMapping("{id}/{pid}")
    public ResponseEntity<JsonResponse> editProductOfASale(@PathVariable("id") Long salesId, @PathVariable("pid") Long productId) {
        final JsonResponse jsonResponse = new JsonResponse();
        final Optional<Sales> sales = salesService.getSalesById(salesId);
        if (sales.isPresent()) {
            Optional<Cart> cartById = cartService.findCartById(new CartId(salesId, productId));
            if (cartById.isPresent()) {
                Optional<Product> product = productService.getProductById(productId);
                product.get().setProductUnit(cartById.get().getProductCount());
                product.get().setTotalProductCost(product.get().getProductCost() * product.get().getProductUnit());
                jsonResponse.setObject(product.get());
            }
            else {
                jsonResponse.setMessage("No such product found in cart for given Sales ID: " + salesId);
            }
        }
        else {
            jsonResponse.setMessage("No sale found for given ID: " + salesId);
        }
        return  ResponseEntity.ok(jsonResponse);
    }

    @PutMapping("{id}/{pid}")
    public ResponseEntity<JsonResponse> updateSalesProduct(@PathVariable("id") Long salesId, @PathVariable("pid") Long productId,
                                                           @RequestBody Product product) {
        final JsonResponse jsonResponse = new JsonResponse();
        final Optional<Sales> sales = salesService.getSalesById(salesId);
        final Optional<Product> product1 = productService.getProductById(productId);
        if (sales.isPresent() && product1.isPresent()) {
            final Optional<Cart> oldCart = cartService.findCartById(new CartId(salesId, productId));
            final Cart newCart = new Cart(salesId, productId, product.getProductUnit());
            if (oldCart.isPresent()) {
                final double amount = (newCart.getProductCount() - oldCart.get().getProductCount())
                        * product1.get().getProductCost();
                final Cart updatedCart = cartService.editCart(newCart);
                sales.get().setTotal(sales.get().getTotal() + amount);
                jsonResponse.setMessage("Updated Product - " + product1.get().getProductName());
            }
            else {
                final Cart updatedCart = cartService.editCart(newCart);
                sales.get().setTotal(sales.get().getTotal() + (newCart.getProductCount()) * product1.get().getProductCost());
                jsonResponse.setMessage("Added product - " + product1.get().getProductName());
            }
            Sales updatedSales = salesService.editSales(sales.get());

            // return cart back to user
            List<Cart> entireUserCart = cartService.getEntireUserCart(salesId);
            ArrayList<Object> list = new ArrayList<>();
            list.add(updatedSales);
            list.addAll(entireUserCart);
            jsonResponse.setObject(list);
        }
        else {
            jsonResponse.setMessage("No sale/product found for given ID - " + salesId + ", " + productId);
        }
        return ResponseEntity.ok(jsonResponse);
    }

    @DeleteMapping("{id}/{pid}")
    public ResponseEntity<JsonResponse> deleteProduct(@PathVariable("id") Long salesId, @PathVariable("pid") Long productId) {
        final JsonResponse jsonResponse = new JsonResponse();
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
        jsonResponse.setObject(productList);
        jsonResponse.setMessage("Deleted Product - " + product.get().getProductName() + " from sale - " + salesId);
        return ResponseEntity.ok(jsonResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<JsonResponse> deleteSale(@PathVariable("id") Long salesId) {

        final List<Cart> cartList = getUserCartList(salesId);
        for (final Cart cart : cartList) {
            cartService.deleteCart(cart);
        }
        final Optional<Sales> salesById = salesService.getSalesById(salesId);
        salesService.deleteSales(salesById.get());
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.setMessage("Deleted Sale - " + salesById.get().getSalesId());
        jsonResponse.setResponseCode("200");
        return ResponseEntity.ok(jsonResponse);
    }

    public List<Cart> getUserCartList(final Long salesId) {
        return cartService.getEntireUserCart(salesId);
    }

    // Update the product list based on their cart
    public List<Product> getUserProductList(final List<Cart> userCartList) {
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

    // Generate bill as pdf and enable download
    @GetMapping("/generate/{id}")
    public ResponseEntity<byte[]> generateAndDownloadBill(@PathVariable("id") Long salesID) {
        final Optional<Sales> sale = salesService.getSalesById(salesID);
        final List<Object> list = new ArrayList<>();
        if (sale.isPresent()) {
            list.add(sale.get());
            list.addAll(getUserProductList(getUserCartList(salesID)));
            return getBillAsPdfUsingIText(list, salesID);
        }
        throw new RuntimeException("Invalid sales ID");
    }

    public ResponseEntity<byte[]> getBillAsPdfUsingIText(final List<Object> list, final Long salesID){
        final byte[] pdfBytes = pdfGenerationService.generateBillPdf(list);

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "bill_" + salesID + ".pdf");
        return new ResponseEntity<>(pdfBytes, headers, org.springframework.http.HttpStatus.OK);
    }

    public ResponseEntity<byte[]> getBillAsPdfUsingPdfBox(final List<Object> list){
        final String billContent = list.toString();
        final byte[] pdfBytes = pdfGenerationService.generateBillPdf(billContent);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "bill.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(pdfBytes.length)
                .body(pdfBytes);
    }

}
