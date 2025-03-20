package co.edu.uniquindio.prasegured.service;

import co.edu.uniquindio.prasegured.dto.SubscriptionRequest;
import co.edu.uniquindio.prasegured.exception.BadRequestException;
import co.edu.uniquindio.prasegured.exception.EmailExistException;
import co.edu.uniquindio.prasegured.model.Subscriber;
import co.edu.uniquindio.prasegured.repository.SubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class SubscriptionService {

    private final SubscriberRepository subscriberRepository;
    private final EmailService emailService;

    @Autowired
    public SubscriptionService(SubscriberRepository subscriberRepository, EmailService emailService) {
        this.subscriberRepository = subscriberRepository;
        this.emailService = emailService;
    }

    @Transactional
    public void subscribe(SubscriptionRequest request) {
        String email = request.email();

        // Check if email already exists
        if (subscriberRepository.findByEmail(email).isPresent()) {
            throw new EmailExistException("Este correo ya está suscrito a nuestro boletín");
        }

        // Validate email format
        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new BadRequestException("Formato de correo electrónico inválido");
        }

        // Create new subscriber
        Subscriber subscriber = new Subscriber();
        subscriber.setEmail(email);
        subscriber.setSubscribedAt(LocalDateTime.now());
        subscriber.setActive(true);

        // Save to database
        subscriberRepository.save(subscriber);

        // Send confirmation email
        emailService.sendSubscriptionConfirmation(email);
    }
}