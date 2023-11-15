package com.example.SalesApp.supermarketBillingSystem.Service;

import com.example.SalesApp.supermarketBillingSystem.Entity.Cart;
import com.example.SalesApp.supermarketBillingSystem.Entity.CartId;

import java.util.List;
import java.util.Optional;

public interface CartService {
    Cart addCart(Cart cart);
    Cart editCart(Cart cart);
    void deleteCart(Cart cart);
    Optional<Cart> findCartById(CartId cartId);
    List<Cart> getAllCart();
    List<Cart> getEntireUserCart(Long salesId);
    List<Long> getProductIds(Long salesId);

}
