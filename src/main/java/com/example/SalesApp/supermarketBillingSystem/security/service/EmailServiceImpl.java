package com.example.SalesApp.supermarketBillingSystem.security.service;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService{

    @Autowired
    private JavaMailSender javaMailSender;

    // Simple text mail
    @Override
    public void sendMailSimpleText(final String to, final String subject, final String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
//        message.setTo(new String[] {"", ""}); // for multiple recipients
        message.setSubject(subject);
        message.setText(body);
        javaMailSender.send(message);
    }

    @Override
    public void sendMailHTML(final String to, final String subject, final String body) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        message.setFrom(new InternetAddress("")); // add from address
        message.setRecipients(Message.RecipientType.TO, to); // add to address
        message.setSubject(subject);

        // example html data
//        String body ="<h1>This is an invitation for our dinner.</h1>\n" +
//                "<p>At <strong>Forger</strong> residence.</p> \n Let us know. Tataaa";

        message.setContent(body, "text/html; charset=utf-8");
        javaMailSender.send(message);
    }

    public void sendMailWithHtmlTemplate() throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        message.setFrom(new InternetAddress(""));
        message.setRecipients(Message.RecipientType.TO, "");
        message.setSubject("");

        String htmlTemplate = "";

//        htmlTemplate.replace()

        message.setContent(htmlTemplate, "text/html; charset=utf-8");
        javaMailSender.send(message);
    }

    @Override
    public void sendMailWithAttachment(final String to, final String subject, final String body) throws MessagingException {

        final MimeMessage message = javaMailSender.createMimeMessage();

        message.setFrom(new InternetAddress("")); // add from address
        final MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText("Below is the html text", body);

        FileSystemResource file = new FileSystemResource("thanksForShopping.jfif");
        helper.addAttachment("attachmentFile.jpeg", file);
        javaMailSender.send(message);
    }

    // send bill as an attahcment
    public void sendMailWithAttachment(final String to, final String subject, final String body, final byte[] attachment, final String fileName) throws MessagingException {

        final MimeMessage message = javaMailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setCc(""); // cc address
        helper.setSubject(subject);
        message.setContent(body, "text/html; charset=utf-8");
        helper.setText("", body);

        final DataSource dataSource = new ByteArrayDataSource(attachment, "application/pdf");
        final MimeBodyPart pdfBodyPart = new MimeBodyPart();
        pdfBodyPart.setDataHandler(new DataHandler(dataSource));
        pdfBodyPart.setFileName(fileName);
        helper.getMimeMultipart().addBodyPart(pdfBodyPart, 1);
        message.setContent(helper.getMimeMultipart());

        javaMailSender.send(message);
    }
}
