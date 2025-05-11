package co.edu.uniquindio.prasegured.controller;

import co.edu.uniquindio.prasegured.dto.AuthenticationResponse;
import co.edu.uniquindio.prasegured.dto.CredencialesDTO;
import co.edu.uniquindio.prasegured.dto.UsuarioDTO;
import co.edu.uniquindio.prasegured.dto.VerificationRequest;
import co.edu.uniquindio.prasegured.model.ESTADOSUSUARIO;
import co.edu.uniquindio.prasegured.model.Usuario;
import co.edu.uniquindio.prasegured.repository.UsuarioRepository;
import co.edu.uniquindio.prasegured.security.JwtService;
import co.edu.uniquindio.prasegured.service.AuthService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService; // Importa UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
    private UserDetailsService userDetailsService; // Inyecta UserDetailsService

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody CredencialesDTO credenciales) {
        Usuario usuario = usuarioRepository.findByCorreo(credenciales.correo());
        if (usuario != null) {
            String rawPassword = credenciales.correo() + credenciales.contraseña();
            if (bCryptPasswordEncoder.matches(rawPassword, usuario.getContraseña())) {
                // Obtén el UserDetails desde tu UserDetailsService
                UserDetails userDetails = userDetailsService.loadUserByUsername(credenciales.correo());
                // Genera el token JWT utilizando tu JwtService, pasando el UserDetails
                String jwtToken = jwtService.generateToken(userDetails);
                return ResponseEntity.ok(new AuthenticationResponse(jwtToken));
            }
        }
        return ResponseEntity.status(401).body(Map.of("error", "Credenciales inválidas"));
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
                // Crear el DTO con los datos necesarios
                UsuarioDTO usuarioDTO = new UsuarioDTO(
                        usuario.getNombreCom(),
                        usuario.getTelefono(),
                        usuario.getCiudadResidencia(),
                        usuario.getDireccion(),
                        usuario.getDocumento(),
                        usuario.getFechaNacimiento()
                );

                // Retornar el DTO
                return ResponseEntity.ok(usuarioDTO);
            } else {
                return ResponseEntity.status(404).body(Map.of("error", "Usuario no encontrado"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Token inválido o expirado"));
        }
    }
}