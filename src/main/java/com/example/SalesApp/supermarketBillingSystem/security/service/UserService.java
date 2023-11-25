package com.example.SalesApp.supermarketBillingSystem.security.service;

import com.example.SalesApp.supermarketBillingSystem.security.Entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService {

    UserDetailsService userDetailsService();

    User addUser(User user);
    User updateUser(User user);
    void deleteUser(Integer id);
    Optional<User> getUserById(Integer id);
    Optional<User> getUserByEmail(String email);
}
