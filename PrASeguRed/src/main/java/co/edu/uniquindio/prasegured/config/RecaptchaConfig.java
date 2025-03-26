package co.edu.uniquindio.prasegured.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RecaptchaConfig {

    @Bean
    public String recaptchaSecret() {
        // Carga las variables desde el archivo .env
        Dotenv dotenv = Dotenv.configure()
                .directory("./")
                .ignoreIfMalformed()
                .ignoreIfMissing()
                .load();

        // Obtiene la clave secreta de reCAPTCHA
        String secret = dotenv.get("GOOGLE_RECAPTCHA_SECRET");
        if (secret == null || secret.isEmpty()) {
            throw new IllegalArgumentException("GOOGLE_RECAPTCHA_SECRET no está definida en el archivo .env");
        }
        return secret;
    }
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().load();
        String secret = dotenv.get("GOOGLE_RECAPTCHA_SECRET");

        if (secret == null) {
            System.out.println("⚠️ ERROR: No se pudo cargar GOOGLE_RECAPTCHA_SECRET");
        } else {
            System.out.println("✅ Recaptcha Secret: " + secret);
        }
    }
}
