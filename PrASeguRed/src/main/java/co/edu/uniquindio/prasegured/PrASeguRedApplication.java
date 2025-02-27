package co.edu.uniquindio.prasegured;

import co.edu.uniquindio.prasegured.model.User;
import co.edu.uniquindio.prasegured.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PrASeguRedApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrASeguRedApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(UserRepository userRepository) {
        return args -> {
            // Save a new user
            User user = new User(null, "John Doe", "john.doe@example.com");
            userRepository.save(user);

            // List all users
            userRepository.findAll().forEach(System.out::println);
        };
    }

}
