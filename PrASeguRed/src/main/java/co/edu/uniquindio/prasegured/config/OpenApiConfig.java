package co.edu.uniquindio.prasegured.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class OpenApiConfig {

    @Value("classpath:static/openapi.yaml")
    private Resource openApiResource;

    @Bean
    public OpenAPI customOpenAPI() throws IOException {
        try (InputStream is = openApiResource.getInputStream()) {
            LoaderOptions options = new LoaderOptions();
            return new Yaml(new Constructor(OpenAPI.class, options)).load(is);
        }
    }
}