package com.example.mjukvarukonstruktionbokningserver.commons;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Sendemail {

    public Sendemail() {
    }

    public static void sendMail(String username, String reason, String booker) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("kthbooking@gmail.com","KthBookingGrupp5");
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("kthbooking@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(username));
            message.setSubject("Bokning av grupprum/projektrum");

            if(reason.equals("booked"))
                message.setText(booker + " has booked a room with you. Check your bookings.");
            else if(reason.equals("overbook"))
                message.setText("Teacher or admin " + booker + " has taken the room, but you will get your hours back.");
            else if(reason.equals("removed"))
                message.setText("Your fellow booker " + booker + " has removed your appointment, you will get your hours back.");

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}