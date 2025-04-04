package co.edu.uniquindio.prasegured.dto;

import co.edu.uniquindio.prasegured.model.Categoria;

import java.util.Date;
import java.util.List;

public record ReporteRequest(
        String id,
         String titulo,
         Date fechaPublicacion,
         String descripcion,
         String ubicacion,
         List<Categoria>categoria
) {
}
