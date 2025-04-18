package co.edu.uniquindio.prasegured.data;

import co.edu.uniquindio.prasegured.model.Comentario;
import co.edu.uniquindio.prasegured.model.ESTADOS;
import co.edu.uniquindio.prasegured.model.EnumEstado;
import co.edu.uniquindio.prasegured.repository.ComentarioRepository;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class TestDataLoaderComentarios {
    public static Map<String, Comentario> loadTestData(ComentarioRepository comentarioRepository, MongoTemplate mongoTemplate) {
        Comentario comentario1 = new Comentario();
        comentario1.setId("01");
        comentario1.setDescripcion("Este es un comentario de prueba.");
        comentario1.setEstado(EnumEstado.Publicado);
        comentario1.setFechaCreacion(new Date());

        Comentario comentario2 = new Comentario();
        comentario2.setId("02");
        comentario2.setDescripcion("no le crean a esa *****.");
        comentario2.setEstado(EnumEstado.Eliminado);

        return TestDataLoader.loadTestData(
                List.of(comentario1, comentario2),
                comentarioRepository,
                mongoTemplate
        );
    }
}
