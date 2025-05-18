package co.edu.uniquindio.prasegured.config;

import co.edu.uniquindio.prasegured.security.UsuarioActivoCheckFilter;
import co.edu.uniquindio.prasegured.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

@Configuration
@EnableWebSecurity
@Profile("!test")
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;
    
    @Autowired
    private UsuarioActivoCheckFilter usuarioActivoCheckFilter;

    @Bean(name = "defaultSecurityFilterChain")
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> {}) // Habilitar configuración CORS desde WebConfig
            .authorizeHttpRequests(auth -> auth
                // Rutas públicas (no requieren autenticación)
                .requestMatchers("/auth/login", "/auth/codigo-usuario", "/auth/token",
                                "/cuenta/password", "/cuenta/nueva-password", "/swagger-ui/**", 
                                "/v3/api-docs/**", "/openapi.yaml").permitAll()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll() // Permitir solicitudes OPTIONS
                // Todas las demás rutas requieren autenticación
                .anyRequest().authenticated())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // Primero el filtro JWT y luego nuestro filtro personalizado
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(usuarioActivoCheckFilter, JwtAuthenticationFilter.class)
            .build();
    }
}