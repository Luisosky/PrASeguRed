package co.edu.uniquindio.prasegured.service;

import co.edu.uniquindio.prasegured.exception.BadRequestException;
import co.edu.uniquindio.prasegured.exception.EmailExistException;
import co.edu.uniquindio.prasegured.exception.ResourceNotFoundException;
import co.edu.uniquindio.prasegured.model.Rol;
import co.edu.uniquindio.prasegured.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserRegistrationRequest validRequest;
    private User existingUser;

    @BeforeEach
    void setUp() {
        // Configurar un usuario válido para pruebas
        validRequest = new UserRegistrationRequest(
                "test@example.com",
                "Password1",
                "Test User",
                LocalDate.of(1990, 1, 1),
                Rol.USER
        );

        existingUser = new User("1", "Existing User", "existing@example.com");
        existingUser.setPassword("encodedPassword");
        existingUser.setDateBirth(LocalDate.of(1985, 5, 15));
        existingUser.setRol(Rol.USER);
    }

    @Test
    void registerUser_Success() {
        // Dado que el usuario no existe
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> {
            User user = i.getArgument(0);
            user.setId("generatedId");
            return user;
        });

        // Obtener el usuario registrado
        User result = userService.registerUser(validRequest);

        // Verificar campos del usuario
        assertNotNull(result);
        assertEquals("generatedId", result.getId());
        assertEquals(validRequest.email(), result.getEmail());
        assertEquals(validRequest.fullName(), result.getName());
        assertEquals("encodedPassword", result.getPassword());

        verify(userRepository).findByEmail(validRequest.email());
        verify(passwordEncoder).encode(validRequest.password());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUser_EmailExists_ThrowsException() {
        // Dado que el email ya existe por ende el usuario tambien
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(existingUser));

        // Enconces capturar la excepción
        assertThrows(EmailExistException.class, () ->
                userService.registerUser(validRequest)
        );

        verify(userRepository).findByEmail(validRequest.email());
        verifyNoMoreInteractions(passwordEncoder, userRepository);
    }

    @Test
    void getUserById_Success() {
        // Encontrar el usuario existente
        when(userRepository.findById("1")).thenReturn(Optional.of(existingUser));

        // Retornar el objeto tipo usuario
        User result = userService.getUserById("1");

        // Verificar que el usuario no sea nulo
        assertNotNull(result);
        assertEquals(existingUser.getId(), result.getId());
        assertEquals(existingUser.getName(), result.getName());

        verify(userRepository).findById("1");
    }

    //ID no encontrado
    @Test
    void getUserById_NotFound_ThrowsException() {
        // Encontrar el usuario inexistente
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        // Capturar la excepción
        assertThrows(ResourceNotFoundException.class, () ->
                userService.getUserById("999")
        );

        verify(userRepository).findById("999");
    }

    //ID nulo
    @Test
    void getUserById_NullId_ThrowsException() {
        // Capturar la excepción
        assertThrows(BadRequestException.class, () ->
                userService.getUserById(null)
        );

        verifyNoInteractions(userRepository);
    }

    @Test
    void updateUser_Success() {
        // Dado que el usuario existente no tenga ID
        User updatedDetails = new User(null, "Updated Name", "existing@example.com");
        when(userRepository.findById("1")).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        // Se actualiza el usuario con un ID "1"
        User result = userService.updateUser("1", updatedDetails);

        // Verifica el cambio de nombre y que el email no haya cambiado
        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        assertEquals(existingUser.getEmail(), result.getEmail());
        // Busca el usuario por el ID verificando que existe
        verify(userRepository).findById("1");
        verify(userRepository).save(existingUser);
    }

    @Test
    void updateUser_WithNewEmail_EmailConflict_ThrowsException() {
        // Dado que los dos usuarios tienen el mismo email
        User updatedDetails = new User(null, "Updated Name", "new@example.com");
        User conflictUser = new User("2", "Another User", "new@example.com");

        when(userRepository.findById("1")).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.of(conflictUser));

        // Capturar la excepción
        assertThrows(EmailExistException.class, () ->
                userService.updateUser("1", updatedDetails)
        );

        verify(userRepository).findById("1");
        verify(userRepository).findByEmail("new@example.com");
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void deleteUser_Success() {
        // Dado que el usuario existe con ID 1
        when(userRepository.existsById("1")).thenReturn(true);
        doNothing().when(userRepository).deleteById("1");

        // Borrar dicho usuario
        userService.deleteUser("1");

        // Verificar que el usuario fue eliminado
        verify(userRepository).existsById("1");
        verify(userRepository).deleteById("1");
    }

    @Test
    void deleteUser_NotFound_ThrowsException() {
        // Dado que el usuario con ID 999 no exista
        when(userRepository.existsById("999")).thenReturn(false);

        // Capture la excepción
        assertThrows(ResourceNotFoundException.class, () ->
                userService.deleteUser("999")
        );

        verify(userRepository).existsById("999");
        verify(userRepository, never()).deleteById(anyString());
    }
}