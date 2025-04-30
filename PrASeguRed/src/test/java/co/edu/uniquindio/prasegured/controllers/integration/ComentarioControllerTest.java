package co.edu.uniquindio.prasegured.controllers.integration;

import co.edu.uniquindio.prasegured.data.TestDataLoaderComentarios;
import co.edu.uniquindio.prasegured.dto.ComentarioRequest;
import co.edu.uniquindio.prasegured.model.Comentario;
import co.edu.uniquindio.prasegured.model.ESTADOREPORTE;
import co.edu.uniquindio.prasegured.repository.ComentarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ComentarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private Map<String, Comentario> comentarios;

    @BeforeEach
    void setUp() {
        comentarioRepository.deleteAll();
        comentarios = TestDataLoaderComentarios.loadTestData(comentarioRepository, mongoTemplate);
    }

    @Test
    void testGuardarComentario() throws Exception {
        var comentarioRequest = new ComentarioRequest(
                "102",
                "202",
                true,
                "Anónimo",
                "Nuevo comentario",
                0,
                0
        );

        mockMvc.perform(post("/api/comentarios/guardar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comentarioRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idReporte").value("102"))
                .andExpect(jsonPath("$.idUsuario").value("202"))
                .andExpect(jsonPath("$.anonimo").value(true))
                .andExpect(jsonPath("$.nombre").value("Anónimo"))
                .andExpect(jsonPath("$.descripcion").value("Nuevo comentario"))
                .andExpect(jsonPath("$.likes").value(0))
                .andExpect(jsonPath("$.dislikes").value(0));
    }

    @Test
    void testObtenerComentariosPorReporte() throws Exception {
        var comentario = comentarios.values().stream()
                .filter(c -> c.getIdReporte().equals("101"))
                .findFirst()
                .orElseThrow();

        mockMvc.perform(get("/api/comentarios/por-reporte/" + comentario.getIdReporte())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].idReporte").value(comentario.getIdReporte()));
    }

    @Test
    void testObtenerComentariosPorUsuario() throws Exception {
        var comentario = comentarios.values().stream()
                .filter(c -> c.getIdUsuario().equals("201"))
                .findFirst()
                .orElseThrow();

        mockMvc.perform(get("/api/comentarios/por-usuario/" + comentario.getIdUsuario())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].idUsuario").value(comentario.getIdUsuario()));
    }

    @Test
    void testEliminarComentario() throws Exception {
        var comentario = comentarios.values().stream().findFirst().orElseThrow();

        mockMvc.perform(delete("/api/comentarios/" + comentario.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDenegarComentario() throws Exception {
        var comentario = comentarios.values().stream().findFirst().orElseThrow();

        mockMvc.perform(patch("/api/comentarios/denegar/" + comentario.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/comentarios/por-reporte/" + comentario.getIdReporte())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].estado").value(ESTADOREPORTE.Denegado.toString()));
    }
}