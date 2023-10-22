package com.example.SalesApp.supermarketBillingSystem.Service;

import com.example.SalesApp.supermarketBillingSystem.Entity.Cart;

import java.util.List;

public interface CartService {
    Cart addCart(Cart cart);
    Cart editCart(Cart cart);
    void deleteCart(Cart cart);
    Cart findCartById(Long id);
    List<Cart> getAllCart(Cart cart);

}
