package co.edu.uniquindio.prasegured.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Configuration
@Profile("!test")
@Service
public class RecaptchaService {

//    private final String recaptchaSecret;
//    private static final String RECAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify";
//
//    public RecaptchaService(String recaptchaSecret) {
//        this.recaptchaSecret = recaptchaSecret;
//    }
//
//    public boolean verifyRecaptcha(String token) {
//        RestTemplate restTemplate = new RestTemplate();
//        String url = RECAPTCHA_URL + "?secret=" + recaptchaSecret + "&response=" + token;
//
//        Map<String, Object> response = restTemplate.postForObject(url, null, Map.class);
//
//        return response != null && Boolean.TRUE.equals(response.get("success"));
//    }
}

