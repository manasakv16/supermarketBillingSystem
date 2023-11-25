package com.example.SalesApp.supermarketBillingSystem.RestController;

import com.example.SalesApp.supermarketBillingSystem.Entity.Mail;
import com.example.SalesApp.supermarketBillingSystem.security.service.EmailService;
import jakarta.mail.MessagingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/mail")
@RestController
public class MailController {

    @Autowired
    private EmailService emailService;

    private static final Logger logger = LogManager.getLogger(MailController.class.getName());

    // send simple mail
    public String send(@RequestBody Mail mail){
        emailService.sendMailSimpleText(mail.getTo(), mail.getSubject(), mail.getBody());
        logger.info("Mail sent to - " + mail.getTo());
        return "Mail sent";
    }

    // send html mail
    public String sendHtml(@RequestBody Mail mail) throws MessagingException {
        emailService.sendMailHTML(mail.getTo(), mail.getSubject(), mail.getBody());
        logger.info("Mail sent to - " + mail.getTo());
        return "Mail sent";
    }

    // send simple mail with attachment
    @PostMapping
    public String sendMailWithAttachment(@RequestBody Mail mail) throws MessagingException {
        emailService.sendMailWithAttachment(mail.getTo(), mail.getSubject(), mail.getBody());
        logger.info("Mail sent to - " + mail.getTo());
        return "Mail sent";
    }


}
