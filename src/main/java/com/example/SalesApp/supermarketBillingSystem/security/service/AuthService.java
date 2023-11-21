package com.example.SalesApp.supermarketBillingSystem.security.service;

import com.example.SalesApp.supermarketBillingSystem.security.dto.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    JsonResponse SignUp(SignUpRequest signUpRequest);

    ResponseEntity<HttpHeaders> signIn(SignInRequest signInRequest) throws Exception;

    ResponseEntity<HttpHeaders> refreshToken(RefreshTokenRequest refreshTokenRequest) throws Exception;

    JsonResponse logout(SignInRequest signInRequest);
}
