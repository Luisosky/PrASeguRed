package co.edu.uniquindio.prasegured.controller;

import co.edu.uniquindio.prasegured.dto.ActualizacionCuentaDTO;
import co.edu.uniquindio.prasegured.model.Usuario;
import co.edu.uniquindio.prasegured.service.UsuarioService;
import co.edu.uniquindio.prasegured.auth.JwtTokenValidator;
import co.edu.uniquindio.prasegured.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/cuenta")
public class CuentaController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private JwtTokenValidator jwtTokenValidator;
    
    @Autowired
    private JwtService jwtService;
    
    @PatchMapping
    public ResponseEntity<?> actualizarCuenta(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody ActualizacionCuentaDTO datosActualizados) {
        
        try {
            // Extraer el token del encabezado (eliminar el prefijo "Bearer ")
            String token = authHeader.replace("Bearer ", "");
            
            // Obtener el correo del usuario del token
            String userEmail = jwtTokenValidator.extractUsername(token);
            
            // Verificar que el token sea válido
            if (userEmail == null || !jwtTokenValidator.isTokenValid(token)) {
                return ResponseEntity.status(401).body(Map.of("error", "Token inválido o expirado"));
            }
            
            // Obtener el usuario por su correo
            Usuario usuario = usuarioService.getUsuarioActivoByCorreo(userEmail);
            
            if (usuario == null) {
                return ResponseEntity.status(404).body(Map.of("error", "Usuario no encontrado"));
            }
            
            // Actualizar los datos del usuario
            Usuario usuarioActualizado = usuarioService.actualizarDatosUsuario(usuario.getId(), datosActualizados);
            
            // Crear mensaje de respuesta
            Map<String, String> response = new HashMap<>();
            response.put("message", "Datos actualizados correctamente.");
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error interno del servidor: " + e.getMessage()));
        }
    }
    
    // Método alternativo que usa el contexto de seguridad de Spring [Me lo tiro GPT]
    @PatchMapping("/seguro")
    public ResponseEntity<?> actualizarCuentaSeguro(
            @RequestBody ActualizacionCuentaDTO datosActualizados) {
        
        try {
            // Obtener la autenticación del contexto de seguridad
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401).body(Map.of("error", "No autenticado"));
            }
            
            // Obtener el correo del usuario autenticado
            String userEmail = ((UserDetails) authentication.getPrincipal()).getUsername();
            
            // Obtener el usuario por su correo
            Usuario usuario = usuarioService.getUsuarioActivoByCorreo(userEmail);
            
            if (usuario == null) {
                return ResponseEntity.status(404).body(Map.of("error", "Usuario no encontrado"));
            }
            
            // Actualizar los datos del usuario
            Usuario usuarioActualizado = usuarioService.actualizarDatosUsuario(usuario.getId(), datosActualizados);
            
            // Crear mensaje de respuesta
            Map<String, String> response = new HashMap<>();
            response.put("message", "Datos actualizados correctamente.");
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error interno del servidor: " + e.getMessage()));
        }
    }
}