package co.edu.uniquindio.prasegured.repositories;

import co.edu.uniquindio.prasegured.model.Comentario;
import co.edu.uniquindio.prasegured.repository.ComentarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Map;

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

}
