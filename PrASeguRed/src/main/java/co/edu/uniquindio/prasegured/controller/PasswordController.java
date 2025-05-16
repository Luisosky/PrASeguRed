package co.edu.uniquindio.prasegured.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniquindio.prasegured.dto.PasswordResetRequest;
import co.edu.uniquindio.prasegured.model.Usuario;
import co.edu.uniquindio.prasegured.repository.UsuarioRepository;
import co.edu.uniquindio.prasegured.service.AuthService;
import jakarta.mail.MessagingException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/cuenta")
@CrossOrigin("*") // Permite peticiones desde el front
public class PasswordController {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @PostMapping("/password")
    public ResponseEntity<Map<String, String>> solicitarRecuperacionPassword(@RequestBody Map<String, String> request) {
        Map<String, String> response = new HashMap<>();
        String correo = request.get("correo");
        
        // Verificar si el correo existe en la base de datos
        Usuario usuario = usuarioRepository.findByCorreo(correo);
        if (usuario == null) {
            response.put("error", "No existe una cuenta asociada a este correo electrónico.");
            return ResponseEntity.status(404).body(response);
        }
        
        try {
            // Enviar código de verificación
            authService.sendVerificationCode(correo);
            
            response.put("message", "Se ha enviado un código de verificación a su correo electrónico.");
            return ResponseEntity.ok(response);
        } catch (MessagingException e) {
            response.put("error", "Error al enviar el código de verificación.");
            return ResponseEntity.status(500).body(response);
        }
    }
    
    @PutMapping("/password")
    public ResponseEntity<Map<String, String>> establecerNuevaPassword(@RequestBody PasswordResetRequest request) {
        Map<String, String> response = new HashMap<>();
        
        // Verificar si el correo existe en la base de datos
        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo());
        if (usuario == null) {
            response.put("error", "No existe una cuenta asociada a este correo electrónico.");
            return ResponseEntity.status(404).body(response);
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
            response.put("error", "Error al actualizar la contraseña: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}