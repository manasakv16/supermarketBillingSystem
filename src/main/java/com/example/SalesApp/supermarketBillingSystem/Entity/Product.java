package com.example.SalesApp.supermarketBillingSystem.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;
    private String productName;
    private Double productCost;
    private String productCompany;
    private String productType;
    private String productDescription;

    public Product() {
    }

    public Product(Long productId, String productName, Double productCost, String productCompany, String productType, String productDescription) {
        this.productId = productId;
        this.productName = productName;
        this.productCost = productCost;
        this.productCompany = productCompany;
        this.productType = productType;
        this.productDescription = productDescription;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getProductCost() {
        return productCost;
    }

    public void setProductCost(Double productCost) {
        this.productCost = productCost;
    }

    public String getProductCompany() {
        return productCompany;
    }

    public void setProductCompany(String productCompany) {
        this.productCompany = productCompany;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productCost=" + productCost +
                ", productCompany='" + productCompany + '\'' +
                ", productType='" + productType + '\'' +
                ", productDescription='" + productDescription + '\'' +
                '}';
    }
}
