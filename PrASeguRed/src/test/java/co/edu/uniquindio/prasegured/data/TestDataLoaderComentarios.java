package co.edu.uniquindio.prasegured.data;

import co.edu.uniquindio.prasegured.model.Comentario;
import co.edu.uniquindio.prasegured.model.ESTADOREPORTE;
import co.edu.uniquindio.prasegured.repository.ComentarioRepository;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TestDataLoaderComentarios {
    public static Map<String, Comentario> loadTestData(ComentarioRepository comentarioRepository, MongoTemplate mongoTemplate) {
        Comentario comentario1 = new Comentario();
        comentario1.setId("01");
        comentario1.setIdReporte("04");
        comentario1.setIdUsuario("29");
        comentario1.setDescripcion("Este es un comentario de prueba.");
        comentario1.setEstado(ESTADOREPORTE.Publicado);
        comentario1.setFechaPublicacion(new Date());
        comentario1.setAnonimo(false);
        comentario1.setLikes(8);
        comentario1.setDislikes(2);


        Comentario comentario2 = new Comentario();
        comentario2.setId("02");
        comentario2.setIdReporte("04");
        comentario2.setIdUsuario("29");
        comentario2.setDescripcion("no le crean a esa *****.");
        comentario2.setEstado(ESTADOREPORTE.Eliminado);
        comentario1.setFechaPublicacion(new Date());
        comentario1.setAnonimo(true);
        comentario1.setLikes(0);
        comentario1.setDislikes(22);

        return loadTestData(
                List.of(comentario1, comentario2),
                comentarioRepository,
                mongoTemplate
        );
    }
    public static Map<String, Comentario> loadTestData
            (List<Comentario> newComentarios, ComentarioRepository comentarioRepository, MongoTemplate mongoTemplate) {
        mongoTemplate.getDb().listCollectionNames().forEach(mongoTemplate::dropCollection);
        return comentarioRepository.saveAll(newComentarios).stream()
                .collect(Collectors.toMap(Comentario::getId, comentario -> comentario));
    }
}
