package com.example.SalesApp.supermarketBillingSystem.Entity;

import jakarta.persistence.*;
import java.time.LocalDate;


@Entity
public class Sales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long salesId;
    private String customerName;
    private String customerMobile;
    private String customerEmail;
    private String productId;
    private String unitCount;
    private Double total;
    private LocalDate salesDate;

    public Sales() {
        this.salesDate = LocalDate.now();
    }


    public Sales(Long salesId, String customerName, String customerMobile,
                 String customerEmail, String productId, String unitCount,
                 Double total) {
        this.salesId = salesId;
        this.customerName = customerName;
        this.customerMobile = customerMobile;
        this.customerEmail = customerEmail;
        this.productId = productId;
        this.unitCount = unitCount;
        this.total = total;
        this.salesDate = LocalDate.now();
    }

    public Long getSalesId() {
        return salesId;
    }

    public void setSalesId(Long salesId) {
        this.salesId = salesId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public LocalDate getSalesDate() {
        return salesDate;
    }

    public void setSalesDate(LocalDate salesDate) {
        this.salesDate = salesDate;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUnitCount() {
        return unitCount;
    }

    public void setUnitCount(String unitCount) {
        this.unitCount = unitCount;
    }

    @Override
    public String toString() {
        return "Sales{" +
                "salesId=" + salesId +
                ", customerName='" + customerName + '\'' +
                ", customerMobile='" + customerMobile + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                ", productId=" + productId +
                ", unitCount=" + unitCount +
                ", total=" + total +
                ", salesDate=" + salesDate +
                '}';
    }
}
