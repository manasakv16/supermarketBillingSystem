package com.example.SalesApp.supermarketBillingSystem.Entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;

import java.io.Serializable;


@Embeddable
public class CartId implements Serializable {

    private Long salesId;
    private Long productId;

    public CartId(){}
    public CartId(Long salesId, Long productId) {
        this.salesId = salesId;
        this.productId = productId;
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

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "CartId{" +
                "salesId=" + salesId +
                ", productId=" + productId +
                '}';
    }
}

