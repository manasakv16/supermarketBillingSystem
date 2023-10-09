package com.example.SalesApp.supermarketBillingSystem.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class Sales {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long salesId;
    private String customerId;
    private String customerName;
    private String customerMobile;
    private String customerEmail;
    private Double total;
    private Date salesDate;

    public Sales() {
    }

    public Sales(Long salesId, String customerId, String customerName, String customerMobile, String customerEmail, Double total, Date salesDate) {
        this.salesId = salesId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerMobile = customerMobile;
        this.customerEmail = customerEmail;
        this.total = total;
        this.salesDate = salesDate;
    }

    public Long getSalesId() {
        return salesId;
    }

    public void setSalesId(Long salesId) {
        this.salesId = salesId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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

    @Override
    public String toString() {
        return "Sales{" +
                "salesId=" + salesId +
                ", customerId='" + customerId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", customerMobile='" + customerMobile + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                ", total=" + total +
                ", salesDate=" + salesDate +
                '}';
    }
}
