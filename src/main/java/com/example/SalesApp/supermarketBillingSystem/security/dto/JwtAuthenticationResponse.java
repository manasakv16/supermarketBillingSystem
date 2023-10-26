package com.example.SalesApp.supermarketBillingSystem.security.dto;

import lombok.Data;

@Data
public class JwtAuthenticationResponse {

    private String token;
    private String refreshToken;
}
