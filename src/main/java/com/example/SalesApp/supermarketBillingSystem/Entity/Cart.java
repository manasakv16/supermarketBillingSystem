package com.example.SalesApp.supermarketBillingSystem.Entity;

import jakarta.persistence.Entity;

@Entity
public class Cart {
    private Long salesId;
    private Long productId;
    private int productCount;

    public Cart() {
    }

    public Cart(Long salesId, Long productId, int productCount) {
        this.salesId = salesId;
        this.productId = productId;
        this.productCount = productCount;
    }

    public Long getSalesId() {
        return salesId;
    }

    public void setSalesId(Long salesId) {
        this.salesId = salesId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "salesId=" + salesId +
                ", productId=" + productId +
                ", productCount=" + productCount +
                '}';
    }
}
