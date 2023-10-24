package com.example.SalesApp.supermarketBillingSystem.Entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;


@Entity
public class Sales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long salesId;
    private String customerId;
    private Double total;
    private LocalDate salesDate;

    public Sales() {
        this.salesDate = LocalDate.now();
    }

    public Sales(Long salesId, String customerId, LocalDate salesDate) {
        this.salesId = salesId;
        this.customerId = customerId;
        this.total = 0.0;
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

    @Override
    public String toString() {
        return "Sales{" +
                "salesId=" + salesId +
                ", customerId='" + customerId + '\'' +
                ", total=" + total +
                ", salesDate=" + salesDate +
                '}';
    }
}
