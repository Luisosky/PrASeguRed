package co.edu.uniquindio.prasegured.controller;

import co.edu.uniquindio.prasegured.dto.AccountUpdateRequest;
import co.edu.uniquindio.prasegured.dto.DeleteAccountRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Tag(name = "Account", description = "API for account management")
public class AccountController {

    @PatchMapping("/cuenta")
    public ResponseEntity<Map<String, String>> updateAccount(@RequestBody AccountUpdateRequest request) {

        return ResponseEntity.ok(Map.of("message", "Datos actualizados correctamente."));
    }

    @DeleteMapping("/cuenta")
    public ResponseEntity<Map<String, String>> deleteAccount(@RequestBody DeleteAccountRequest request) {

        return ResponseEntity.ok(Map.of("message", "Cuenta eliminada."));
    }
}