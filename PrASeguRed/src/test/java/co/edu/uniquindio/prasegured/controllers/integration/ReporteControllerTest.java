package co.edu.uniquindio.prasegured.controllers.integration;

import co.edu.uniquindio.prasegured.data.TestDataLoaderReportes;
import co.edu.uniquindio.prasegured.model.ESTADOREPORTE;
import co.edu.uniquindio.prasegured.model.Reporte;
import co.edu.uniquindio.prasegured.repository.ReporteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post; // Importación correcta
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath; // Importación correcta
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Profile("test")
public class ReporteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReporteRepository reporteRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private Map<String, Reporte> reportes;

    @BeforeEach
    void setUp() {
        reportes = TestDataLoaderReportes.loadTestData(reporteRepository, mongoTemplate);
    }

    @Test
    void testCrearReporte() throws Exception {
        // Sección de Arrange: Configuración inicial del reporte a enviar
        var reporte = new Reporte();
        reporte.setId("11");
        reporte.setIdUsuario("03");
        reporte.setEstado(ESTADOREPORTE.Espera);
        reporte.setCreadorAnuncio("maria");
        reporte.setTitulo("Se regalan gatos");
        reporte.setFechaPublicacion(new Date());
        reporte.setFechaActualizacion(null);
        reporte.setDescripcion("todo el que quiera adoptar puede");
        reporte.setUbicacion("Casa azul");
        reporte.setLikes(6);
        reporte.setDislikes(2);
        // Inicializa los campos nulos si es necesario
        reporte.setCategoria(null);
        reporte.setLocations(null);

        mockMvc.perform(post("/reportes")
                        .contentType(MediaType.APPLICATION_JSON_VALUE) // Solución para el error
                        .content(objectMapper.writeValueAsString(reporte)))
                // Sección de Assert: Validar el código de estado y la respuesta
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(reporte.getId()))
                .andExpect(jsonPath("$.idUsuario").value(reporte.getIdUsuario()))
                .andExpect(jsonPath("$.estado").value(reporte.getEstado().toString()))
                .andExpect(jsonPath("$.creadorAnuncio").value(reporte.getCreadorAnuncio()))
                .andExpect(jsonPath("$.titulo").value(reporte.getTitulo()))
                .andExpect(jsonPath("$.descripcion").value(reporte.getDescripcion()))
                .andExpect(jsonPath("$.ubicacion").value(reporte.getUbicacion()))
                .andExpect(jsonPath("$.likes").value(reporte.getLikes()))
                .andExpect(jsonPath("$.dislikes").value(reporte.getDislikes()));
    }
    @Test
    void testGetReporteSuccess() throws Exception {
        var reporte = reportes.values().stream().findAny().get();
        mockMvc.perform(post("/reportes/" + reporte.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reporte.getId()))
                .andExpect(jsonPath("$.idUsuario").value(reporte.getIdUsuario()))
                .andExpect(jsonPath("$.estado").value(reporte.getEstado().toString()))
                .andExpect(jsonPath("$.creadorAnuncio").value(reporte.getCreadorAnuncio()))
                .andExpect(jsonPath("$.titulo").value(reporte.getTitulo()))
                .andExpect(jsonPath("$.descripcion").value(reporte.getDescripcion()))
                .andExpect(jsonPath("$.ubicacion").value(reporte.getUbicacion()))
                .andExpect(jsonPath("$.likes").value(reporte.getLikes()))
                .andExpect(jsonPath("$.dislikes").value(reporte.getDislikes()));
    }

    @Test
    void testGetReporteNotFound() throws Exception {
        mockMvc.perform(post("/reportes/999")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }
}