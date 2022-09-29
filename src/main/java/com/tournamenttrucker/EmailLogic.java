package com.tournamenttrucker;

import com.tournamenttrucker.models.PersonModel;
import com.tournamenttrucker.models.TournamentModel;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;

public class EmailLogic {

    public static void sendEmail(TournamentModel tournament,String teamName, List<PersonModel> players, Double prize)
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
        //TODO: add password
        final String password = "";

        // Sender's email ID needs to be mentioned
        String from = "leino333@gmail.com";
        String to = "leino333@gmail.com";

        try{
            Session session = Session.getDefaultInstance(props,
                    new Authenticator(){
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }});

            // Create a new message
            Message  message = new MimeMessage(session);

            for (PersonModel player : players)
            {
                // Set To: header field of the header.
//                to = player.getEmailAddress();
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            }

            String subject = "Tournament completed";
            StringBuilder body = new StringBuilder();
            body.append("Hello to: ");
            body.append(teamName);
            body.append(" team player");
            body.append("\n");
            body.append("Thank you for participating in the tournament: ");
            body.append(tournament.getTournamentName());
            body.append("\n");
            body.append("Your team won the prize: ");
            body.append(prize);
            body.append("\n");
            body.append("Thanks for a great tournament everyone!");

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            message.setSubject(subject);
            message.setText(body.toString());

            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully...." + body);

        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
