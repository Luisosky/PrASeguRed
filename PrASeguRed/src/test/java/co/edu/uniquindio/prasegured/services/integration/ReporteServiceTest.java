package co.edu.uniquindio.prasegured.services.integration;

import co.edu.uniquindio.prasegured.data.TestDataLoaderReportes;
import co.edu.uniquindio.prasegured.dto.ReporteRequest;
import co.edu.uniquindio.prasegured.model.EnumEstado;
import co.edu.uniquindio.prasegured.model.Reporte;
import co.edu.uniquindio.prasegured.repository.ReporteRepository;
import co.edu.uniquindio.prasegured.service.ReporteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Date;
import java.util.Map;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class ReporteServiceTest {
    @Autowired
    private ReporteService reporteService;
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
    void testCreateReporte() {
        // Arrange: Se crean los datos del reporte a ser registrado
        var reporte = new ReporteRequest("42", "07", "Se perdió perrito",
                new Date(), "Última vez visto en el parque central. Responde al nombre de Max.",
                "Parque Central", null, null);

        var newReporte = reporteService.save(reporte);

        // Assert: Se verifica que el reporte se haya registrado con los datos proporcionados.
        assertNotNull(newReporte.id());
        assertEquals(newReporte.idUsuario(), reporte.idUsuario());
        assertEquals(newReporte.titulo(), reporte.titulo());
        assertEquals(newReporte.fechaPublicacion(), reporte.fechaPublicacion());
        assertEquals(newReporte.descripcion(), reporte.descripcion());
        assertEquals(newReporte.ubicacion(), reporte.ubicacion());
        assertEquals(newReporte.locations(), reporte.locations());
        assertEquals(newReporte.categoria(), reporte.categoria());

    }

    @Test
    void testCreateReporteThrowsValueConflictExceptionWhenIdExists() {
        // Arrange: Se crean los datos del reporte a ser registrado (Con el id de un reporte ya existente).
        var reporteStore = reportes.values().stream().findAny().orElseThrow();
        var reporte = new ReporteRequest(reporteStore.getId(), "07", "Se perdió perrito",
                new Date(), "Última vez visto en el parque central. Responde al nombre de Max.",
                "Parque Central", null, null);
        // Act: Se intenta crear el nuevo reporte
        var exception = assertThrows(Exception.class, () -> {
            reporteService.save(reporte);
        });
        // Assert: Se verifica que se haya lanzado la excepción esperada
        assertEquals("El id ya existe", exception.getMessage());
    }

    @Test
    void testGetReporteCompleto() {
        // Arrange: Se obtiene aleatoriamente uno de los reportes registrado para pruebas.
        var reporteStore = reportes.values().stream().findAny().orElseThrow();
        // Act: Ejecute la acción de obtener reporte basado en su Id.
        reporteService.reporteCompleto(reporteStore.getId());
        var foundReporte = reporteService.findById(reporteStore.getId());
        // Assert: Se verifica que los datos obtenidos correspondan a los del reporte almacenado.
        assertEquals(reporteStore.getId(), foundReporte.id());
        assertEquals(reporteStore.getIdUsuario(), foundReporte.idUsuario());
        assertEquals(reporteStore.getTitulo(), foundReporte.titulo());
        assertEquals(reporteStore.getFechaPublicacion(), foundReporte.fechaPublicacion());
        assertEquals(reporteStore.getDescripcion(), foundReporte.descripcion());
        assertEquals(reporteStore.getUbicacion(), foundReporte.ubicacion());
    }
    @Test
    void testUpdateReporte() {
        // Arrange: Se obtiene aleatoriamente uno de los reportes registrado para pruebas.
        var reporteStore = reportes.values().stream().findAny().orElseThrow();
        // Se crean los datos del reporte a ser registrado
        var reporte = new ReporteRequest(reporteStore.getId(), "07", "Se perdió perrito",
                new Date(), "Última vez visto en el parque central. Responde al nombre de Max.",
                "Parque Central", null, null);
        // Act: Ejecute la acción de actualizar el reporte basado en su Id.
        var updatedReporte = reporteService.update(reporteStore.getId(), reporte);
        // Assert: Se verifica que los datos obtenidos correspondan a los del reporte almacenado.
        assertEquals(reporteStore.getId(), updatedReporte.id());
        assertEquals(reporteStore.getIdUsuario(), updatedReporte.idUsuario());
        assertEquals(reporteStore.getTitulo(), updatedReporte.titulo());
        assertEquals(reporteStore.getFechaPublicacion(), updatedReporte.fechaPublicacion());
        assertEquals(reporteStore.getDescripcion(), updatedReporte.descripcion());
        assertEquals(reporteStore.getUbicacion(), updatedReporte.ubicacion());
    }
    @Test
    void testUpdateReporteThrowsValueConflictExceptionWhenIdExists() {
        // Arrange: Se crean los datos del reporte a ser registrado (Con el id de un reporte ya existente).
        var reporteStore = reportes.values().stream().findAny().orElseThrow();
        var reporte = new ReporteRequest(reporteStore.getId(), "07", "Se perdió perrito",
                new Date(), "Última vez visto en el parque central. Responde al nombre de Max.",
                "Parque Central", null, null);
        // Act: Se intenta crear el nuevo reporte
        var exception = assertThrows(Exception.class, () -> {
            reporteService.update(reporteStore.getId(), reporte);
        });
        // Assert: Se verifica que se haya lanzado la excepción esperada
        assertEquals("El id ya existe", exception.getMessage());
    }
    @Test
    void testEstadoDenegado(){
        // Arrange: Se obtiene aleatoriamente uno de los reportes registrado para pruebas.
        var reporteStore = reportes.values().stream().findAny().orElseThrow();
        // Act: Ejecute la acción de obtener reporte basado en su Id.
        reporteService.estadoDenegado(reporteStore.getId());
        var foundReporte = reporteService.findById(reporteStore.getId());
        // Assert: Se verifica que los datos obtenidos correspondan a los del reporte almacenado.
        assertEquals(EnumEstado.Denegado, foundReporte.estado());
    }
    @Test
    void testDeleteReporte() {
        // Arrange: Se obtiene aleatoriamente uno de los reportes registrado para pruebas.
        var reporteStore = reportes.values().stream().findAny().orElseThrow();
        // Act: Ejecute la acción de eliminar el reporte basado en su Id.
        reporteService.deleteById(reporteStore.getId());
        // Assert: Se verifica que el reporte ya no exista en la base de datos.
        var exception = assertThrows(Exception.class, () -> {
            reporteService.findById(reporteStore.getId());
        });
        assertEquals("El reporte no existe", exception.getMessage());
    }
    @Test
    void testGetAllReportes() {
        // Arrange: Se obtienen todos los reportes almacenados en la base de datos.
        var reportesStore = reportes.values();
        // Act: Ejecute la acción de obtener todos los reportes.
        var allReportes = reporteService.findAll();
        // Assert: Se verifica que la cantidad de reportes obtenidos sea igual a la cantidad de reportes almacenados.
        assertEquals(reportesStore.size(), allReportes.size());
    }


    @Test
    void testGetReporteNotFound() {
        // Arrange: Se crean los datos del reporte a ser registrado (Con el id de un reporte ya existente).
        var id = "nonexistentId";
        // Act: Ejecute la acción de obtener reporte basado en su Id.
        var exception = assertThrows(Exception.class, () -> {
            reporteService.findById(id);
        });
        // Assert: Se verifica que se haya lanzado la excepción esperada
        assertEquals("El reporte no existe", exception.getMessage());
    }
}
