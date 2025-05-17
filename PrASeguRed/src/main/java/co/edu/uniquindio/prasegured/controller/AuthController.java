package co.edu.uniquindio.prasegured.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import co.edu.uniquindio.prasegured.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService; // Importa UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniquindio.prasegured.model.ESTADOSUSUARIO;
import co.edu.uniquindio.prasegured.model.Usuario;
import co.edu.uniquindio.prasegured.repository.UsuarioRepository;
import co.edu.uniquindio.prasegured.security.JwtService;
import co.edu.uniquindio.prasegured.service.AuthService;
import co.edu.uniquindio.prasegured.service.ReporteService;
import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*") // Permite peticiones desde el front
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService; // Inyecta tu JwtService

    @Autowired
    private ReporteService reporteService;

    @Autowired
    private UserDetailsService userDetailsService; // Inyecta UserDetailsService

    @PostMapping("/login")  
    public ResponseEntity<?> login(@RequestBody CredencialesDTO credenciales) {
        try {
            System.out.println("Se recibio un request de login de este correo: " + credenciales.correo());
            
            Usuario usuario = usuarioRepository.findByCorreo(credenciales.correo());
            if (usuario == null) {
                System.out.println("usuario no encontrado con el email: " + credenciales.correo());
                return ResponseEntity.status(401).body(Map.of("error", "Credenciales inválidas"));
            }
            
            String rawPassword = credenciales.correo() + credenciales.contraseña();
            System.out.println("Vreficando contraseña para el usuario");
            
            if (bCryptPasswordEncoder.matches(rawPassword, usuario.getContraseña())) {
                System.out.println("La contraseña es correcta");
                UserDetails userDetails = userDetailsService.loadUserByUsername(credenciales.correo());
                String jwtToken = jwtService.generateToken(userDetails);
                return ResponseEntity.ok(new AuthenticationResponse(jwtToken));
            } else {
                System.out.println("Contraseña invalida del usuario: " + credenciales.correo());
                return ResponseEntity.status(401).body(Map.of("error", "Credenciales inválidas"));
            }
        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(400).body(Map.of("error", "Error en el proceso de login", "message", e.getMessage()));
        }
    }

    @PostMapping("/codigo-usuario")
    public ResponseEntity<Map<String, String>> enviarCodigo(@RequestBody VerificationRequest request) {
        Map<String, String> response = new HashMap<>();
        try {
            authService.sendVerificationCode(request.getEmail());
            response.put("message", "Código enviado correctamente");
            return ResponseEntity.ok(response);
        } catch (MessagingException e) {
            response.put("error", "Error al enviar el código");
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/token")
    public ResponseEntity<Map<String, String>> verificarCodigo(@RequestBody VerificationRequest request) {
        Map<String, String> response = new HashMap<>();
        boolean valido = authService.verifyCode(request.getEmail(), request.getCode());
        if (valido) {
            var usuario= usuarioRepository.findByCorreo(request.getEmail());
            if (usuario != null) {
                usuario.setEstado(ESTADOSUSUARIO.ACTIVO.toString());
                usuarioRepository.save(usuario);
                response.put("message", "Código verificado correctamente. Usuario activado.");
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "Usuario no encontrado");
                return ResponseEntity.status(404).body(response);
            }
        } else {
            response.put("error", "Código incorrecto o expirado");
            return ResponseEntity.status(400).body(response);
        }
    }
    @PostMapping("/usuario-datos")
    public ResponseEntity<?> obtenerDatosUsuario(@RequestHeader("Authorization") String token) {
        try {
            // Extraer el token sin el prefijo "Bearer "
            String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;

            // Validar el token y extraer el correo electrónico del usuario
            String correo = jwtService.extractUsername(jwtToken);

            // Buscar el usuario por correo electrónico
            Usuario usuario = usuarioRepository.findByCorreo(correo);

            if (usuario != null) {
                // Convertir las locations a DTOs
                List<LocationDTO> locationDTOs = null;
                if (usuario.getLocations() != null) {
                    locationDTOs = usuario.getLocations().stream()
                            .map(loc -> new LocationDTO(
                                    loc.getLat(),
                                    loc.getLng()))
                            .collect(Collectors.toList());
                }

                // Crear el DTO con todos los campos incluidos rol y ubicaciones
                UsuarioDTO usuarioDTO = new UsuarioDTO(
                        usuario.getId(),
                        usuario.getNombreCom(),
                        usuario.getTelefono(),
                        usuario.getCiudadResidencia(),
                        usuario.getDireccion(),
                        usuario.getDocumento(),
                        usuario.getFechaNacimiento(),
                        usuario.getCorreo(),
                        usuario.getRol(),            // Campo de rol
                        locationDTOs                 // Campo de ubicaciones
                );

                // Retornar el DTO
                return ResponseEntity.ok(usuarioDTO);
            } else {
                return ResponseEntity.status(404).body(Map.of("error", "Usuario no encontrado"));
            }
        } catch (Exception e) {
            // Log del error para debugging
            e.printStackTrace();
            return ResponseEntity.status(401).body(Map.of("error", "Token inválido o expirado"));
        }
    }
}