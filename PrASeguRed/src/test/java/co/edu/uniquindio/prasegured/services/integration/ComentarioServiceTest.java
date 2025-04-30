package co.edu.uniquindio.prasegured.services.integration;

import co.edu.uniquindio.prasegured.data.TestDataLoaderComentarios;
import co.edu.uniquindio.prasegured.dto.ComentarioDTO;
import co.edu.uniquindio.prasegured.dto.ComentarioRequest;
import co.edu.uniquindio.prasegured.exception.ValueConflictException;
import co.edu.uniquindio.prasegured.model.Comentario;
import co.edu.uniquindio.prasegured.model.ESTADOREPORTE;
import co.edu.uniquindio.prasegured.repository.ComentarioRepository;
import co.edu.uniquindio.prasegured.service.ComentarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ComentarioServiceTest {

    @Autowired
    private ComentarioService comentarioService;

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
    void testSaveComentario() {
        // Arrange: Crear una solicitud de comentario con datos de prueba
        var comentarioDTO = new ComentarioRequest("05", "2904", false, "lol", "falso", 2, 0);

        // Act: Llama al método que guarda el comentario
        ComentarioDTO savedComentario = comentarioService.guardarComentario(comentarioDTO);

        assertNotNull(savedComentario.id());
        assertEquals(comentarioDTO.idUsuario(), savedComentario.idUsuario()); // Verifica el usuario
        assertEquals(comentarioDTO.idReporte(), savedComentario.idReporte()); // Verifica el reporte
        assertEquals(comentarioDTO.descripcion(), savedComentario.descripcion()); // Verifica el estado
    }

    @Test
    void testDeleteComentario() {
        var comentario = comentarios.values().stream().findAny().orElseThrow();
        comentarioService.eliminarComentario(comentario.getId());
        var deletedComentario = comentarioRepository.findById(comentario.getId()).orElseThrow();
        assertEquals(ESTADOREPORTE.Eliminado, deletedComentario.getEstado());
    }

    @Test
    void testFindAllByReporteId() {
        var comentario = comentarios.values().stream().findAny().orElseThrow();
        var result = comentarioService.obtenerComentariosPorReporte(comentario.getIdReporte());
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(comentario.getIdReporte(), result.get(0).idReporte());
    }

    @Test
    void testFindAllByUsuarioId() {
        var comentario = comentarios.values().stream().findAny().orElseThrow();
        var result = comentarioService.obtenerComentariosPorUsuario(comentario.getIdUsuario());
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(comentario.getIdUsuario(), result.get(0).idUsuario());
    }

    @Test
    void testDeleteComentarioThrowsException() {
        String nonExistentId = "nonexistentId";
        ValueConflictException exception = assertThrows(ValueConflictException.class, () -> {
            comentarioService.eliminarComentario(nonExistentId);
        });
        assertEquals("No existe un comentario con id: " + nonExistentId, exception.getMessage());
    }
    @Test
    void testEstadoDenegado() {
        var comentario = comentarios.values().stream().findAny().orElseThrow();
        comentarioService.denegarComentario(comentario.getId());
        var updatedComentario = comentarioRepository.findById(comentario.getId()).orElseThrow();
        assertEquals(ESTADOREPORTE.Denegado, updatedComentario.getEstado());
    }
}