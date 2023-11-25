package com.example.SalesApp.supermarketBillingSystem.security.dto;

import lombok.Data;

@Data
public class Password {

    private String oldPassword;
    private String newPassword;
    private String newPasswordAgain;

}
