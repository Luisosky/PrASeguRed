package co.edu.uniquindio.prasegured.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record AccountUpdateRequest(
        @Email(message = "Debe ser un email válido")
        String correo,

        @Size(max = 15, message = "El teléfono no debe exceder los 15 caracteres")
        String telefono
) {}