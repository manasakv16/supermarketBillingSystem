package com.example.SalesApp.supermarketBillingSystem.security.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.util.HashMap;

public interface JwtService {

    String generateToken(HashMap<String, Object> objectObjectHashMap, UserDetails userDetails) throws Exception;
    String extractUserName(String jwt) throws IOException;

    boolean isTokenValid(String jwt, UserDetails userDetails) throws IOException;

    String generateRefreshToken(HashMap<String, Object> objectObjectHashMap, UserDetails userDetails) throws Exception;

    public boolean verifyJwtToken(String token, UserDetails userDetails) throws IOException;
}
