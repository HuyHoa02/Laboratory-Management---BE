package com.chris.LaboratoryManagement.service;

import com.chris.LaboratoryManagement.dto.mail.DataMailDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;

//    public void sendHtmlMail(DataMailDTO dataMail) throws MessagingException {
//        MimeMessage message = mailSender.createMimeMessage();
//
//        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
//
//        // Build the HTML content
//        String htmlContent = buildHtmlContent(dataMail.getProps());
//
//
//        helper.setTo(dataMail.getTo());
//        helper.setSubject(dataMail.getSubject());
//        helper.setText(htmlContent, true);
//
//        mailSender.send(message);
//    }

    public void sendVerificationMail(DataMailDTO dataMail) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

        String htmlContent = verificationEmailBuilder(dataMail.getProps());

        helper.setTo(dataMail.getTo());
        helper.setSubject(dataMail.getSubject());
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    private String verificationEmailBuilder(Map<String, Object> props) {
        // Extract properties
        String firstName = (String) props.get("firstName");
        String lastName = (String) props.get("lastName");
        String code = (String) props.get("code");
        String id = (String) props.get("id");

        // Build HTML content
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html lang='en'>");
        html.append("<head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        html.append("<title>Email Verification</title>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; background-color: #f4f4f9; margin: 0; padding: 0; }");
        html.append(".container { background-color: #ffffff; padding: 20px; margin: 30px auto; max-width: 600px; ");
        html.append("border-radius: 10px; box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1); }");
        html.append("h1 { color: #333333; text-align: center; margin-bottom: 20px; }");
        html.append("p { font-size: 16px; color: #555555; line-height: 1.6; }");
        html.append(".code { font-size: 18px; color: #007BFF; font-weight: bold; margin-top: 10px; }");
        html.append(".footer { font-size: 14px; color: #888888; text-align: center; margin-top: 20px; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");
        html.append("<div class='container'>");
        html.append("<h1>Email Verification</h1>");
        html.append("<p>Dear ").append(firstName).append(" ").append(lastName).append(",</p>");
        html.append("<p>Thank you for signing up! Please use the information below to complete your registration:</p>");
        html.append("<p><strong>Verification ID:</strong> <span class='code'>").append(id).append("</span></p>");
        html.append("<p><strong>Verification Code:</strong> <span class='code'>").append(code).append("</span></p>");
        html.append("<p><i>This code will expire in 24 hours.</i></p>");
        html.append("<p>If you did not request this email, please disregard it.</p>");
        html.append("<hr>");
        html.append("<div class='footer'>");
        html.append("<p>Can Tho University - School of Information and Communication Technology</p>");
        html.append("<p>© ").append(java.time.Year.now()).append(" CIT, All Rights Reserved.</p>");
        html.append("</div>");
        html.append("</div>");
        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }

}