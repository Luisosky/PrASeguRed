package co.edu.uniquindio.prasegured.controller;

import co.edu.uniquindio.prasegured.dto.EmailRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/suscripcion/")
public class EmailController {

    @PostMapping("/noticias")
    public ResponseEntity<String> subscribeToNews(@RequestBody EmailRequest emailRequest) {
        // Simulación de almacenamiento del email
        System.out.println("Email recibido: " + emailRequest.getEmail());
        return ResponseEntity.ok("Suscripción exitosa");
    }
}


