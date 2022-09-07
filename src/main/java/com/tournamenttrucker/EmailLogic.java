package com.tournamenttrucker;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class EmailLogic {
    public static void sendEmail(String player, String subject, String body)
    {
        sendEmail(new ArrayList<>(Arrays.asList(player)), subject, body);
    }
    public static void sendEmail(List<String> players, String subject, String body)
    {
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        // Get a Properties object
        Properties props = System.getProperties();
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
        props.put("mail.store.protocol", "pop3");
        props.put("mail.transport.protocol", "smtp");
        final String username = "leino333@gmail.com";//
        final String password = "hdfaolqwhsjypaun";

        // Sender's email ID needs to be mentioned
        String from = "leino333@gmail.com";

        try{
            Session session = Session.getDefaultInstance(props,
                    new Authenticator(){
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }});

            // Create a new message
            Message  message = new MimeMessage(session);

            for (String emailAddress : players)
            {
                // Set To: header field of the header.
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));
            }

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            message.setSubject("This is the Subject Line!" + subject);
            message.setText("This is actual message");

            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully...." + body);

        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
