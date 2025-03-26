package co.edu.uniquindio.prasegured.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
public class RecaptchaService {

    @Value("${google.recaptcha.secret}")
    private String recaptchaSecret;

    private static final String RECAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify";

    public boolean verifyRecaptcha(String token) {
        RestTemplate restTemplate = new RestTemplate();

        String url = RECAPTCHA_URL + "?secret=" + recaptchaSecret + "&response=" + token;

        Map<String, Object> response = restTemplate.postForObject(url, null, Map.class);

        if (response == null || !response.containsKey("success")) {
            return false;
        }

        return (boolean) response.get("success");
    }
}