package co.edu.uniquindio.prasegured.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestión de Usuarios y Reportes")
                        .description("API para el registro, autenticación, gestión de cuentas y reportes")
                        .version("1.0.0"))
                .servers(List.of(new Server().url("https://api.ejemplo.com/v1")));
    }
}