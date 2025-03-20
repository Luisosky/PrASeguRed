package co.edu.uniquindio.prasegured.controller;

import co.edu.uniquindio.prasegured.model.Rol;
import co.edu.uniquindio.prasegured.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;
    private HttpHeaders headers;

    @BeforeEach
    void setUp() {
        // Limpia la base de datos para testear
        userRepository.deleteAll();

        // Crea un nuevo usuario para testear
        testUser = new User();
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setPassword(passwordEncoder.encode("Password123"));
        testUser.setDateBirth(LocalDate.of(1990, 1, 1));
        testUser.setRol(Rol.USER);
        testUser = userRepository.save(testUser);

        // Configura los headers para JSON
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }
    // Obtener todos los usuarios
    @Test
    void getAllUsers_Success() {
        ResponseEntity<User[]> response = restTemplate.getForEntity("/api/users", User[].class);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().length);
    }
    // Obtener un usuario por su id
    @Test
    void getUserById_Success() {

        ResponseEntity<User> response = restTemplate.getForEntity(
                "/api/users/" + testUser.getId(), User.class);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testUser.getId(), response.getBody().getId());
        assertEquals(testUser.getName(), response.getBody().getName());
        assertEquals(testUser.getEmail(), response.getBody().getEmail());
    }
    // Obtener un usuario por su id que no existe, debe retornar 404
    @Test
    void getUserById_NotFound_Returns404() {

        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                "/api/users/nonexistentId",
                HttpMethod.GET,
                null,
                ErrorResponse.class);


        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().status());
        assertTrue(response.getBody().message().contains("Usuario con ID"));
    }
    // Registar usuario
    @Test
    void registerUser_Success() {

        UserRegistrationRequest request = new UserRegistrationRequest(
                "newuser@example.com",
                "Password123",
                "New User",
                LocalDate.of(1995, 5, 15),
                Rol.USER
        );


        ResponseEntity<User> response = restTemplate.postForEntity(
                "/api/auth/register",
                request,
                User.class);


        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("New User", response.getBody().getName());
        assertEquals("newuser@example.com", response.getBody().getEmail());
        assertNotNull(response.getBody().getId());
    }
    // Registar usuario con email existente, retorna error 409
    @Test
    void registerUser_EmailExists_Returns409() {
        // Given
        UserRegistrationRequest request = new UserRegistrationRequest(
                testUser.getEmail(), // Usar email existente, del test user
                "Password123",
                "Duplicate User",
                LocalDate.of(1995, 5, 15),
                Rol.USER
        );


        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                "/api/auth/register",
                HttpMethod.POST,
                new HttpEntity<>(request, headers),
                ErrorResponse.class);


        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(409, response.getBody().status());
        assertTrue(response.getBody().message().contains("ya está registrado"));
    }
    // Registrar usuario, request invalido, retorna error 400
    @Test
    void registerUser_InvalidRequest_Returns400() {
        // Dado - email invalido
        UserRegistrationRequest request = new UserRegistrationRequest(
                "not-an-email",
                "Password123",
                "Invalid User",
                LocalDate.of(1995, 5, 15),
                Rol.USER
        );


        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                "/api/auth/register",
                HttpMethod.POST,
                new HttpEntity<>(request, headers),
                ErrorResponse.class);


        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().status());
        assertTrue(response.getBody().details().toString().contains("email"));
    }
    // Actualizar usuario
    @Test
    void updateUser_Success() {

        User updateRequest = new User();
        updateRequest.setName("Updated Name");
        updateRequest.setEmail(testUser.getEmail());


        ResponseEntity<User> response = restTemplate.exchange(
                "/api/users/" + testUser.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(updateRequest, headers),
                User.class);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated Name", response.getBody().getName());
        assertEquals(testUser.getEmail(), response.getBody().getEmail());
    }
    // Actualizar usuario con email existente, retorna error 409
    @Test
    void updateUser_EmailConflict_Returns409() {
        // Se necesita crear otro usuario
        User anotherUser = new User();
        anotherUser.setName("Another User");
        anotherUser.setEmail("another@example.com");
        anotherUser.setPassword(passwordEncoder.encode("Password123"));
        anotherUser.setDateBirth(LocalDate.of(1992, 2, 2));
        anotherUser.setRol(Rol.USER);
        userRepository.save(anotherUser);

        // Intenta actualizar el update user con el email del otro usuario
        User updateRequest = new User();
        updateRequest.setName("Updated Name");
        updateRequest.setEmail("another@example.com");


        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                "/api/users/" + testUser.getId(),
                HttpMethod.PUT,
                new HttpEntity<>(updateRequest, headers),
                ErrorResponse.class);


        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(409, response.getBody().status());
        assertTrue(response.getBody().message().contains("ya está siendo usado"));
    }
    // Eliminar usuario
    @Test
    void deleteUser_Success() {

        ResponseEntity<Void> response = restTemplate.exchange(
                "/api/users/" + testUser.getId(),
                HttpMethod.DELETE,
                null,
                Void.class);


        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertFalse(userRepository.existsById(testUser.getId()));
    }
    // Eliminar usuario que no existe, retorna 404
    @Test
    void deleteUser_NotFound_Returns404() {

        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                "/api/users/nonexistentId",
                HttpMethod.DELETE,
                null,
                ErrorResponse.class);


        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().status());
        assertTrue(response.getBody().message().contains("no encontrado"));
    }
    // Paginacion de usuarios
    @Test
    void getPagedUsers_Success() {
        // DAdo - Añadir mas usuarios para la paginacion
        for (int i = 0; i < 15; i++) {
            User user = new User();
            user.setName("Paged User " + i);
            user.setEmail("paged" + i + "@example.com");
            user.setPassword(passwordEncoder.encode("Password123"));
            user.setDateBirth(LocalDate.of(1990, 1, 1));
            user.setRol(Rol.USER);
            userRepository.save(user);
        }

        // Cuando - Obtener la primera pagina con 5 elementos (usuarios)
        Map<String, Object> params = new HashMap<>();
        params.put("page", 0);
        params.put("size", 5);

        ResponseEntity<Map> response = restTemplate.getForEntity(
                "/api/users/paged?page={page}&size={size}",
                Map.class,
                params);


        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        @SuppressWarnings("unchecked")
        Map<String, Object> pageData = response.getBody();
        assertEquals(5, ((java.util.List<?>) pageData.get("content")).size());
        assertEquals(16, pageData.get("totalElements"));  // 15 nuevos + 1 test user
        assertEquals(4, pageData.get("totalPages"));      // Con 5 elementos por pagina
    }
    // Error de servidor, retorna 500
    @Test
    void serverError_Returns500() {
        // El test asume que ya tenemos un endpoint que triggerea este error
        // Es probable que necesites crear un ednpoint test

        // Ejemplo usando un request mal formado
        HttpEntity<String> badRequest = new HttpEntity<>("{malformed:json}", headers);

        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                "/api/users",
                HttpMethod.POST,
                badRequest,
                ErrorResponse.class);


        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().status());
    }
}