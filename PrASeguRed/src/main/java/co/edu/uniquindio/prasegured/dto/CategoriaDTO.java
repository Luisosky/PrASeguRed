package co.edu.uniquindio.prasegured.dto;

import co.edu.uniquindio.prasegured.model.EnumEstado;

public record CategoriaDTO (
        String id,
        String name,
        String descripcion,
        EnumEstado tipoCategoria){
}
