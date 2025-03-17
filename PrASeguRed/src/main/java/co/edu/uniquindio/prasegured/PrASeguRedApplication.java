package co.edu.uniquindio.prasegured;

import co.edu.uniquindio.prasegured.model.User;
import co.edu.uniquindio.prasegured.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate; // Add this import if needed

@SpringBootApplication
public class PrASeguRedApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrASeguRedApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(UserRepository userRepository) {
        return args -> {
            // Guarda un nuevo usuario usando el constructor disponible
            User user = new User("johndoe", "John Doe", "john.doe@example.com", LocalDate.now(), "password");
            userRepository.save(user);

            // Lista todos los usuarios
            userRepository.findAll().forEach(System.out::println);
        };
    }
}