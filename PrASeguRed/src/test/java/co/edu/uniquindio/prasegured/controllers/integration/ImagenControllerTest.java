package co.edu.uniquindio.prasegured.controllers.integration;

import co.edu.uniquindio.prasegured.data.TestDataLoaderImagenes;
import co.edu.uniquindio.prasegured.model.Imagen;
import co.edu.uniquindio.prasegured.repository.ImagenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ImagenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ImagenRepository imagenRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private Map<String, Imagen> imagenes;

    @BeforeEach
    void setUp() {
        imagenRepository.deleteAll();
        imagenes = TestDataLoaderImagenes.loadTestData(imagenRepository, mongoTemplate);
    }

    @Test
    void testSubirImagen() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "imagen.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                new byte[]{1, 2, 3, 4}
        );

        mockMvc.perform(multipart("/api/imagenes/subir")
                        .file(file)
                        .param("reporteId", "102")
                        .param("usuarioId", "202"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reporteId").value("102"))
                .andExpect(jsonPath("$.usuarioId").value("202"))
                .andExpect(jsonPath("$.nombre").value("imagen.jpg"));
    }

    @Test
    void testGetImagenPorId() throws Exception {
        var imagen = imagenes.values().stream().findAny().get();

        mockMvc.perform(get("/api/imagenes/" + imagen.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(imagen.getId()))
                .andExpect(jsonPath("$.reporteId").value(imagen.getReporteId()))
                .andExpect(jsonPath("$.usuarioId").value(imagen.getUsuarioId()))
                .andExpect(jsonPath("$.nombre").value(imagen.getNombre()));
    }

    @Test
    void testGetImagenPorIdNotFound() throws Exception {
        mockMvc.perform(get("/api/imagenes/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetImagenesPorUsuarioId() throws Exception {
        var imagen = imagenes.values().stream().findAny().get();

        mockMvc.perform(get("/api/imagenes/usuario/" + imagen.getUsuarioId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].usuarioId").value(imagen.getUsuarioId()));
    }

    @Test
    void testGetImagenesPorReporteId() throws Exception {
        var imagen = imagenes.values().stream().findAny().get();

        mockMvc.perform(get("/api/imagenes/reporte/" + imagen.getReporteId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].reporteId").value(imagen.getReporteId()));
    }
}