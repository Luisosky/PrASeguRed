package co.edu.uniquindio.prasegured.controllers.unit;

import co.edu.uniquindio.prasegured.controller.ImagenController;
import co.edu.uniquindio.prasegured.dto.ImagenDTO;
import co.edu.uniquindio.prasegured.service.ImagenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ImagenController.class)
public class ImagenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImagenService imagenService;

    private ImagenDTO imagenDTO;

    @BeforeEach
    void setup() {
        imagenDTO = new ImagenDTO(
                "1",
                "reporte123",
                "usuario123",
                "imagen.jpg",
                new byte[]{1, 2, 3},
                co.edu.uniquindio.prasegured.model.EnumEstado.Espera
        );
    }

    @Test
    void testSubirImagenSuccess() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "imagen.jpg",
                "image/jpeg",
                new byte[]{1, 2, 3}
        );

        Mockito.when(imagenService.saveImagen(Mockito.any(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(imagenDTO);

        mockMvc.perform(multipart("/api/imagenes/subir")
                        .file(file)
                        .param("reporteId", "reporte123")
                        .param("usuarioId", "usuario123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(imagenDTO.id()))
                .andExpect(jsonPath("$.reporteId").value(imagenDTO.reporteId()))
                .andExpect(jsonPath("$.usuarioId").value(imagenDTO.usuarioId()))
                .andExpect(jsonPath("$.nombre").value(imagenDTO.nombre()))
                .andExpect(jsonPath("$.estado").value(imagenDTO.estado().toString()));
    }

    @Test
    void testSubirImagenError() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "imagen.jpg",
                "image/jpeg",
                new byte[]{1, 2, 3}
        );

        Mockito.when(imagenService.saveImagen(Mockito.any(), Mockito.anyString(), Mockito.anyString()))
                .thenThrow(new RuntimeException("Error al guardar la imagen"));

        mockMvc.perform(multipart("/api/imagenes/subir")
                        .file(file)
                        .param("reporteId", "reporte123")
                        .param("usuarioId", "usuario123"))
                .andExpect(status().isInternalServerError());
    }
}