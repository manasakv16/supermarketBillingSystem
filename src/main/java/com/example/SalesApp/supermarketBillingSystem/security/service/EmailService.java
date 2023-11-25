package com.example.SalesApp.supermarketBillingSystem.security.service;

import jakarta.mail.MessagingException;

public interface EmailService {

    void sendMailSimpleText(String to, String subject, String body);

    void sendMailHTML(String to, String subject, String body) throws MessagingException;

    void sendMailWithAttachment(String to, String subject, String body) throws MessagingException;
    void sendMailWithAttachment(String to, String subject, String body, byte[] attachment, String fileName) throws MessagingException;

}
