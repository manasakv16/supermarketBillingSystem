package com.example.SalesApp.supermarketBillingSystem.security.dto;

import lombok.Data;

import java.util.List;

@Data
public class JsonResponse {

    private String responseCode;
    private String message;
    private Object object;
}
