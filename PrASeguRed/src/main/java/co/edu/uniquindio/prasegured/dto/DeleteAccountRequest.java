package co.edu.uniquindio.prasegured.dto;

import jakarta.validation.constraints.NotNull;

public record DeleteAccountRequest(
        @NotNull(message = "Debe confirmar la eliminación")
        boolean confirmacion
) {}