package com.example.SalesApp.supermarketBillingSystem.security.service;

import com.example.SalesApp.supermarketBillingSystem.security.Entity.Role;
import com.example.SalesApp.supermarketBillingSystem.security.Entity.User;
import com.example.SalesApp.supermarketBillingSystem.security.Repository.UserRepository;
import com.example.SalesApp.supermarketBillingSystem.security.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

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
            jsonResponse.setMessage("User created: " + user1.getFirstName());
        }
        else {
            jsonResponse.setResponseCode("403");
            jsonResponse.setMessage("User failed to create, password not strong enough. It should be min of length 8 having a lower case char, upper case char, a number & a special char");
        }
        return jsonResponse;
    }

    boolean passwordVerifier(String password){
        Pattern passwordChecker = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{8,20}$");
        if(password == null)
            return false;
        else {
            Matcher m = passwordChecker.matcher(password);
            return m.matches();
        }
    }

    public JwtAuthenticationResponse signIn(SignInRequest signInRequest) throws Exception {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                (signInRequest.getEmail(), signInRequest.getPassword()));
        final var user = userRepository.findByEmail(signInRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password"));

        final HashMap<String, Object> role = new HashMap<>();
        role.put("role", user.getRole().name());
        role.put("key", Math.random());

        var token = jwtService.generateToken(role, user);
        var refreshToken = jwtService.generateRefreshToken(role, user);

        final JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken(token);
        jwtAuthenticationResponse.setRefreshToken(refreshToken);

        storeUserKeyInDB(role, user);
        return jwtAuthenticationResponse;

    }

    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) throws Exception {
        String userEmail = jwtService.extractUserName(refreshTokenRequest.getToken());
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid user or password"));

        if(jwtService.isTokenValid(refreshTokenRequest.getToken(), user)) {
            final HashMap<String, Object> role = new HashMap<>();
            role.put("role", user.getRole());
            role.put("key", Math.random());
            var token = jwtService.generateToken(role, user);

            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
            jwtAuthenticationResponse.setToken(token);
            jwtAuthenticationResponse.setRefreshToken(refreshTokenRequest.getToken());
            System.out.println("token: " + token);
            System.out.println("refreshToken: " + refreshTokenRequest.getToken());
            return jwtAuthenticationResponse;
        }
        return  null;
    }

    public void storeUserKeyInDB(HashMap<String, Object> role, User user){
        user.setKey(role.get("key").toString());
        User save = userRepository.save(user);
    }

    public JsonResponse logout(SignInRequest signInRequest){
        final JsonResponse jsonResponse = new JsonResponse();
        Optional<User> user = userRepository.findByEmail(signInRequest.getEmail());
        if(user.isPresent()) {
            user.get().setKey(null);
            User save = userRepository.save(user.get());
            jsonResponse.setMessage("Logged out user - " + signInRequest.getEmail());
        }
        else {
            jsonResponse.setMessage("No user found with mail - " + signInRequest.getEmail());
        }
        return  jsonResponse;
    }
}
