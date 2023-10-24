package com.example.SalesApp.supermarketBillingSystem.Controller;

import com.example.SalesApp.supermarketBillingSystem.Entity.Customer;
import com.example.SalesApp.supermarketBillingSystem.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @RequestMapping("/newCustomer")
    public String newCustomer(){
        return "addCustomer";
    }
    @RequestMapping("/addCustomer")
    public String addCustomer(@ModelAttribute("customer")Customer customer, final ModelMap modelMap){
        String msg;
        if(customerService.isCustomerValid(customer.getCustomerMobile())) {
            final Customer addedCustomer = customerService.addCustomer(customer);
            msg = "Added Customer - " + customer.getCustomerName();
        }
        else {
            msg = "Error creating Customer: Mobile number is either empty or invalid.";
        }
        modelMap.addAttribute("msg", msg);
        return "addCustomer";
    }

    @RequestMapping("/editCustomer")
    public String editCustomer(){
        return "editCustomer";
    }

    @RequestMapping("/updateCustomer")
    public String updateCustomer(@ModelAttribute("customer") Customer customer, final ModelMap modelMap){
        final Customer editedCustomer = customerService.editCustomer(customer);
        modelMap.addAttribute("msg", "Updated customer : " + editedCustomer.getCustomerName());
        return "showAllCustomer";
    }

    @RequestMapping("/deleteCustomer")
    public String deleteCustomer(@RequestParam("customerId") String customerMobile, final ModelMap modelMap){
        customerService.deleteCustomer(customerMobile);
        modelMap.addAttribute("msg", "Deleted Customer - " + customerMobile);
        return "showAllCustomer";
    }

    @RequestMapping("/getAllCustomer")
    public String getAllCustomer(final ModelMap modelMap) {
        final List<Customer> customerList = customerService.getAllCustomer();
        modelMap.addAttribute("customer", customerList);
        return "showAllCustomer";
    }
}

