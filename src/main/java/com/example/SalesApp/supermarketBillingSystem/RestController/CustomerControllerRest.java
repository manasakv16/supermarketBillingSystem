package com.example.SalesApp.supermarketBillingSystem.RestController;

import com.example.SalesApp.supermarketBillingSystem.Entity.Customer;
import com.example.SalesApp.supermarketBillingSystem.Service.CustomerService;
import com.example.SalesApp.supermarketBillingSystem.security.dto.JsonResponse;
import org.hibernate.boot.jaxb.internal.JaxpSourceXmlSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customer")
public class CustomerControllerRest {

    @Autowired
    private CustomerService customerService;

    @GetMapping("{id}")
    public ResponseEntity<JsonResponse> getCustomer(@PathVariable("id") String customerId){
        final JsonResponse jsonResponse = new JsonResponse();
        Optional<Customer> c = customerService.getCustomerById(customerId);
        if(c.isPresent()){
            jsonResponse.setObject(c);
        }
        else {
            jsonResponse.setMessage("No customer found with ID - " + customerId);
        }
        return ResponseEntity.ok(jsonResponse);
    }

    @PutMapping("{id}")
    public ResponseEntity<JsonResponse> updateCustomer(@PathVariable("id") String customerId, @RequestBody Customer customer){
        final Customer editedCustomer = customerService.editCustomer(customer);
        final JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.setMessage("Updated customer - " + editedCustomer.getCustomerMobile());
        return ResponseEntity.ok(jsonResponse);
    }

    @GetMapping
    public ResponseEntity<JsonResponse> getAllCustomer() {
        final JsonResponse jsonResponse = new JsonResponse();
        final List<Customer> customerList = customerService.getAllCustomer();
        jsonResponse.setMessage("Showing all customers");
        jsonResponse.setObject(customerList);
        return ResponseEntity.ok(jsonResponse);
    }
}

