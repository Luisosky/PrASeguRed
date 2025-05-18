package co.edu.uniquindio.prasegured.security;

import co.edu.uniquindio.prasegured.model.Usuario;
import co.edu.uniquindio.prasegured.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

@Component
public class UsuarioActivoCheckFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
            
        // Solo verificamos si el usuario ya está autenticado por el filtro JWT anterior
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            
            if (principal instanceof UserDetails) {
                String username = ((UserDetails) principal).getUsername();
                Usuario usuario = usuarioRepository.findByCorreo(username);
                
                // Si el usuario existe pero no está activo, rechazamos la solicitud
                if (usuario != null && !usuario.isActivo()) {
                    SecurityContextHolder.clearContext(); // Limpiamos el contexto de seguridad
                    
                    // Configuramos la respuesta de error
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.setContentType("application/json");
                    
                    Map<String, String> errorResponse = Map.of(
                        "error", "Acceso denegado", 
                        "message", "Su cuenta está desactivada. Contacte al administrador."
                    );
                    
                    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
                    return; // Terminamos la ejecución del filtro
                }
            }
        }
        
        filterChain.doFilter(request, response);
    }
}