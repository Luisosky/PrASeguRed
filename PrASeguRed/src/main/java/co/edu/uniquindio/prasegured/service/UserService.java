package co.edu.uniquindio.prasegured.service;

import co.edu.uniquindio.prasegured.dto.UserRegistrationRequest;
import co.edu.uniquindio.prasegured.model.User;
import co.edu.uniquindio.prasegured.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(UserRegistrationRequest request) {
        // Verifica si el email ya esta registrado
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        // Crea un nuevo usuario desde la request de registracion
        User newUser = new User();
        newUser.setEmail(request.email());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setName(request.fullName());
        newUser.setDateBirth(request.dateBirth());
        newUser.setRol(request.rol());

        // Guarda y retorna el nuevo usuario
        return userRepository.save(newUser);
    }
}