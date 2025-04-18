package co.edu.uniquindio.prasegured.repositories;

import co.edu.uniquindio.prasegured.data.TestDataLoader;
import co.edu.uniquindio.prasegured.model.Reporte;
import co.edu.uniquindio.prasegured.repository.ReporteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List; // Importar la clase List
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
public class ReporteRepositoryTest {
    @Autowired
    private ReporteRepository reporteRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    private Map<String, Reporte> reportes;

    @BeforeEach
    void setUp() {
        reportes = TestDataLoader.loadTestData(reporteRepository, mongoTemplate);
    }

    @Test
    void testFindById() {
        var testReporte = reportes.values().stream().findAny().orElseThrow();
        Optional<Reporte> reporte = reporteRepository.findById(testReporte.getId());
        assertTrue(reporte.isPresent());
        assertEquals(testReporte.getDescripcion(), reporte.get().getDescripcion());
    }

    @Test
    void testFindByIdUsuario() {
        var testUsuarioId = reportes.values().stream().findAny().orElseThrow().getIdUsuario();
        List<Reporte> reportesUsuario = reporteRepository.findByIdUsuario(testUsuarioId); // Cambiar var por List<Reporte>

        assertFalse(reportesUsuario.isEmpty());
        reportes.values().stream()
                .filter(r -> r.getIdUsuario().equals(testUsuarioId))
                .forEach(r -> assertTrue(reportesUsuario.contains(r)));
    }
}