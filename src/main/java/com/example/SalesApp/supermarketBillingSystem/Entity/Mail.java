package com.example.SalesApp.supermarketBillingSystem.Entity;

import lombok.Data;

@Data
public class Mail {

    private String to;
    private String subject;
    private String body;
    private String htmlTemplate;

}