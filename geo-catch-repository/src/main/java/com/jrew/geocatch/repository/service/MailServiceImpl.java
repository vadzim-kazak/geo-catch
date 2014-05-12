package com.jrew.geocatch.repository.service;

import com.jrew.geocatch.repository.model.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 5/9/14
 * Time: 4:23 AM
 */
@Service
public class MailServiceImpl implements MailService {

    public static final String ENCODING = "UTF-8";

    @Autowired
    JavaMailSender mailSender;

    @Value("#{emailProperties['email.to']}")
    String to;

    @Override
    public void sendMail(Email email) throws Exception {

        MimeMessage message = mailSender.createMimeMessage();

        email.setTo(to);

        StringBuffer emailMessage = new StringBuffer();

        if (!StringUtils.isEmpty(email.getName())) {
            emailMessage.append("User name: ")
                        .append(email.getName())
                        .append(System.getProperty("line.separator"));
        }

        emailMessage.append("Email: ")
                    .append(email.getFrom());

        if (!StringUtils.isEmpty(email.getMessage())) {
            emailMessage.append(System.getProperty("line.separator"))
                    .append(System.getProperty("line.separator"))
                    .append("Message: ")
                    .append(email.getMessage());
        }

        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email.getTo()));
        message.setSubject(email.getSubject(), ENCODING);
        message.setText(emailMessage.toString(), ENCODING);
        mailSender.send(message);
    }
}
