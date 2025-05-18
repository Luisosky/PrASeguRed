package co.edu.uniquindio.prasegured.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniquindio.prasegured.dto.PasswordResetRequest;
import co.edu.uniquindio.prasegured.model.Usuario;
import co.edu.uniquindio.prasegured.repository.UsuarioRepository;
import co.edu.uniquindio.prasegured.service.AuthService;
import co.edu.uniquindio.prasegured.service.TokenVerificationService;
import jakarta.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/cuenta")
public class PasswordController {
    
    private static final Logger logger = LoggerFactory.getLogger(PasswordController.class);
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @Autowired
    private TokenVerificationService tokenVerificationService;
    
    @PostMapping("/password")
    public ResponseEntity<?> solicitarRecuperacionPassword(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody Map<String, String> request) {
        
        logger.info("Recibida solicitud de recuperación de contraseña para correo: {}", request.get("correo"));
        
        // Validar el token si está presente (opcional para recuperación de contraseña)
        if (authHeader != null && !authHeader.isEmpty()) {
            logger.info("Token presente en la solicitud: {}", authHeader);
            ResponseEntity<?> tokenValidationResponse = tokenVerificationService.validateTokenAndGetUser(authHeader);
            if (tokenValidationResponse != null) {
                logger.warn("Validación de token falló");
                return tokenValidationResponse;
            }
        }
        
        Map<String, String> response = new HashMap<>();
        String correo = request.get("correo");
        
        if (correo == null || correo.trim().isEmpty()) {
            logger.warn("Solicitud sin correo electrónico");
            response.put("error", "El correo electrónico es requerido.");
            return ResponseEntity.badRequest().body(response);
        }
        
        // Verificar si el correo existe en la base de datos
        logger.info("Buscando usuario con correo: {}", correo);
        Usuario usuario = usuarioRepository.findByCorreo(correo);
        if (usuario == null) {
            logger.warn("No se encontró usuario con correo: {}", correo);
            response.put("error", "No existe una cuenta asociada a este correo electrónico.");
            return ResponseEntity.status(404).body(response);
        }
        
        logger.info("Usuario encontrado, estado activo: {}", usuario.isActivo());
        // Verificar si la cuenta está activa
        if (!usuario.isActivo()) {
            logger.warn("La cuenta está desactivada para el correo: {}", correo);
            response.put("error", "La cuenta asociada a este correo electrónico está desactivada. Por favor contacte al administrador.");
            return ResponseEntity.status(403).body(response);
        }
        
        try {
            // Enviar código de verificación
            logger.info("Enviando código de verificación a: {}", correo);
            authService.sendVerificationCode(correo);
            
            response.put("message", "Se ha enviado un código de verificación a su correo electrónico.");
            logger.info("Código de verificación enviado correctamente a: {}", correo);
            return ResponseEntity.ok(response);
        } catch (MessagingException e) {
            logger.error("Error al enviar código de verificación: {}", e.getMessage(), e);
            response.put("error", "Error al enviar el código de verificación.");
            return ResponseEntity.status(500).body(response);
        }
    }
    
    @PutMapping("/nueva-password")
    public ResponseEntity<?> establecerNuevaPassword(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody PasswordResetRequest request) {
        
        logger.info("Recibida solicitud para establecer nueva contraseña para: {}", request.getCorreo());
        
        // Validar el token si está presente (opcional para reseteo de contraseña)
        if (authHeader != null && !authHeader.isEmpty()) {
            ResponseEntity<?> tokenValidationResponse = tokenVerificationService.validateTokenAndGetUser(authHeader);
            if (tokenValidationResponse != null) {
                return tokenValidationResponse;
            }
        }
        
        Map<String, String> response = new HashMap<>();
        
        // Verificar si el correo existe en la base de datos
        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo());
        if (usuario == null) {
            response.put("error", "No existe una cuenta asociada a este correo electrónico.");
            return ResponseEntity.status(404).body(response);
        }
        
        // Verificar si la cuenta está activa
        if (!usuario.isActivo()) {
            response.put("error", "La cuenta asociada a este correo electrónico está desactivada. Por favor contacte al administrador.");
            return ResponseEntity.status(403).body(response);
        }
        
        // Verificar el código
        boolean codigoValido = authService.verifyCode(request.getCorreo(), request.getCodigo());
        if (!codigoValido) {
            response.put("error", "El código de verificación es inválido o ha expirado.");
            return ResponseEntity.status(400).body(response);
        }
        
        // Actualizar la contraseña
        try {
            // Encriptar la nueva contraseña usando el mismo método que en el registro/login
            String nuevaContraseñaEncriptada = bCryptPasswordEncoder.encode(request.getCorreo() + request.getNuevaContraseña());
            usuario.setContraseña(nuevaContraseñaEncriptada);
            usuarioRepository.save(usuario);
            
            response.put("message", "Contraseña actualizada correctamente.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error al actualizar contraseña: {}", e.getMessage(), e);
            response.put("error", "Error al actualizar la contraseña: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}