package com.example.SalesApp.supermarketBillingSystem.RestController;

import com.example.SalesApp.supermarketBillingSystem.Entity.Product;
import com.example.SalesApp.supermarketBillingSystem.Service.ProductService;
import com.example.SalesApp.supermarketBillingSystem.security.dto.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductControllerRest {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<JsonResponse> getAllProducts() {
        final JsonResponse jsonResponse = new JsonResponse();
        List<Product> productList = productService.getAllProducts();
        jsonResponse.setObject("200");
        jsonResponse.setMessage("Listing all products");
        jsonResponse.setObject(productList);
        return ResponseEntity.ok(jsonResponse);
    }

    @PostMapping
    public ResponseEntity<JsonResponse> saveProduct(@RequestBody Product product) {

        final String msg;
        final JsonResponse jsonResponse = new JsonResponse();
        if((product.getProductName() == null || product.getProductName().isEmpty())
                || (product.getProductCost() == null || product.getProductCost() <= 0)) {
            msg = "*** Failed to add product, Product name & cost information is compulsory ***";
            jsonResponse.setMessage(msg);
            jsonResponse.setResponseCode("200");
            return ResponseEntity.ok(jsonResponse);
        }
        product.setTotalProductCost(product.getProductCost() * product.getProductUnit());
        Product product1 = productService.addProduct(product);
        msg = "*** Created Product - " + product1.getProductName() + " ***";
        jsonResponse.setObject("200");
        jsonResponse.setMessage(msg);
        return ResponseEntity.ok(jsonResponse);
    }

    @GetMapping("{id}")
    public JsonResponse editProduct(@PathVariable("id") Long productId){
        Optional<Product> product = productService.getProductById(productId);
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.setResponseCode("200 OK");
        if(product.isPresent())
            jsonResponse.setObject(product.get());
        else jsonResponse.setMessage("Product not found for ID: " + productId);
        return jsonResponse;
    }

    @PutMapping("{id}")
    public JsonResponse updateProduct(@PathVariable("id") Long productId, @RequestBody Product product){
        product.setProductId(productId);
        Product savedProduct = productService.editProduct(product);
        String msg = "Updated Product - " + product.getProductName();
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.setMessage(msg);
        jsonResponse.setObject(savedProduct);
        jsonResponse.setResponseCode("200 OK");
        return jsonResponse;
    }

    @DeleteMapping("{id}")
    public JsonResponse deleteProduct(@PathVariable("id") Long productId) {
        Optional<Product> product = productService.getProductById(productId);
        productService.deleteProduct(product.get());
        String msg = "Deleted Product - " + product.get().getProductName() + " - " + product.get().getProductId();
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.setMessage(msg);
        jsonResponse.setResponseCode("200 OK");
        return jsonResponse;
    }

}
