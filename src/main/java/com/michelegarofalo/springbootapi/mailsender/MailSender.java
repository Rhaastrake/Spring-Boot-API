package com.michelegarofalo.springbootapi.mailsender;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Properties;

//Allowed origins
@CrossOrigin(origins = {
        "https://www.webfixer.it"
})

// mailSender class initialization for Spring
@RestController
@RequestMapping("mailSender")
public class MailSender {

    @Value("${mail.username}")
    private String username;
    @Value("${mail.password}")
    private String password;
    @Value("${mail.host}")
    private String host;
    @Value("${mail.port}")
    private int port;

    // sendMail Method initialization for Spring
    @PostMapping("sendMail")
    public ResponseEntity<String> sendMail(
            @RequestParam(value = "formType", required = false) String formType,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "details", required = false) String details,
            @RequestParam(value = "serviceSpecification", required = false) String serviceSpecification,
            @RequestParam(value = "pcType", required = false) String pcType,
            @RequestParam(value = "useType", required = false) String useType,
            @RequestParam(value = "priceRange", required = false) String priceRange,
            @RequestParam(value = "secondHand", required = false) String secondHand,
            @RequestParam(value = "display", required = false) String display,
            @RequestParam(value = "dbIntegration", required = false) String dbIntegration,
            @RequestParam(value = "expiryDate", required = false) String expiryDate
    ) {
        System.out.println(">> sendMail method called");

        // Cleaning all input params
        formType = clean(formType);
        name = clean(name);
        phoneNumber = safeNum(phoneNumber);
        city = clean(city);
        details = clean(details);
        serviceSpecification = clean(serviceSpecification);
        pcType = clean(pcType);
        useType = clean(useType);
        priceRange = clean(priceRange);
        secondHand = clean(secondHand);
        display = clean(display);
        dbIntegration = clean(dbIntegration);
        expiryDate = clean(expiryDate);

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", String.valueOf(port));

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(username));

            // Filter of mal subject reading formType input
            String subject = switch (formType) {
                case "assistance" -> "Richiesta assistenza | WebFixer.it";
                case "purchaseGuide" -> "Richiesta nuovo PC | WebFixer.it";
                case "discord" -> "Richiesta server discord | WebFixer.it";
                case "application" -> "Richiesta applicazione | WebFixer.it";
                case "bugReport" -> "Segnalazione bug | WebFixer.it";
                default -> "Richiesta generica | WebFixer.it";
            };
            message.setSubject(subject);

            // Body of the mail
            StringBuilder body = new StringBuilder("<h2>Nuova richiesta dal sito WebFixer.it</h2>");
            if (!details.isEmpty()) body.append("<h3>Dettagli aggiuntivi:</h3><p>").append(details).append("</p>");
            if (!name.isEmpty()) body.append("<h3>Nome:</h3><p>").append(name).append("</p>");
            if (!phoneNumber.isEmpty()) body.append("<h3>Telefono:</h3><p>").append(phoneNumber).append("</p>");
            if (!city.isEmpty()) body.append("<h3>Città:</h3><p>").append(city).append("</p>");
            if (!serviceSpecification.isEmpty()) body.append("<h3>Servizio richiesto:</h3><p>").append(serviceSpecification).append("</p>");
            if (!pcType.isEmpty()) body.append("<h3>Tipo PC:</h3><p>").append(pcType).append("</p>");
            if (!useType.isEmpty()) body.append("<h3>Tipo di utilizzo:</h3><p>").append(useType).append("</p>");
            if (!priceRange.isEmpty()) body.append("<h3>Range di prezzo:</h3><p>").append(priceRange).append("</p>");
            if (!secondHand.isEmpty()) body.append("<h3>Componenti usati:</h3><p>").append(secondHand).append("</p>");
            if (!display.isEmpty()) body.append("<h3>Display:</h3><p>").append(display).append("</p>");
            if (!dbIntegration.isEmpty()) body.append("<h3>Database:</h3><p>").append(dbIntegration).append("</p>");
            if (!expiryDate.isEmpty()) body.append("<h3>Data di scadenza:</h3><p>").append(expiryDate).append("</p>");

            message.setContent(body.toString(), "text/html; charset=UTF-8");

            Transport.send(message);

            return ResponseEntity.ok("✅ Dati inviati con successo");

        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("❌ Errore nell'invio della mail");
        }
    }

    // Text cleaning and safing
    private String clean(String value) {
        if (value == null) return "";
        return value.trim().replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;");
    }
    // Num cleaning and safing
    private String safeNum(String value) {
        if (value == null) return "";
        return value.replaceAll("[^0-9]", "");
    }
}