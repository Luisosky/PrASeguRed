package co.edu.uniquindio.prasegured.service;

import co.edu.uniquindio.prasegured.dto.UserRegistrationRequest;
import co.edu.uniquindio.prasegured.exception.BadRequestException;
import co.edu.uniquindio.prasegured.exception.EmailExistException;
import co.edu.uniquindio.prasegured.exception.InternalServerException;
import co.edu.uniquindio.prasegured.exception.ResourceNotFoundException;
import co.edu.uniquindio.prasegured.model.User;
import co.edu.uniquindio.prasegured.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerUser(UserRegistrationRequest request) {
        try {
            // Verifica si el email ya está registrado
            if (userRepository.findByEmail(request.email()).isPresent()) {
                throw new EmailExistException("El email " + request.email() + " ya está registrado");
            }

            if (request.password().length() < 8) {
                throw new BadRequestException("La contraseña debe tener al menos 8 caracteres");
            }

            // Crea un nuevo usuario por el request de registro
            User newUser = new User();
            newUser.setEmail(request.email());
            newUser.setPassword(passwordEncoder.encode(request.password()));
            newUser.setName(request.fullName());
            newUser.setDateBirth(request.dateBirth());
            newUser.setRol(request.rol());

            // Guarda y retorna el nuevo usuario
            return userRepository.save(newUser);
        } catch (Exception e) {
            if (e instanceof ResourceNotFoundException ||
                    e instanceof EmailExistException ||
                    e instanceof BadRequestException) {
                throw e;
            }
            throw new InternalServerException("Error al registrar el usuario: " + e.getMessage());
        }
    }

    public User getUserById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new BadRequestException("El ID no puede estar vacío");
        }

        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con ID " + id + " no encontrado"));
    }

    @Transactional
    public User updateUser(String id, User userDetails) {
        try {
            User user = getUserById(id);

            // Verifica que el email no esté siendo usado por otro usuario
            if (userDetails.getEmail() != null && !userDetails.getEmail().equals(user.getEmail()) &&
                    userRepository.findByEmail(userDetails.getEmail()).isPresent()) {
                throw new EmailExistException("El email " + userDetails.getEmail() + " ya está siendo usado por otro usuario");
            }

            // Actualiza los campos del usuario si no son nulos
            if (userDetails.getName() != null) {
                user.setName(userDetails.getName());
            }
            if (userDetails.getEmail() != null) {
                user.setEmail(userDetails.getEmail());
            }
            if (userDetails.getDateBirth() != null) {
                user.setDateBirth(userDetails.getDateBirth());
            }

            return userRepository.save(user);
        } catch (Exception e) {
            if (e instanceof ResourceNotFoundException ||
                    e instanceof EmailExistException ||
                    e instanceof BadRequestException) {
                throw e;
            }
            throw new InternalServerException("Error al actualizar el usuario: " + e.getMessage());
        }
    }

    @Transactional
    public void deleteUser(String id) {
        try {
            if (!userRepository.existsById(id)) {
                throw new ResourceNotFoundException("Usuario con ID " + id + " no encontrado");
            }
            userRepository.deleteById(id);
        } catch (Exception e) {
            if (e instanceof ResourceNotFoundException) {
                throw e;
            }
            throw new InternalServerException("Error al eliminar el usuario: " + e.getMessage());
        }
    }
}