package co.edu.uniquindio.prasegured.repositories;

import co.edu.uniquindio.prasegured.data.TestDataLoaderCategorias;
import co.edu.uniquindio.prasegured.model.Categoria;
import co.edu.uniquindio.prasegured.repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
public class CategoriaRepositoryTest {
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    private Map<String, Categoria> categorias;

    @BeforeEach
    void setUp() {
        categorias = TestDataLoaderCategorias.loadTestData(categoriaRepository, mongoTemplate);
    }

    @Test
    void testFindByName() {
        var testCategoria = categorias.values().stream().findAny().orElseThrow();
        var categoria = categoriaRepository.findByName(testCategoria.getName());
        assertTrue(categoria.isPresent());
        assertEquals(testCategoria.getName(), categoria.get().getName());
    }

    @Test
    void testFindByStatusNot() {
        var testCategoria = categorias.values().stream().findAny().orElseThrow();
        var categorias = categoriaRepository.findByStatusNot(testCategoria.getStatus());
        assertTrue(categorias.stream().noneMatch(c -> c.getStatus().equals(testCategoria.getStatus())));
    }

}
