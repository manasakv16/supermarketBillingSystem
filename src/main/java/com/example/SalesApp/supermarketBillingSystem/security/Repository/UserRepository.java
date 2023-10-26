package com.example.SalesApp.supermarketBillingSystem.security.Repository;

import com.example.SalesApp.supermarketBillingSystem.security.Entity.Role;
import com.example.SalesApp.supermarketBillingSystem.security.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("select u from User u where u.email=?1")
    Optional<User> findByEmail(String email);

    User findByRole(Role role);

}
