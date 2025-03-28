package co.edu.uniquindio.prasegured.controller;

import co.edu.uniquindio.prasegured.dto.CredencialesDTO;
import co.edu.uniquindio.prasegured.dto.VerificationRequest;
import co.edu.uniquindio.prasegured.model.Usuario;
import co.edu.uniquindio.prasegured.repository.UsuarioRepository;
import co.edu.uniquindio.prasegured.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*") // Permite peticiones desde el front
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AuthService authService;

    @PostMapping("/auth/login")
    public ResponseEntity<Void> login(@RequestBody CredencialesDTO credenciales) {
        Usuario usuario = usuarioRepository.findByCorreo(credenciales.correo());
        if (usuario != null) {
            String rawPassword = credenciales.correo() + credenciales.contraseña();
            if (bCryptPasswordEncoder.matches(rawPassword, usuario.getContraseña())) {
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.status(401).build();
    }

    @PostMapping("/enviar-codigo")
    public ResponseEntity<String> enviarCodigo(@RequestBody VerificationRequest request) {
        try {
            authService.sendVerificationCode(request.getEmail());
            return ResponseEntity.ok("Código enviado correctamente");
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body("Error al enviar el código");
        }
    }

    @PostMapping("/verificar-codigo")
    public ResponseEntity<String> verificarCodigo(@RequestBody VerificationRequest request) {
        boolean valido = authService.verifyCode(request.getEmail(), request.getCode());
        return valido ? ResponseEntity.ok("Código verificado correctamente") :
                ResponseEntity.status(400).body("Código incorrecto o expirado");
    }
}