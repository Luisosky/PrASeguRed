package co.edu.uniquindio.prasegured.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Especifica los orígenes permitidos en lugar de usar comodín
        config.addAllowedOrigin("http://localhost:4200"); // Tu frontend Angular
        
        // Configura los encabezados permitidos
        config.addAllowedHeader("Origin");
        config.addAllowedHeader("Content-Type");
        config.addAllowedHeader("Accept");
        config.addAllowedHeader("Authorization");
        
        // Configura los métodos permitidos explícitamente
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        
        // Permite credenciales (importante para autenticación)
        config.setAllowCredentials(true);
        
        // Expone encabezados que el cliente puede leer
        config.setExposedHeaders(Arrays.asList("Authorization"));
        
        // Tiempo de caché para respuestas preflight
        config.setMaxAge(3600L);

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}