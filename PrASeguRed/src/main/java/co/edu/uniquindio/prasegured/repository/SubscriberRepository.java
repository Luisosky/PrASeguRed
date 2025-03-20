package co.edu.uniquindio.prasegured.repository;

import co.edu.uniquindio.prasegured.model.Subscriber;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SubscriberRepository extends MongoRepository<Subscriber, String> {
    Optional<Subscriber> findByEmail(String email);
}