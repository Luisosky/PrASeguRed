package co.edu.uniquindio.prasegured.dto;

import co.edu.uniquindio.prasegured.model.ESTADOREPORTE;

public record CategoriaDTO (
        String id,
        String name,
        String descripcion,
        ESTADOREPORTE tipoCategoria){
}
