package com.example.SalesApp.supermarketBillingSystem.Service;

import com.example.SalesApp.supermarketBillingSystem.Entity.Cart;
import com.example.SalesApp.supermarketBillingSystem.Repository.CartRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    private CartRepo cartRepo;

    @Override
    public Cart addCart(Cart cart) {
        return cartRepo.save(cart);
    }

    @Override
    public Cart editCart(Cart cart) {
        return cartRepo.save(cart);
    }

    @Override
    public void deleteCart(Cart cart) {
       cartRepo.delete(cart);
    }

    @Override
    public Cart findCartById(Long id) {
        return cartRepo.getReferenceById(id);
    }

    @Override
    public List<Cart> getAllCart(Cart cart) {
        return cartRepo.findAll();
    }
}
