package application.walliedev;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSender {

    public static void sendEmail(String to, String subject, String username) throws MessagingException {
        final String from = "mywallieapp@gmail.com";
        final String password = "ewrayvwkjkkzxswk";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        String htmlContent =
                "<html>" +
                        "<body style='margin: 0; padding: 0; background-color: #f3f3f3; font-family: \"Segoe UI\", Tahoma, Geneva, Verdana, sans-serif;'>" +

                        "<table width='100%' cellspacing='0' cellpadding='0' style='padding: 40px 0;'>" +
                        "  <tr>" +
                        "    <td align='center'>" +
                        "      <table width='600' style='background-color: #ffffff; border-radius: 12px; padding: 40px 30px; box-shadow: 0 8px 20px rgba(0,0,0,0.08);'>" +
                        "        <tr><td>" +

                        "<h1 style='text-align: center; color: #6200ee; font-size: 26px;'>🚀 Welcome to Wallie, " + username + "!</h1>" +

                        "<p style='font-size: 18px; color: #333; text-align: center; line-height: 1.6; margin-top: 20px;'>We're so glad you've joined 📈<br>Let Wallie help you budget smarter and save better 💰</p>" +

                        "<div style='display: flex; justify-content: center; margin: 30px 0;'>" +
                        "  <a href='#' style='display: inline-block; padding: 14px 24px; font-size: 16px; font-weight: 600; color: white; background-color: #6200ee; border-radius: 50px; text-decoration: none; text-align: center; width: 100%; max-width: 260px; box-shadow: 0 4px 12px rgba(98,0,238,0.3);'>Start Budgeting Now</a>" +
                        "</div>" +

                        "<p style='font-size: 16px; color: #444; line-height: 1.6; margin-bottom: 0;'>Here’s what you can do with Wallie:</p>" +
                        "<ul style='color: #555; font-size: 16px; margin-top: 10px; padding-left: 20px;'>" +
                        "  <li>🎯 Create budgets for health, shopping, travel & more</li>" +
                        "  <li>📅 Track your daily payments easily</li>" +
                        "  <li>📊 Visualize your progress and optimize your savings</li>" +
                        "</ul>" +

                        "<hr style='border: none; border-top: 1px solid #eee; margin: 30px 0;'>" +

                        "<p style='font-size: 14px; color: #888; text-align: center;'>If you didn’t sign up for Wallie, please ignore this email.</p>" +

                        "        </td></tr>" +
                        "      </table>" +
                        "    </td>" +
                        "  </tr>" +
                        "</table>" +

                        "</body>" +
                        "</html>";



        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);



        message.setContent(htmlContent, "text/html; charset=utf-8");

        System.out.println("sending...");
        Transport.send(message);
    }
}
