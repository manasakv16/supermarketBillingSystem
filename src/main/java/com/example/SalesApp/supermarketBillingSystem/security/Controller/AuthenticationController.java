package com.example.SalesApp.supermarketBillingSystem.security.Controller;

import com.example.SalesApp.supermarketBillingSystem.security.Entity.User;
import com.example.SalesApp.supermarketBillingSystem.security.dto.*;
import com.example.SalesApp.supermarketBillingSystem.security.service.AuthService;
import com.example.SalesApp.supermarketBillingSystem.security.service.EmailService;
import com.example.SalesApp.supermarketBillingSystem.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthService authService;
    private final UserService userService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
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

    @PostMapping("/forgot/{id}")
    public ResponseEntity<JsonResponse> forgotPassword(@PathVariable("id") Integer id){
        final Optional<User> user = userService.getUserById(id);
        final JsonResponse jsonResponse =new JsonResponse();
        if (user.isPresent()) {
            String password = authService.generateRandomPassword();
            emailService.sendMailSimpleText(user.get().getEmail(), "Password reset request",
                    "Hi " + user.get().getFirstName() +" \n You can use below password to login to our application. " +
                            "Do note this password should be changed during your first login. \n \n " + password);

            user.get().setPassword(passwordEncoder.encode(password));
            user.get().setResetPassword(true);
            User updateUser = userService.updateUser(user.get());
            logger.info("Sent temporary password to user - " + id);
            jsonResponse.setMessage("Sent temporary password to user - " + id);
        }
        else jsonResponse.setMessage("Invalid user ID.");
        return ResponseEntity.ok(jsonResponse);
    }

    @PostMapping("/reset/{id}")
    public ResponseEntity<JsonResponse> resetPassword(@PathVariable("id") Integer id, @RequestBody Password password){
        final Optional<User> user = userService.getUserById(id);
        final JsonResponse jsonResponse = new JsonResponse();

        if(user.isPresent()) {
            if (user.get().getResetPassword()) {
                final Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                        (user.get().getEmail(), password.getOldPassword()));
                if (authenticate.isAuthenticated()){
                    final JsonResponse jsonResponse1 = authService.resetPassword(password.getNewPassword(), password.getNewPasswordAgain(), user.get().getEmail());
                    return ResponseEntity.ok(jsonResponse1);
                }
                else {
                    jsonResponse.setMessage("Incorrect user password - " + id);
                    logger.info("Incorrect user password - " + id);
                }
            }
            else {
                jsonResponse.setMessage("Invalid/ Bad request");
                logger.info("Invalid/ Bad request.");
            }
        }
        else {
            jsonResponse.setMessage("Invalid/ Bad request");
            logger.info("Invalid/ Bad request.");
        }
        return ResponseEntity.ok(jsonResponse);
    }

}
