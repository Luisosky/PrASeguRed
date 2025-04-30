package co.edu.uniquindio.prasegured.controllers.integration;

import co.edu.uniquindio.prasegured.data.TestDataLoaderCategorias;
import co.edu.uniquindio.prasegured.dto.CategoriaRequest;
import co.edu.uniquindio.prasegured.model.Categoria;
import co.edu.uniquindio.prasegured.model.ESTADOREPORTE;
import co.edu.uniquindio.prasegured.repository.CategoriaRepository;
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
public class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    private Map<String, Categoria> categorias;

    @BeforeEach
    void setUp() {
        categoriaRepository.deleteAll();
        categorias = TestDataLoaderCategorias.loadTestData(categoriaRepository, mongoTemplate);
    }

    @Test
    void testCreateCategoria() throws Exception {
        var categoriaRequest = new CategoriaRequest(
                "Categoria Nueva",
                "Descripción de la categoría",
                ESTADOREPORTE.Espera
        );

        mockMvc.perform(post("/categoria")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoriaRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Categoria Nueva"))
                .andExpect(jsonPath("$.descripcion").value("Descripción de la categoría"))
                .andExpect(jsonPath("$.tipoCategoria").value(ESTADOREPORTE.Espera.toString()));
    }

    @Test
    void testGetAllCategorias() throws Exception {
        mockMvc.perform(get("/categoria")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(categorias.size()));
    }

    @Test
    void testUpdateCategoria() throws Exception {
        var categoria = categorias.values().stream().findFirst().orElseThrow();

        var categoriaRequest = new CategoriaRequest(
                "Categoria Actualizada",
                "Nueva descripción de la categoría",
                ESTADOREPORTE.Espera
        );

        mockMvc.perform(put("/categoria/" + categoria.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoriaRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Categoria Actualizada"))
                .andExpect(jsonPath("$.descripcion").value("Nueva descripción de la categoría"))
                .andExpect(jsonPath("$.tipoCategoria").value(ESTADOREPORTE.Espera.toString()));
    }

    @Test
    void testDeleteCategoria() throws Exception {
        var categoria = categorias.values().stream().findFirst().orElseThrow();

        mockMvc.perform(delete("/categoria/" + categoria.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testFindCategoriaById() throws Exception {
        var categoria = categorias.values().stream().findFirst().orElseThrow();

        mockMvc.perform(get("/categoria/" + categoria.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(categoria.getId()))
                .andExpect(jsonPath("$.name").value(categoria.getName()))
                .andExpect(jsonPath("$.descripcion").value(categoria.getDescripcion()))
                .andExpect(jsonPath("$.tipoCategoria").value(categoria.getTipoCategoria().toString()));
    }
}