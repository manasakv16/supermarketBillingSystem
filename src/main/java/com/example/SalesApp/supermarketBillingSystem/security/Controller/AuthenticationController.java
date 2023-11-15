package com.example.SalesApp.supermarketBillingSystem.security.Controller;

import com.example.SalesApp.supermarketBillingSystem.security.Entity.User;
import com.example.SalesApp.supermarketBillingSystem.security.dto.*;
import com.example.SalesApp.supermarketBillingSystem.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<JsonResponse> signUp(@RequestBody SignUpRequest signUpRequest){
        final JsonResponse jsonResponse = new JsonResponse();
        JsonResponse ok = (authService.SignUp(signUpRequest));
        jsonResponse.setMessage(ok.getMessage());
        return ResponseEntity.ok(jsonResponse);
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> signIn(@RequestBody SignInRequest signInRequest) throws Exception {
        return ResponseEntity.ok(authService.signIn(signInRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) throws Exception {
        JwtAuthenticationResponse jwtAuthenticationResponse = authService.refreshToken(refreshTokenRequest);
        return ResponseEntity.ok(jwtAuthenticationResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<JsonResponse> logout(@RequestBody SignInRequest signInRequest){
        JsonResponse logout = authService.logout(signInRequest);
        return ResponseEntity.ok(logout);
    }
}
