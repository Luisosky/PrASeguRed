package co.edu.uniquindio.prasegured.repository;

import co.edu.uniquindio.prasegured.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    //metodos query
}

