package co.edu.uniquindio.prasegured.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record VerificationRequest(
        @NotBlank(message = "El código de verificación es requerido")
        @Size(min = 6, max = 6, message = "El código debe tener exactamente 6 dígitos")
        @Pattern(regexp = "^[0-9]{6}$", message = "El código debe contener solo dígitos")
        String codigoVerificacion
) {}