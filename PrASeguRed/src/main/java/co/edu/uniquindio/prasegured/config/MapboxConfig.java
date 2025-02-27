package co.edu.uniquindio.prasegured.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapboxConfig {
    @Bean
    public String mapboxToken() {
        //Carga las variables de .env
        Dotenv dotenv = Dotenv.configure()
                .directory("./")
                .ignoreIfMalformed()
                .ignoreIfMissing()
                .load();
        //Busca por acces token
        String token = dotenv.get("ACCES_TOKEN");
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("ACCES_TOKEN no esta definida en el archivo .env");
        }
        return token;
    }
}
