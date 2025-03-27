package co.edu.uniquindio.prasegured.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailRequest(
        @NotBlank(message = "El nombre es requerido")
        String name,

        @NotBlank(message = "El correo es requerido")
        @Email(message = "Debe ser un email válido")
        @JsonProperty("correo") // Ahora sí se mapea correctamente
        String email,

        @NotBlank(message = "El asunto es requerido")
        String subject,

        @NotBlank(message = "El mensaje es requerido")
        String message
) {}
