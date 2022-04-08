package com.micropay.webcash.services;

import com.micropay.webcash.model.EmailRequest;
import org.springframework.stereotype.Component;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import static com.micropay.webcash.utils.Logger.logError;
import static com.micropay.webcash.utils.Logger.logInfo;

@Component
public class EmailService {

    public static void sendEmail(EmailRequest data) {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", WebcashControllerService.schemaConfig.getSmtpServer());
        prop.put("mail.smtp.port", WebcashControllerService.schemaConfig.getSmtpPort());
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); // TLS
        prop.put("mail.smtp.ssl.enable", "true"); // SSL

        Session session = Session.getInstance(prop, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(WebcashControllerService.schemaConfig.getEmailUserName(), WebcashControllerService.schemaConfig.getEmailPassword());
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(WebcashControllerService.schemaConfig.getEmailUserName()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(data.getEmailReceipient()));
            message.setSubject(data.getEmailSubject());
            message.setText(data.getMessageBody());
            Transport.send(message);
        } catch (MessagingException e) {
            logInfo("====================== Email Details ============== Email Address: " + data.getEmailReceipient() + " Email Body: " + data.getMessageBody());
            logError(e);
        }
    }
}
