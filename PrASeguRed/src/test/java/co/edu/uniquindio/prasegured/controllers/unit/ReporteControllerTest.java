package co.edu.uniquindio.prasegured.controllers.unit;


import co.edu.uniquindio.prasegured.controller.ReportesController;
import co.edu.uniquindio.prasegured.dto.ReporteDTO;
import co.edu.uniquindio.prasegured.dto.ReporteRequest;
import co.edu.uniquindio.prasegured.exception.ResourceNotFoundException;
import co.edu.uniquindio.prasegured.exception.ValueConflictException;
import co.edu.uniquindio.prasegured.model.ESTADOREPORTE;
import co.edu.uniquindio.prasegured.service.ReporteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReportesController.class)
public class ReporteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReporteService reporteService;

    private ReporteRequest reporte;
    private ReporteDTO reporteDTO;

    @BeforeEach
    void setup() {
        reporte = new ReporteRequest(
                "1",
                "user123",
                "Reporte de prueba",
                new Date(),
                "Descripción del reporte",
                "Ubicación",
                List.of(),
                List.of()
        );

        reporteDTO = new ReporteDTO(
                "1",
                "user123",
                ESTADOREPORTE.Espera,
                "Juan",
                "Reporte de prueba",
                new Date(),
                null,
                "Descripción del reporte",
                "Ubicación",
                0,
                0,
                List.of(),
                List.of()
        );
    }

    @Test
    void testCreateReporteSuccess() throws Exception {
        Mockito.when(reporteService.save(Mockito.any(ReporteRequest.class))).thenReturn(reporteDTO);
        mockMvc.perform(post("/reportes")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(reporte)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value(reporteDTO.titulo()))
                .andExpect(jsonPath("$.descripcion").value(reporteDTO.descripcion()))
                .andExpect(jsonPath("$.ubicacion").value(reporteDTO.ubicacion()))
                .andExpect(jsonPath("$.estado").value(reporteDTO.estado().name()));
    }

    @Test
    void testCreateReporteConflict() throws Exception {
        Mockito.when(reporteService.save(Mockito.any(ReporteRequest.class))).thenThrow(ValueConflictException.class);

        mockMvc.perform(post("/reportes")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(reporte)))
                .andExpect(status().isConflict());
    }

    @Test
    void testGetReporteSuccess() throws Exception {
        Mockito.when(reporteService.findById(reporteDTO.id())).thenReturn(reporteDTO);

        mockMvc.perform(get("/reportes/" + reporteDTO.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value(reporteDTO.titulo()))
                .andExpect(jsonPath("$.descripcion").value(reporteDTO.descripcion()))
                .andExpect(jsonPath("$.ubicacion").value(reporteDTO.ubicacion()));
    }

    @Test
    void testGetReporteNotFound() throws Exception {
        Mockito.when(reporteService.findById(reporteDTO.id())).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get("/reportes/" + reporteDTO.id()))
                .andExpect(status().isNotFound());
    }
}