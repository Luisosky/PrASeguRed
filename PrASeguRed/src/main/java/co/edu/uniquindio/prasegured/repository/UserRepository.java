package co.edu.uniquindio.prasegured.repository;

import co.edu.uniquindio.prasegured.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    //metodos query
    Optional<User> findByEmail(String email);
}

