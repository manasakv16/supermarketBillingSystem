package com.example.SalesApp.supermarketBillingSystem.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String productName;
    private Integer productUnit;
    private Double productCost;
    private Double totalProductCost;
    private String productCompany;
    private String productType;
    private String productDescription;

    public Product() {
    }

    public Product(Long productId, String productName, Integer productUnit, Double productCost,
                   String productCompany, String productType, String productDescription) {
        this.productId = productId;
        this.productName = productName;
        this.productUnit = productUnit;
        this.productCost = productCost;
        this.totalProductCost = productCost * productUnit;
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

    public Integer getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(Integer productUnit) {
        this.productUnit = productUnit;
    }

    public Double getTotalProductCost() {
        return totalProductCost;
    }

    public void setTotalProductCost(Double totalProductCost) {
        this.totalProductCost = totalProductCost;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productUnit=" + productUnit +
                ", productCost=" + productCost +
                ", totalProductCost=" + totalProductCost +
                ", productCompany='" + productCompany + '\'' +
                ", productType='" + productType + '\'' +
                ", productDescription='" + productDescription + '\'' +
                '}';
    }
}
