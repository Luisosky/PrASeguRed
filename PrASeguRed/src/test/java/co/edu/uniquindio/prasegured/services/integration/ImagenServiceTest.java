package co.edu.uniquindio.prasegured.services.integration;

import co.edu.uniquindio.prasegured.data.TestDataLoaderImagenes;
import co.edu.uniquindio.prasegured.dto.ImagenDTO;
import co.edu.uniquindio.prasegured.exception.ValueConflictException;
import co.edu.uniquindio.prasegured.model.ESTADOREPORTE;
import co.edu.uniquindio.prasegured.model.Imagen;
import co.edu.uniquindio.prasegured.repository.ImagenRepository;
import co.edu.uniquindio.prasegured.service.ImagenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@Profile("test")
public class ImagenServiceTest {

    @Autowired
    private ImagenService imagenService;

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
    void testSaveImagen() throws IOException {
        // Arrange
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("imagen.jpg");
        when(file.getBytes()).thenReturn(new byte[]{1, 2, 3});

        // Act
        ImagenDTO savedImagen = imagenService.saveImagen(file, "reporteId", "usuarioId");

        // Assert
        assertNotNull(savedImagen.id());
        assertEquals("imagen.jpg", savedImagen.nombre());
        assertEquals("reporteId", savedImagen.reporteId());
        assertEquals("usuarioId", savedImagen.usuarioId());
        assertEquals(ESTADOREPORTE.Espera, savedImagen.estado());
    }

    @Test
    void testDeleteImagen() throws IOException {
        var imagen = imagenes.values().stream().findAny().orElseThrow();
        imagenService.deleteImagen(imagen.getId());
        var deletedImagen = imagenRepository.findById(imagen.getId()).orElseThrow();
        assertEquals(ESTADOREPORTE.Eliminado, deletedImagen.getEstado());
    }

    @Test
    void testUpdateImagen() throws IOException {
        // Arrange
        var imagen = imagenes.values().stream().findAny().orElseThrow();
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("imagen_actualizada.jpg");
        when(file.getBytes()).thenReturn(new byte[]{4, 5, 6});

        // Act
        ImagenDTO updatedImagen = imagenService.updateImagen(imagen.getId(), file);

        // Assert
        assertNotNull(updatedImagen);
        assertEquals("imagen_actualizada.jpg", updatedImagen.nombre());
    }

    @Test
    void testFindAllByReporteId() {
        // Arrange
        var imagen = imagenes.values().stream().findAny().orElseThrow();

        // Act
        var result = imagenService.findAllByReporteId(imagen.getReporteId());

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(imagen.getReporteId(), result.get(0).reporteId());
    }

    @Test
    void testFindAllByUsuarioId() {
        // Arrange
        var imagen = imagenes.values().stream().findAny().orElseThrow();

        // Act
        var result = imagenService.findAllByUsuarioId(imagen.getUsuarioId());

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(imagen.getUsuarioId(), result.get(0).usuarioId());
    }

    @Test
    void testDeleteImagenThrowsException() {

        String nonExistentId = "nonexistentId";
        ValueConflictException exception = assertThrows(ValueConflictException.class, () -> {
            imagenService.deleteImagen(nonExistentId);
        });
        assertEquals("No existe una imagen con el id: " + nonExistentId, exception.getMessage());
    }
}