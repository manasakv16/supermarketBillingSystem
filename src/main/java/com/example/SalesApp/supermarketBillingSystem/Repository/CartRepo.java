package com.example.SalesApp.supermarketBillingSystem.Repository;

import com.example.SalesApp.supermarketBillingSystem.Entity.Cart;
import com.example.SalesApp.supermarketBillingSystem.Entity.CartId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Repository
public interface CartRepo extends JpaRepository<Cart, CartId> {

    @Query("select c from Cart as c where c.salesId=?1")
    public List<Cart> getUserSpecificCart(Long salesId);

    @Query("select c.productId as productId from Cart as c where c.salesId=?1")
    public List<Long> getUserSpecificProductList(Long salesId);
}
