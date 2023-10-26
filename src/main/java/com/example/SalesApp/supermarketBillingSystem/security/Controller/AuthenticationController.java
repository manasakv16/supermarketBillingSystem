package com.example.SalesApp.supermarketBillingSystem.security.Controller;

import com.example.SalesApp.supermarketBillingSystem.security.Entity.User;
import com.example.SalesApp.supermarketBillingSystem.security.dto.JwtAuthenticationResponse;
import com.example.SalesApp.supermarketBillingSystem.security.dto.RefreshTokenRequest;
import com.example.SalesApp.supermarketBillingSystem.security.dto.SignInRequest;
import com.example.SalesApp.supermarketBillingSystem.security.dto.SignUpRequest;
import com.example.SalesApp.supermarketBillingSystem.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<User> signUp(@RequestBody SignUpRequest signUpRequest){
        return ResponseEntity.ok(authService.SignUp(signUpRequest));
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signIn(@RequestBody SignInRequest signInRequest){
        return ResponseEntity.ok(authService.signIn(signInRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest){
        JwtAuthenticationResponse jwtAuthenticationResponse = authService.refreshToken(refreshTokenRequest);
        return ResponseEntity.ok(jwtAuthenticationResponse);
    }
}
