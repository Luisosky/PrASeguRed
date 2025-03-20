package co.edu.uniquindio.prasegured.controller;

import co.edu.uniquindio.prasegured.dto.SubscriptionRequest;
import co.edu.uniquindio.prasegured.model.Subscriber;
import co.edu.uniquindio.prasegured.repository.SubscriberRepository;
import co.edu.uniquindio.prasegured.service.EmailService;
import co.edu.uniquindio.prasegured.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private final EmailService emailService;
    private final SubscriptionService subscriptionService;
    private final SubscriberRepository subscriberRepository;

    @Autowired
    public TestController(EmailService emailService, SubscriptionService subscriptionService,
                          SubscriberRepository subscriberRepository) {
        this.emailService = emailService;
        this.subscriptionService = subscriptionService;
        this.subscriberRepository = subscriberRepository;
    }

    @GetMapping("/email/{to}")
    public ResponseEntity<String> testSimpleEmail(@PathVariable String to) {
        emailService.sendSimpleMessage(to, "Test Email", "This is a test email from PrASeguRed");
        return ResponseEntity.ok("Email sent to " + to);
    }

    @GetMapping("/html-email/{to}")
    public ResponseEntity<String> testHtmlEmail(@PathVariable String to) {
        String htmlContent = "<h1>Test HTML Email</h1><p>This is a test HTML email from PrASeguRed</p>";
        emailService.sendHtmlMessage(to, "Test HTML Email", htmlContent);
        return ResponseEntity.ok("HTML Email sent to " + to);
    }

    @GetMapping("/subscription/{email}")
    public ResponseEntity<String> testSubscription(@PathVariable String email) {
        subscriptionService.subscribe(new SubscriptionRequest(email));
        return ResponseEntity.ok("Subscription processed for " + email);
    }

    @GetMapping("/subscribers")
    public ResponseEntity<List<Subscriber>> getAllSubscribers() {
        List<Subscriber> subscribers = subscriberRepository.findAll();
        return ResponseEntity.ok(subscribers);
    }
}