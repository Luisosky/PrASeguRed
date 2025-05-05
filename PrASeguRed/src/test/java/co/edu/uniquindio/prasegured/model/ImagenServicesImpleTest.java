package co.edu.uniquindio.prasegured.model;

import co.edu.uniquindio.prasegured.dto.ImagenDTO;
import co.edu.uniquindio.prasegured.mapper.ImagenMapper;
import co.edu.uniquindio.prasegured.model.EnumEstado;
import co.edu.uniquindio.prasegured.model.Imagen;
import co.edu.uniquindio.prasegured.repository.ImagenRepository;
import co.edu.uniquindio.prasegured.service.ImagenServicesImple;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

class ImagenServicesImpleTest {

    private final ImagenRepository imagenRepository = Mockito.mock(ImagenRepository.class);
    private final ImagenMapper imagenMapper = Mockito.mock(ImagenMapper.class);
    private final ImagenServicesImple imagenServices = new ImagenServicesImple(imagenRepository, imagenMapper);

    @Test
    void testSaveImagen() throws IOException {
        // Simular un archivo MultipartFile
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test-image.jpg",
                "image/jpeg",
                "test content".getBytes()
        );

        // Crear un objeto Imagen simulado
        Imagen imagen = new Imagen();
        imagen.setId(UUID.randomUUID().toString());
        imagen.setNombre(file.getOriginalFilename());
        imagen.setContent(file.getBytes());
        imagen.setReporteId("reporte123");
        imagen.setUsuarioId("usuario123");
        imagen.setEstado(EnumEstado.Espera);

        // Configurar el comportamiento del repositorio y el mapper
        Mockito.when(imagenRepository.save(any(Imagen.class))).thenReturn(imagen);
        Mockito.when(imagenMapper.toImagenDTO(any(Imagen.class))).thenReturn(new ImagenDTO(imagen.getId(),imagen.getReporteId(),imagen.getUsuarioId(),
                imagen.getNombre(),imagen.getContent(),imagen.getEstado()));

        // Llamar al método a probar
        ImagenDTO result = imagenServices.saveImagen(file, "reporte123", "usuario123");

        // Verificar el resultado
        assertEquals(imagen.getId(), result.id());
        assertEquals(imagen.getNombre(), result.nombre());
        assertEquals(imagen.getEstado(), result.estado());

        // Verificar interacciones con los mocks
        Mockito.verify(imagenRepository).save(any(Imagen.class));
        Mockito.verify(imagenMapper).toImagenDTO(any(Imagen.class));
    }
}