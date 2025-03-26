package co.edu.uniquindio.prasegured;

import co.edu.uniquindio.prasegured.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CorreoApplication implements CommandLineRunner {

    @Autowired
    private EmailService emailService;

    public static void main(String[] args) {
        SpringApplication.run(CorreoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Replace with your email details
        String to = "finiasyfer29@gmail.com";
        String subject = "Test Email";
        String text = "This is a test email sent from Spring Boot application.";

        emailService.sendSimpleEmail(to, subject, text);
        System.out.println("Email sent successfully");
    }
}