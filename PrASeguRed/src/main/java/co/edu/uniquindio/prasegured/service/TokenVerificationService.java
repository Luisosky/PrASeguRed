package co.edu.uniquindio.prasegured.service;

import co.edu.uniquindio.prasegured.model.Usuario;
import co.edu.uniquindio.prasegured.repository.UsuarioRepository;
import co.edu.uniquindio.prasegured.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TokenVerificationService {

    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    /**
     * Verifica el token y obtiene el usuario activo.
     * @param authHeader El header de autorización
     * @return El usuario si está activo y el token es válido, o null si hay algún error
     */
    public Usuario getActiveUserFromToken(String authHeader) {
        try {
            String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
            String correo = jwtService.extractUsername(token);
            
            if (correo == null) {
                return null;
            }
            
            Usuario usuario = usuarioRepository.findByCorreo(correo);
            
            if (usuario != null && usuario.isActivo()) {
                return usuario;
            }
            
            return null;
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Método útil para controladores que devuelve la respuesta HTTP apropiada si hay un error de autenticación
     */
    public ResponseEntity<?> validateTokenAndGetUser(String authHeader) {
        if (authHeader == null || authHeader.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Token de autorización requerido"));
        }
        
        Usuario usuario = getActiveUserFromToken(authHeader);
        
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Token inválido o cuenta desactivada"));
        }
        
        return null; // No hay error, el controlador puede continuar
    }
}