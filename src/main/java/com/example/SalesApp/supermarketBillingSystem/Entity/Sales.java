package com.example.SalesApp.supermarketBillingSystem.Entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
public class Sales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long salesId;
    private String customerName;
    private String customerMobile;
    private String customerEmail;
    private String productId;
    private Integer unitCount;
    private Double total;
    private Date salesDate;

    public Sales() {
    }

    public Sales(Long salesId, String customerName, String customerMobile, String customerEmail, String productId, Integer unitCount, Double total, Date salesDate) {
        this.salesId = salesId;
        this.customerName = customerName;
        this.customerMobile = customerMobile;
        this.customerEmail = customerEmail;
        this.productId = productId;
        this.unitCount = unitCount;
        this.total = total;
        this.salesDate = Date.from(Instant.now());
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

    public Date getSalesDate() {
        return salesDate;
    }

    public void setSalesDate(Date salesDate) {
        this.salesDate = salesDate;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getUnitCount() {
        return unitCount;
    }

    public void setUnitCount(Integer unitCount) {
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
