package com.example.SalesApp.supermarketBillingSystem.security.service;

import com.example.SalesApp.supermarketBillingSystem.security.dto.*;

public interface AuthService {

    public JsonResponse SignUp(SignUpRequest signUpRequest);

    public JwtAuthenticationResponse signIn(SignInRequest signInRequest) throws Exception;

    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) throws Exception;

    public JsonResponse logout(SignInRequest signInRequest);
}
