package co.edu.uniquindio.prasegured.repository;


import co.edu.uniquindio.prasegured.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveAndFindUser() {
        // Da: Crea una nueva instancia de usuario
        User user = new User(null, "Alice", "alice@example.com");
        userRepository.save(user);

        // Cuando: Retorna el usuario por su id
        User foundUser = userRepository.findById(user.getId()).orElse(null);

        // Entonces: Verifa que el usuario este correctamente en la persistencia
        assertNotNull(foundUser, "The user should not be null.");
        assertEquals("Alice", foundUser.getName(), "User name should be 'Alice'.");
        assertEquals("alice@example.com", foundUser.getEmail(), "User email should be 'alice@example.com'.");
    }
}

