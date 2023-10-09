package com.example.SalesApp.supermarketBillingSystem.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @RequestMapping("/homePage")
    public String home(){
        return "homePage";
    }




}
