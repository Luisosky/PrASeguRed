package co.edu.uniquindio.prasegured.repository;

import co.edu.uniquindio.prasegured.model.Rol;
import co.edu.uniquindio.prasegured.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void cleanup() {
        userRepository.deleteAll();
    }

    @Test
    public void testSaveAndFindUser() {
        // Dado: Crea una nueva instancia de usuario
        User user = new User(null, "Alice", "alice@example.com");
        user.setPassword("securePassword");
        user.setDateBirth(LocalDate.of(1990, 1, 1));
        user.setRol(Rol.USER);
        userRepository.save(user);

        // Cuando: Retorna el usuario por su id
        User foundUser = userRepository.findById(user.getId()).orElse(null);

        // Entonces: Verifa que el usuario este correctamente en la persistencia
        assertNotNull(foundUser, "The user should not be null.");
        assertEquals("Alice", foundUser.getName(), "User name should be 'Alice'.");
        assertEquals("alice@example.com", foundUser.getEmail(), "User email should be 'alice@example.com'.");
        assertEquals(LocalDate.of(1990, 1, 1), foundUser.getDateBirth(), "Date of birth should match");
        assertEquals(Rol.USER, foundUser.getRol(), "Role should be USER");
    }

    @Test
    public void testFindByEmail() {
        // Dado: Usuario guardado en la base de datos
        User user = new User(null, "Bob", "bob@example.com");
        userRepository.save(user);

        // Cuando: Se busca por email
        Optional<User> foundUser = userRepository.findByEmail("bob@example.com");
        Optional<User> notFoundUser = userRepository.findByEmail("nonexistent@example.com");

        // Entonces: Debe encontrar el usuario correcto y no encontrar el inexistente
        assertTrue(foundUser.isPresent(), "Should find a user with the email");
        assertEquals("Bob", foundUser.get().getName(), "Should find the correct user");
        assertFalse(notFoundUser.isPresent(), "Should not find a nonexistent email");
    }

    @Test
    public void testPagination() {
        // Dado: Varios usuarios en la base de datos
        for (int i = 1; i <= 15; i++) {
            User user = new User(null, "User" + i, "user" + i + "@example.com");
            userRepository.save(user);
        }

        // Cuando: Se solicita una página
        Page<User> firstPage = userRepository.findAll(PageRequest.of(0, 5, Sort.by("name")));
        Page<User> secondPage = userRepository.findAll(PageRequest.of(1, 5, Sort.by("name")));

        // Entonces: Debe retornar la paginación correcta
        assertEquals(5, firstPage.getContent().size(), "First page should have 5 users");
        assertEquals(5, secondPage.getContent().size(), "Second page should have 5 users");
        assertEquals(3, firstPage.getTotalPages(), "Should have 3 total pages");
        assertEquals(15, firstPage.getTotalElements(), "Should have 15 total elements");
        assertEquals("User1", firstPage.getContent().get(0).getName(), "Should be sorted by name");
    }
}