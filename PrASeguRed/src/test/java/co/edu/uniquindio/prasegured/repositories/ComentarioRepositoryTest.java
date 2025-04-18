package co.edu.uniquindio.prasegured.repositories;

import co.edu.uniquindio.prasegured.data.TestDataLoaderComentarios;
import co.edu.uniquindio.prasegured.model.Comentario;
import co.edu.uniquindio.prasegured.repository.ComentarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class ComentarioRepositoryTest {
    @Autowired
    private ComentarioRepository comentarioRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    private Map<String, Comentario> comentarios;

    @BeforeEach
    void setUp() {
        comentarios = TestDataLoaderComentarios.loadTestData(comentarioRepository, mongoTemplate);
    }

    @Test
    void testFindAllByIdReporte() {
        var testComentario = comentarios.values().stream().findAny().orElseThrow();
        var comentarios = comentarioRepository.findAllByIdReporte(testComentario.getIdReporte());
        assertTrue(comentarios.stream().anyMatch(c -> c.getIdReporte().equals(testComentario.getIdReporte())));
    }
    @Test
    void testFindAllByIdUsuario() {
        var testComentario = comentarios.values().stream().findAny().orElseThrow();
        var comentarios = comentarioRepository.findAllByIdUsuario(testComentario.getIdUsuario());
        assertTrue(comentarios.stream().anyMatch(c -> c.getIdUsuario().equals(testComentario.getIdUsuario())));
    }
}
