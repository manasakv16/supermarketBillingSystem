package com.example.SalesApp.supermarketBillingSystem.Service;

import com.example.SalesApp.supermarketBillingSystem.Entity.Cart;
import com.example.SalesApp.supermarketBillingSystem.Entity.CartId;
import com.example.SalesApp.supermarketBillingSystem.Repository.CartRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public Optional<Cart> findCartById(CartId cartId) {
        return cartRepo.findById(cartId);
    }

    @Override
    public List<Cart> getAllCart() {
        return cartRepo.findAll();
    }

    @Override
    public List<Cart> getEntireUserCart(Long salesId){
        List<Cart> productIdList = new ArrayList<>();
        productIdList = cartRepo.getUserSpecificCart(salesId);
        return productIdList;
    }

    @Override
    public List<Long> getProductIds(Long salesId){
        List<Long> productIdList = cartRepo.getUserSpecificProductList(salesId);
        return productIdList;
    }

}
