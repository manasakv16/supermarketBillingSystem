package com.example.SalesApp.supermarketBillingSystem.Service;

import com.example.SalesApp.supermarketBillingSystem.Entity.Customer;

import java.util.List;

public interface CustomerService {

    Customer addCustomer(Customer customer);
    Customer editCustomer(Customer customer);
    void deleteCustomer(String customerMobile);
    Customer getCustomerById(String customerMobile);
    List<Customer> getAllCustomer();
    boolean isCustomerValid(String customerMobile);
    boolean isCustomerValid(Customer getCustomer);
}
