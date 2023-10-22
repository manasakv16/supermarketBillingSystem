package com.example.SalesApp.supermarketBillingSystem.Service;

import com.example.SalesApp.supermarketBillingSystem.Entity.Customer;

import java.util.List;

public interface CustomerService {

    Customer addCustomer(Customer customer);
    Customer editCustomer(Customer customer);
    void deleteCustomer(Customer customer);
    Customer getCustomerById(String customerMobile);
    List<Customer> getAllCustomer(Customer customer);
}
