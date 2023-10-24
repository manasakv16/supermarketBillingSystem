package com.example.SalesApp.supermarketBillingSystem.Service;

import com.example.SalesApp.supermarketBillingSystem.Entity.Cart;
import com.example.SalesApp.supermarketBillingSystem.Entity.CartId;

import java.util.List;

public interface CartService {
    Cart addCart(Cart cart);
    Cart editCart(Cart cart);
    void deleteCart(Cart cart);
    Cart findCartById(CartId cartId);
    List<Cart> getAllCart();
    List<Cart> getEntireUserCart(Long salesId);
    List<Long> getProductIds(Long salesId);

}
