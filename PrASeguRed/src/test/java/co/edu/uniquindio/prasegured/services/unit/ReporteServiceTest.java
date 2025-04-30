package co.edu.uniquindio.prasegured.services.unit;

import co.edu.uniquindio.prasegured.dto.ReporteDTO;
import co.edu.uniquindio.prasegured.dto.ReporteRequest;
import co.edu.uniquindio.prasegured.exception.ResourceNotFoundException;
import co.edu.uniquindio.prasegured.mapper.ReporteMapper;
import co.edu.uniquindio.prasegured.model.ESTADOREPORTE;
import co.edu.uniquindio.prasegured.model.Reporte;
import co.edu.uniquindio.prasegured.repository.ReporteRepository;
import co.edu.uniquindio.prasegured.service.ReporteServiceImple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any; // Importación corregida
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class) // Habilita Mockito en JUnit 5
public class ReporteServiceTest {
    @Mock
    private ReporteRepository reporteRepository;
    @Mock
    private ReporteMapper reporteMapper;

    @InjectMocks
    private ReporteServiceImple reporteService;

    private ReporteRequest reporteRequest;
    private ReporteDTO reporteDTO;
    private Reporte reporte; // Declaración a nivel de clase

    @BeforeEach
    void setUp() {
        // Inicializar el objeto Reporte
        reporte = new Reporte();
        reporte.setId("02");
        reporte.setIdUsuario("03");
        reporte.setEstado(ESTADOREPORTE.Espera);
        reporte.setCreadorAnuncio("Ana");
        reporte.setTitulo("Se regalan perros");
        reporte.setFechaPublicacion(new Date());
        reporte.setDescripcion("Perros de raza pequeña");
        reporte.setUbicacion("Parque central");
        reporte.setLikes(10);
        reporte.setDislikes(1);
        reporte.setCategoria(null);
        reporte.setLocations(null);
        // Inicializar los objetos ReporteRequest y ReporteDTO
        reporteRequest = new ReporteRequest(
                reporte.getId(),
                reporte.getIdUsuario(),
                reporte.getTitulo(),
                reporte.getFechaPublicacion(),
                reporte.getDescripcion(),
                reporte.getUbicacion(),
                reporte.getCategoria(),
                reporte.getLocations()
        );
        reporteDTO = new ReporteDTO(
                reporte.getId(),
                reporte.getIdUsuario(),
                reporte.getEstado(),
                reporte.getCreadorAnuncio(),
                reporte.getTitulo(),
                reporte.getFechaPublicacion(),
                new Date(), // Fecha de actualización arbitraria
                reporte.getDescripcion(),
                reporte.getUbicacion(),
                reporte.getLikes(),
                reporte.getDislikes(),
                reporte.getCategoria(),
                reporte.getLocations()
        );
    }

    @Test
    void testCreateReporteSuccess() {
        // Arrange: Simular que no existe un reporte con el ID dado
        when(reporteRepository.findById(reporteRequest.id())).thenReturn(Optional.empty());
        when(reporteMapper.parseOf(reporteRequest)).thenReturn(reporte); // Simular conversión DTO -> Entity
        when(reporteRepository.save(any(Reporte.class))).thenReturn(reporte); // Simular persistencia
        when(reporteMapper.toReporteDTO(any(Reporte.class))).thenReturn(reporteDTO); // Simular conversión Entity -> DTO
        // Act: Llamar al método que se está probando
        ReporteDTO result = reporteService.save(reporteRequest);
        // Assert: Verificar el resultado esperado
        assertNotNull(result);
        assertEquals(reporteDTO.id(), result.id());
        assertEquals(reporteDTO.titulo(), result.titulo());
        assertEquals(reporteDTO.idUsuario(), result.idUsuario());
        // Verificar que los mocks fueron llamados correctamente
        verify(reporteRepository).findById(reporteRequest.id());
        verify(reporteMapper).parseOf(reporteRequest); // Verificar mapeo DTO -> Entity
        verify(reporteRepository).save(any(Reporte.class));
        verify(reporteMapper).toReporteDTO(any(Reporte.class)); // Verificar mapeo Entity -> DTO
    }
    @Test
    void testUpdateReporteSuccess() {
        // Arrange: Simular que el reporte existe
        when(reporteRepository.findById(reporteRequest.id())).thenReturn(Optional.of(reporte));
        when(reporteRepository.save(any(Reporte.class))).thenReturn(reporte); // Simular persistencia
        when(reporteMapper.toReporteDTO(any(Reporte.class))).thenReturn(reporteDTO); // Simular conversión Entity -> DTO

        // Act: Llamar al método que se está probando
        ReporteDTO result = reporteService.update(reporteRequest.id(), reporteRequest);

        // Assert: Verificar el resultado esperado
        assertNotNull(result); // Verificar que no sea nulo
        assertEquals(reporteDTO.id(), result.id()); // Verificar ID
        assertEquals(reporteDTO.titulo(), result.titulo()); // Verificar título
        assertEquals(reporteDTO.idUsuario(), result.idUsuario()); // Verificar ID de usuario

        // Verificar que los mocks fueron llamados correctamente
        verify(reporteRepository).findById(reporteRequest.id()); // Verificar búsqueda por ID
        verify(reporteRepository).save(any(Reporte.class)); // Verificar persistencia
        verify(reporteMapper).toReporteDTO(any(Reporte.class)); // Verificar mapeo Entity -> DTO
    }
    @Test
    void testUpdateReporteNotFound() {
        // Arrange: Simular que el reporte no existe
        when(reporteRepository.findById(reporteRequest.id())).thenReturn(Optional.empty());

        // Act & Assert: Verificar que se lanza la excepción adecuada
        assertThrows(ResourceNotFoundException.class, () -> reporteService.update(reporteRequest.id(), reporteRequest));

        // Verificar que se intentó buscar el reporte
        verify(reporteRepository).findById(reporteRequest.id());
        // Verificar que no se intentó guardar un reporte
        verify(reporteRepository, never()).save(any(Reporte.class));
    }
    @Test
    void testFindByIdSuccess() {
        // Arrange: Simular que el reporte existe
        when(reporteRepository.findById(reporteRequest.id())).thenReturn(Optional.of(reporte));
        when(reporteMapper.toReporteDTO(any(Reporte.class))).thenReturn(reporteDTO); // Simular conversión Entity -> DTO
        // Act: Llamar al método que se está probando
        ReporteDTO result = reporteService.findById(reporteRequest.id());
        // Assert: Verificar el resultado esperado
        assertNotNull(result);
        assertEquals(reporteDTO.id(), result.id());
        assertEquals(reporteDTO.titulo(), result.titulo());
        assertEquals(reporteDTO.idUsuario(), result.idUsuario());
        // Verificar que los mocks fueron llamados correctamente
        verify(reporteRepository).findById(reporteRequest.id());
        verify(reporteMapper).toReporteDTO(any(Reporte.class)); // Verificar mapeo Entity -> DTO
    }

    @Test
    void testDeleteByIdSuccess() {
        // Arrange: Simular que el reporte existe
        when(reporteRepository.findById(reporteRequest.id())).thenReturn(Optional.of(reporte));
        // Act: Llamar al método que se está probando
        reporteService.deleteById(reporteRequest.id());
        // Assert: Verificar que el estado fue actualizado a "Eliminado"
        assertEquals(ESTADOREPORTE.Eliminado, reporte.getEstado()); // Verificar que el estado cambió
        verify(reporteRepository).save(reporte); // Verificar que se guardó el reporte actualizado
    }
    @Test
    void testReporteCompletoSuccess() {
        // Arrange: Simular que el reporte existe
        when(reporteRepository.findById(reporteRequest.id())).thenReturn(Optional.of(reporte));

        // Act: Llamar al método que se está probando
        reporteService.reporteCompleto(reporteRequest.id());

        // Assert: Verificar que el estado fue actualizado
        verify(reporteRepository).save(any(Reporte.class));
    }
    @Test
    void testEstadoDenegadoSuccess() {
        // Arrange: Simular que el reporte existe
        when(reporteRepository.findById(reporteRequest.id())).thenReturn(Optional.of(reporte));

        // Act: Llamar al método que se está probando
        reporteService.estadoDenegado(reporteRequest.id());

        // Assert: Verificar que el estado fue actualizado
        verify(reporteRepository).save(any(Reporte.class));
    }
}