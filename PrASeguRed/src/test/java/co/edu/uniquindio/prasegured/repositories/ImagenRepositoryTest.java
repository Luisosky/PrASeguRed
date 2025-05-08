package co.edu.uniquindio.prasegured.repositories;

import co.edu.uniquindio.prasegured.data.TestDataLoaderImagenes;
import co.edu.uniquindio.prasegured.model.Imagen;
import co.edu.uniquindio.prasegured.repository.ImagenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
@Profile("test")
public class ImagenRepositoryTest {
    @Autowired
    private ImagenRepository imagenRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    private Map<String, Imagen> imagenes;

    @BeforeEach
    void setUp() {
        imagenes = TestDataLoaderImagenes.loadTestData(imagenRepository, mongoTemplate);
    }

    @Test
    void testFindByReporteId() {
        var testImagen = imagenes.values().stream().findAny().orElseThrow();
        var imagenesPorReporte = imagenRepository.findByReporteId(testImagen.getReporteId());
        assertTrue(imagenesPorReporte.stream().allMatch(i -> i.getReporteId().equals(testImagen.getReporteId())));
    }

    @Test
    void testFindByUsuarioId() {
        var testImagen = imagenes.values().stream().findAny().orElseThrow();
        var imagenesPorUsuario = imagenRepository.findByUsuarioId(testImagen.getUsuarioId());
        assertTrue(imagenesPorUsuario.stream().allMatch(i -> i.getUsuarioId().equals(testImagen.getUsuarioId())));
    }
}
