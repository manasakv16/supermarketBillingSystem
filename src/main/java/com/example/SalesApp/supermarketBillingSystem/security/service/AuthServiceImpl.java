package com.example.SalesApp.supermarketBillingSystem.security.service;

import com.example.SalesApp.supermarketBillingSystem.security.Entity.Role;
import com.example.SalesApp.supermarketBillingSystem.security.Entity.User;
import com.example.SalesApp.supermarketBillingSystem.security.Repository.UserRepository;
import com.example.SalesApp.supermarketBillingSystem.security.dto.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmailService emailService;
    private static final Logger logger = Logger.getLogger(AuthServiceImpl.class.getName());

    public JsonResponse SignUp(final SignUpRequest signUpRequest) {
        final User user = new User();
        final JsonResponse jsonResponse = new JsonResponse();

        if( passwordVerifier(signUpRequest.getPassword())) {
            user.setFirstName(signUpRequest.getFirstName());
            user.setLastName(signUpRequest.getLastName());
            user.setEmail(signUpRequest.getEmail());
            user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
            user.setRole(Role.USER);
            User user1 = userRepository.save(user);
            jsonResponse.setResponseCode("200");
            jsonResponse.setMessage("User signed up: " + user1.getFirstName());
            logger.info("User signed up: " + user1.getFirstName());
        }
        else {
            jsonResponse.setResponseCode("403");
            jsonResponse.setMessage("User failed to create, password not strong enough. It should be min of length 8 having a lower case char, upper case char, a number & a special char");
            logger.info("user not created, password is not strong");
        }
        return jsonResponse;
    }

    public ResponseEntity<HttpHeaders> signIn(SignInRequest signInRequest) throws Exception {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                (signInRequest.getEmail(), signInRequest.getPassword()));
        final var user = userRepository.findByEmail(signInRequest.getEmail())
                .orElseThrow(() -> {
                    new UsernameNotFoundException("Invalid username or password");
                    logger.info("User sign In failed, Invalid username or password - " + signInRequest);
                    return null;
                });
        if(user.getResetPassword()) {
            throw new RuntimeException("User need to reset password before accessing application");
        }

        logger.info("user signed in - " + user.getEmail());
        final HashMap<String, Object> role = new HashMap<>();
        role.put("role", user.getRole().name());
        role.put("key", Math.random());

        var token = jwtService.generateToken(role, user);
        var refreshToken = jwtService.generateRefreshToken(role, user);

        storeUserKeyInDB(role, user);

        final HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        headers.set("refreshAuthorization", refreshToken);
        return ResponseEntity.ok(headers);
    }

    public JsonResponse resetPassword(final String newPassword, final String newPasswordAgain, final String email) {
        final Optional<User> user = userRepository.findByEmail(email);
        final JsonResponse jsonResponse = new JsonResponse();
        if (newPassword.equals(newPasswordAgain)) {
            if(passwordVerifier(newPassword)) {
                user.get().setResetPassword(false);
                user.get().setPassword(passwordEncoder.encode(newPassword));
                final User saved = userRepository.save(user.get());
                logger.info("Update user password - " + email);
                emailService.sendMailSimpleText(email, "Password reset successful",
                        "Hi " + user.get().getFirstName() +" \n \n Your password was changed successfully. Regards,\n Supermarket Team\n ");
            }
            else {
                jsonResponse.setMessage("Password not strong enough");
            }
        }
        else {
            jsonResponse.setMessage("Both passwords do not match");
        }
        return jsonResponse;
    }

    public ResponseEntity<HttpHeaders> refreshToken(RefreshTokenRequest refreshTokenRequest) throws Exception {
        String userEmail = jwtService.extractUserName(refreshTokenRequest.getToken());
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> {
                    new UsernameNotFoundException("Invalid user or password");
                    logger.info("Invalid user or password found in refresh token. Token not refreshed");
                    return null;
                });

        if(jwtService.isTokenValid(refreshTokenRequest.getToken(), user)) {
            final HashMap<String, Object> role = new HashMap<>();
            role.put("role", user.getRole());
            role.put("key", Math.random());
            var token = jwtService.generateToken(role, user);
            storeUserKeyInDB(role, user);
            logger.info("token refreshed");

            final HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            return ResponseEntity.ok(headers);
        }
        logger.info("Invalid token. Token not refreshed");
        return  null;
    }

    public void storeUserKeyInDB(HashMap<String, Object> role, User user){
        user.setKey(role.get("key").toString());
        User save = userRepository.save(user);
        logger.info("updated user key in DB - " + user.getEmail());
    }

    public JsonResponse logout(SignInRequest signInRequest){
        final JsonResponse jsonResponse = new JsonResponse();
        final Optional<User> user = userRepository.findByEmail(signInRequest.getEmail());
        if(user.isPresent()) {
            user.get().setKey(null);
            final User save = userRepository.save(user.get());
            jsonResponse.setMessage("Logged out user - " + signInRequest.getEmail());
            logger.info("Logged out user - " + signInRequest.getEmail());
        }
        else {
            jsonResponse.setMessage("No user found with mail - " + signInRequest.getEmail());
            logger.info("Invalid user passed in request. Logout not applicable");
        }
        return  jsonResponse;
    }

    public boolean passwordVerifier(String password){
        Pattern passwordChecker = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{8,20}$");
        if(password == null)
            return false;
        else {
            Matcher m = passwordChecker.matcher(password);
            return m.matches();
        }
    }

    public String generateRandomPassword() {
        // generate a string of upper case letters having length 2
        String upperCaseStr = RandomStringUtils.random(2, 65, 90, true, true);

        // generate a string of lower case letters having length 2
        String lowerCaseStr = RandomStringUtils.random(2, 97, 122, true, true);

        // generate a string of numeric letters having length 2
        String numbersStr = RandomStringUtils.randomNumeric(2);

        // generate a string of special chars having length 2
        String specialCharStr = RandomStringUtils.random(2, 33, 47, false, false);

        // generate a string of alphanumeric letters having length 2
        String totalCharsStr = RandomStringUtils.randomAlphanumeric(2);

        // concatenate all the strings into a single one
        String demoPassword = upperCaseStr.concat(lowerCaseStr)
                                          .concat(numbersStr)
                                          .concat(specialCharStr)
                                          .concat(totalCharsStr);

        // create a list of Char that stores all the characters, numbers and special characters
        List<Character> listOfChar = demoPassword.chars()
                                                 .mapToObj(data -> (char) data)
                                                 .collect(Collectors.toList());

        // use shuffle() method of the Collections to shuffle the list elements
        Collections.shuffle(listOfChar);

        //generate a random string(secure password) by using list stream() method and collect() method
        final String password = listOfChar.stream()
                                          .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                                          .toString();
        return password;
    }
}
