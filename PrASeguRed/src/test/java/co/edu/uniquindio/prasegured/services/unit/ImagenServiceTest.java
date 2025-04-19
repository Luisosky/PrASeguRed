package co.edu.uniquindio.prasegured.services.unit;

import co.edu.uniquindio.prasegured.dto.ImagenDTO;
import co.edu.uniquindio.prasegured.exception.ValueConflictException;
import co.edu.uniquindio.prasegured.mapper.ImagenMapper;
import co.edu.uniquindio.prasegured.model.EnumEstado;
import co.edu.uniquindio.prasegured.model.Imagen;
import co.edu.uniquindio.prasegured.repository.ImagenRepository;
import co.edu.uniquindio.prasegured.service.ImagenServicesImple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ImagenServiceTest {

    @Mock
    private ImagenRepository imagenRepository;

    @Mock
    private ImagenMapper imagenMapper;

    @InjectMocks
    private ImagenServicesImple imagenService;

    private Imagen imagen;
    private ImagenDTO imagenDTO;
    private MockMultipartFile mockFile;

    @BeforeEach
    void setUp() {
        mockFile = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "contenido de prueba".getBytes()
        );

        imagen = new Imagen();
        imagen.setId(UUID.randomUUID().toString());
        imagen.setReporteId("rep01");
        imagen.setUsuarioId("user01");
        imagen.setNombre("test.jpg");
        imagen.setEstado(EnumEstado.Espera);
        imagen.setContent("contenido de prueba".getBytes());

        imagenDTO = new ImagenDTO(
                imagen.getId(),
                imagen.getReporteId(),
                imagen.getUsuarioId(),
                imagen.getNombre(),
                imagen.getContent(),
                imagen.getEstado()
        );
    }

    @Test
    void testSaveImagenSuccess() throws IOException {
        when(imagenRepository.save(any(Imagen.class))).thenReturn(imagen);
        when(imagenMapper.toImagenDTO(any(Imagen.class))).thenReturn(imagenDTO);

        ImagenDTO result = imagenService.saveImagen(mockFile, "rep01", "user01");

        assertNotNull(result);
        assertEquals(imagenDTO.id(), result.id());
        assertEquals(imagenDTO.nombre(), result.nombre());
        verify(imagenRepository).save(any(Imagen.class));
        verify(imagenMapper).toImagenDTO(any(Imagen.class));
    }

    @Test
    void testDeleteImagenSuccess() throws IOException {
        when(imagenRepository.findById(imagen.getId())).thenReturn(Optional.of(imagen));

        imagenService.deleteImagen(imagen.getId());

        verify(imagenRepository).findById(imagen.getId());
        verify(imagenRepository).save(imagen);
        assertEquals(EnumEstado.Eliminado, imagen.getEstado());
    }

    @Test
    void testDeleteImagenNotFound() {
        when(imagenRepository.findById(imagen.getId())).thenReturn(Optional.empty());

        assertThrows(ValueConflictException.class, () -> imagenService.deleteImagen(imagen.getId()));

        verify(imagenRepository).findById(imagen.getId());
        verify(imagenRepository, never()).save(any(Imagen.class));
    }

    @Test
    void testUpdateImagenSuccess() throws IOException {
        when(imagenRepository.findById(imagen.getId())).thenReturn(Optional.of(imagen));
        when(imagenRepository.save(any(Imagen.class))).thenReturn(imagen);
        when(imagenMapper.toImagenDTO(any(Imagen.class))).thenReturn(imagenDTO);

        ImagenDTO result = imagenService.updateImagen(imagen.getId(), mockFile);

        assertNotNull(result);
        assertEquals(imagenDTO.id(), result.id());
        assertEquals(imagenDTO.nombre(), result.nombre());
        verify(imagenRepository).findById(imagen.getId());
        verify(imagenRepository).save(any(Imagen.class));
        verify(imagenMapper).toImagenDTO(any(Imagen.class));
    }

    @Test
    void testUpdateImagenNotFound() {
        when(imagenRepository.findById(imagen.getId())).thenReturn(Optional.empty());

        assertThrows(ValueConflictException.class, () -> imagenService.updateImagen(imagen.getId(), mockFile));

        verify(imagenRepository).findById(imagen.getId());
        verify(imagenRepository, never()).save(any(Imagen.class));
    }

    @Test
    void testEstadoDenegadoSuccess() throws IOException {
        when(imagenRepository.findById(imagen.getId())).thenReturn(Optional.of(imagen));

        imagenService.estadoDenegado(imagen.getId());

        verify(imagenRepository).findById(imagen.getId());
        verify(imagenRepository).save(imagen);
        assertEquals(EnumEstado.Denegado, imagen.getEstado());
    }

    @Test
    void testFindAllByReporteId() {
        when(imagenRepository.findByReporteId(imagen.getReporteId())).thenReturn(List.of(imagen));
        when(imagenMapper.toImagenDTO(imagen)).thenReturn(imagenDTO);

        List<ImagenDTO> result = imagenService.findAllByReporteId(imagen.getReporteId());

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(imagenRepository).findByReporteId(imagen.getReporteId());
        verify(imagenMapper).toImagenDTO(imagen);
    }

    @Test
    void testFindAllByUsuarioId() {
        when(imagenRepository.findByUsuarioId(imagen.getUsuarioId())).thenReturn(List.of(imagen));
        when(imagenMapper.toImagenDTO(imagen)).thenReturn(imagenDTO);

        List<ImagenDTO> result = imagenService.findAllByUsuarioId(imagen.getUsuarioId());

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(imagenRepository).findByUsuarioId(imagen.getUsuarioId());
        verify(imagenMapper).toImagenDTO(imagen);
    }
}