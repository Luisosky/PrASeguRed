package co.edu.uniquindio.prasegured.dto;

import co.edu.uniquindio.prasegured.model.EnumEstado;

public record CategoriaRequest(
        String name,
        String descripcion,
        EnumEstado tipoCategoria
) {
}
