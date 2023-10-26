package com.example.SalesApp.supermarketBillingSystem.security.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashMap;

public interface JwtService {

    String generateToken(UserDetails userDetails);
    String extractUserName(String jwt);

    boolean isTokenValid(String jwt, UserDetails userDetails);

    String generateRefreshToken(HashMap<String, Object> objectObjectHashMap, UserDetails userDetails);
}
