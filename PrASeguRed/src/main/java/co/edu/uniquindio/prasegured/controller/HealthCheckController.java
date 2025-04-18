package co.edu.uniquindio.prasegured.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController     
@RequestMapping("/health")
public class HealthCheckController {

    @GetMapping
    public ResponseEntity<String> checkHealth() {
        return ResponseEntity.ok("Conexión establecida con el backend");
    }
}
