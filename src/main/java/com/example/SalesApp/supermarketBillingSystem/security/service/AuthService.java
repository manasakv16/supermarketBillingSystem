package com.example.SalesApp.supermarketBillingSystem.security.service;

import com.example.SalesApp.supermarketBillingSystem.security.Entity.User;
import com.example.SalesApp.supermarketBillingSystem.security.dto.JwtAuthenticationResponse;
import com.example.SalesApp.supermarketBillingSystem.security.dto.RefreshTokenRequest;
import com.example.SalesApp.supermarketBillingSystem.security.dto.SignInRequest;
import com.example.SalesApp.supermarketBillingSystem.security.dto.SignUpRequest;

public interface AuthService {

    public User SignUp(SignUpRequest signUpRequest);

    public JwtAuthenticationResponse signIn(SignInRequest signInRequest);

    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
