package co.edu.uniquindio.prasegured.service;

import co.edu.uniquindio.prasegured.dto.UserRegistrationRequest;
import co.edu.uniquindio.prasegured.dto.VerificationRequest;
import co.edu.uniquindio.prasegured.exception.BadRequestException;
import co.edu.uniquindio.prasegured.exception.EmailExistException;
import co.edu.uniquindio.prasegured.exception.InternalServerException;
import co.edu.uniquindio.prasegured.exception.ResourceNotFoundException;
import co.edu.uniquindio.prasegured.model.Rol;
import co.edu.uniquindio.prasegured.model.User;
import co.edu.uniquindio.prasegured.model.VerificationCode;
import co.edu.uniquindio.prasegured.repository.UserRepository;
import co.edu.uniquindio.prasegured.repository.VerificationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final VerificationCodeRepository verificationCodeRepository;
    private final PasswordEncoder passwordEncoder;

    // Patron para validar la complejidad de la contraseña
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");

    @Autowired
    public UserService(UserRepository userRepository,
                       VerificationCodeRepository verificationCodeRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.verificationCodeRepository = verificationCodeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerUser(UserRegistrationRequest request) {
        try {
            // Verificar si el email ya está registrado
            if (userRepository.findByEmail(request.email()).isPresent()) {
                throw new EmailExistException("El email " + request.email() + " ya está registrado");
            }

            // Validar contraseña
            validatePassword(request.password());

            // Validar rol
            Rol userRole;
            try {
                userRole = Rol.valueOf(String.valueOf(request.rol()).toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Rol inválido: " + request.rol());
            }

            // Crear un nuevo usuario
            User newUser = new User();
            newUser.setEmail(request.email());
            newUser.setPassword(passwordEncoder.encode(request.password()));
            newUser.setName(request.fullName());
            newUser.setDateBirth(request.dateBirth());
            newUser.setRol(userRole.name());
            newUser.setEnabled(false); // Si la cuenta no esta verificada se inhabilita el usuario

            // Guarda y retorna un nuevo usuario
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

    private void validatePassword(String password) {
        if (password == null || password.length() < 8) {
            throw new BadRequestException("La contraseña debe tener al menos 8 caracteres");
        }

        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new BadRequestException("La contraseña debe contener al menos una letra mayúscula, " +
                    "una letra minúscula, un número y un carácter especial");
        }
    }

    @Transactional
    public String generateAndSaveVerificationCode(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con email " + email + " no encontrado"));

        // Borra cualquier código de verificación previo
        verificationCodeRepository.deleteByEmail(email);

        // Genera un código de verificación de 6 dígitos
        String code = generateSixDigitCode();

        // Crea un nuevo código de verificación
        VerificationCode verificationCode = new VerificationCode(
                email,
                code,
                LocalDateTime.now().plusMinutes(30) // Tiene que ser verificado en 30 minutos
        );

        verificationCodeRepository.save(verificationCode);

        return code;
    }

    private String generateSixDigitCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    @Transactional
    public boolean verifyCode(VerificationRequest request) {
        String code = request.codigoVerificacion();

        // Encontrar el codigo a verificar
        Optional<VerificationCode> verificationCodeOpt = verificationCodeRepository.findByCode(code);

        if (verificationCodeOpt.isEmpty()) {
            return false;
        }

        VerificationCode verificationCode = verificationCodeOpt.get();

        // Verificar si el código ha expirado o ya ha sido usado
        if (verificationCode.getExpiryDate().isBefore(LocalDateTime.now()) || verificationCode.isUsed()) {
            return false;
        }

        // Get associated user
        Optional<User> userOpt = userRepository.findByEmail(verificationCode.getEmail());
        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();

        // Activa la cuenta del usuario
        user.setEnabled(true);
        userRepository.save(user);

        // Marca el codigo como usado
        verificationCode.setUsed(true);
        verificationCodeRepository.save(verificationCode);

        return true;
    }

    public String authenticate(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("Credenciales inválidas"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadRequestException("Credenciales inválidas");
        }

        if (!user.isEnabled()) {
            throw new BadRequestException("La cuenta no está verificada. Por favor, verifica tu email");
        }

        // JWT TOKEN (AUN no implementado)
        return generateJwtToken(user);
    }

    private String generateJwtToken(User user) {
        // IMPLEMENTAR JWT TOKEN
        // Placeholder lolololol
        return "jwt-token-" + user.getId();
    }

    public boolean validateCredentials(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();
        return passwordEncoder.matches(password, user.getPassword()) && user.isEnabled();
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

            // Verifica si el email ya está siendo usado por otro usuario
            if (userDetails.getEmail() != null && !userDetails.getEmail().equals(user.getEmail()) &&
                    userRepository.findByEmail(userDetails.getEmail()).isPresent()) {
                throw new EmailExistException("El email " + userDetails.getEmail() + " ya está siendo usado por otro usuario");
            }

            // Actualiza los campos del usuario si es que no son nulas
            if (userDetails.getName() != null) {
                user.setName(userDetails.getName());
            }
            if (userDetails.getEmail() != null) {
                user.setEmail(userDetails.getEmail());
            }
            if (userDetails.getDateBirth() != null) {
                user.setDateBirth(userDetails.getDateBirth());
            }

            // Verifica la contraseña y la actualiza si pasa la validación
            if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
                validatePassword(userDetails.getPassword());
                user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
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

            User user = getUserById(id);
            verificationCodeRepository.deleteByEmail(user.getEmail());
        } catch (Exception e) {
            if (e instanceof ResourceNotFoundException) {
                throw e;
            }
            throw new InternalServerException("Error al eliminar el usuario: " + e.getMessage());
        }
    }

    @Transactional
    public void resendVerificationCode(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con email " + email + " no encontrado"));

        if (user.isEnabled()) {
            throw new BadRequestException("El usuario ya está verificado");
        }

        String code = generateAndSaveVerificationCode(email);

        // Enviar el código de verificación al email del usuario (No estoy seguro si hacerlo de esta manera lol)
        // emailService.sendVerificationEmail(email, code);
    }
}