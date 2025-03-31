package co.edu.uniquindio.prasegured.dto;
import co.edu.uniquindio.prasegured.model.Categoria;
import co.edu.uniquindio.prasegured.model.EnumEstado;
import java.util.Date;
import java.util.List;

public record ReporteDTO(
         String id,
         EnumEstado estado,
         String creadorAnuncio,
         String titulo,
         Date fechaPublicacion,
         String descripcion,
         String ubicacion,
         float calificacion,
         int numeroCalificaciones,
         boolean importante,
         boolean resuelto,
         List<Categoria> categoria
) {}