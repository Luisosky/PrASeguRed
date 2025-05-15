package co.edu.uniquindio.prasegured.controller;

import co.edu.uniquindio.prasegured.model.ROL;
import co.edu.uniquindio.prasegured.model.Usuario;
import co.edu.uniquindio.prasegured.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/registro")
public class UsuarioController {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/usuario")
    public ResponseEntity<String> registrarUsuario(@RequestBody Usuario usuario) {
        try {
            // Loguear la recepción del usuario (sin la contraseña por seguridad)
            logger.info("Registrando nuevo usuario: {}", usuario.getCorreo());

            // El servicio ya asigna el rol automáticamente
            Usuario usuarioRegistrado = usuarioService.registrarUsuario(usuario);

            // Verificar que el rol se haya asignado correctamente
            if (usuarioRegistrado.getRol() == null || !usuarioRegistrado.getRol().equals(ROL.USUARIO.toString())) {
                logger.error("Error en la asignación de rol para: {}", usuario.getCorreo());
                return ResponseEntity.status(500).body("Error en la asignación de rol");
            }

            return ResponseEntity.status(201).body("Registro exitoso");
        } catch (IllegalArgumentException e) {
            logger.error("Error en los datos del usuario: {}", e.getMessage());
            return ResponseEntity.status(400).body("Error en los datos: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Error al registrar usuario: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error interno: " + e.getMessage());
        }
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        List<Usuario> usuarios = usuarioService.getAllUsuarios();
        return ResponseEntity.ok(usuarios);
    }
}