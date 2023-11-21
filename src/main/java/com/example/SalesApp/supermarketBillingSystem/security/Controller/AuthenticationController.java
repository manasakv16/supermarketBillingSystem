package com.example.SalesApp.supermarketBillingSystem.security.Controller;

import com.example.SalesApp.supermarketBillingSystem.security.dto.*;
import com.example.SalesApp.supermarketBillingSystem.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.logging.Logger;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthService authService;
    private static final Logger logger = Logger.getLogger(AuthenticationController.class.getName());

    @PostMapping("/signup")
    public ResponseEntity<JsonResponse> signUp(@RequestBody SignUpRequest signUpRequest){
        logger.info("user signUp - " + signUpRequest.toString());
        final JsonResponse jsonResponse = new JsonResponse();
        JsonResponse ok = (authService.SignUp(signUpRequest));
        jsonResponse.setMessage(ok.getMessage());
        return ResponseEntity.ok(jsonResponse);
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signIn(@RequestBody SignInRequest signInRequest) throws Exception {
        logger.info("user signIn - " + signInRequest.getEmail());
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.setMessage("Token generated successfully");
        return new ResponseEntity<>(jsonResponse.toString(), authService.signIn(signInRequest).getBody(), HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) throws Exception {
        logger.info("refreshed token - " + refreshTokenRequest);
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.setMessage("Token refreshed successfully");
        return new ResponseEntity<>(jsonResponse.toString(), authService.refreshToken(refreshTokenRequest).getBody(), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<JsonResponse> logout(@RequestBody SignInRequest signInRequest){
        logger.info("user logged out successfully- " + signInRequest.getEmail());
        JsonResponse logout = authService.logout(signInRequest);
        return ResponseEntity.ok(logout);
    }
}
