package com.example.SalesApp.supermarketBillingSystem.Service;

import com.example.SalesApp.supermarketBillingSystem.Entity.Customer;
import com.example.SalesApp.supermarketBillingSystem.Repository.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CustomerServiceImpl implements CustomerService{

    @Autowired
    private CustomerRepo customerRepo;
    @Override
    public Customer addCustomer(Customer customer) {
        return customerRepo.save(customer);
    }

    @Override
    public Customer editCustomer(Customer customer) {
        return customerRepo.save(customer);
    }

    @Override
    public void deleteCustomer(String customerMobile) {
        customerRepo.deleteById(customerMobile);
    }

    @Override
    public Customer getCustomerById(String customerMobile) {
        return customerRepo.getReferenceById(customerMobile);
    }

    @Override
    public List<Customer> getAllCustomer() {
        return customerRepo.findAll();
    }

    @Override
    public boolean isCustomerValid(String customerMobile) {
        return customerMobile != null && customerMobile.length() == 10;
    }

    @Override
    public boolean isCustomerValid(Customer customer) {
        List<Customer> customerList = customerRepo.findAll();
        for(Customer c : customerList) {
            if(Objects.equals(c.getCustomerMobile(), customer.getCustomerMobile())) {
                return true;
            }
        }
        return false;
    }
}
