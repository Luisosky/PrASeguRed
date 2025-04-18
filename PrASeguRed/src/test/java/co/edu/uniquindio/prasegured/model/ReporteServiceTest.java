package co.edu.uniquindio.prasegured.model;

import co.edu.uniquindio.prasegured.repository.ReporteRepository;
import co.edu.uniquindio.prasegured.service.ReporteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReporteServiceTest {
//    @Autowired
//    private ReporteRepository reporteRepository;
//
//    @Test
//    void crearReporte(){
//        Reporte reporte = new Reporte();
//        reporte.setId("01");
//        reporte.setIdUsuario("02");
//        reporte.setEstado(EnumEstado.Espera);
//        reporte.setCreadorAnuncio("Carlos");
//        reporte.setTitulo("Se regalan gatos");
//        reporte.setFechaPublicacion(new Date());
//        reporte.setFechaActualizacion(null);
//        reporte.setDescripcion("todo el que quiera adoptar puede");
//        reporte.setUbicacion("Casa azul");
//        reporte.setLikes(6);
//        reporte.setDislikes(2);
//        reporte.setCategoria(null);
//        reporte.setLocations(null);
//
//        Reporte guardado = reporteRepository.save(reporte);
//        Optional<Reporte> buscado = reporteRepository.findById("01");
//        assertTrue(buscado.isPresent());
//        assertEquals(EnumEstado.Espera, buscado.get().getEstado());
//    }
//    @Test
//    void eliminarReporte(){
//        Reporte reporte = new Reporte();
//        reporte.setId("02");
//        reporte.setIdUsuario("03");
//        reporte.setEstado(EnumEstado.Espera);
//        reporte.setCreadorAnuncio("Ana");
//        reporte.setTitulo("Se regalan perros");
//        reporte.setFechaPublicacion(new Date());
//        reporte.setDescripcion("perros de raza pequeña");
//        reporte.setUbicacion("Parque central");
//        reporte.setLikes(10);
//        reporte.setDislikes(1);
//        reporte.setCategoria(null);
//        reporte.setLocations(null);
//
//        reporteRepository.save(reporte);
//        assertTrue(reporteRepository.findById("02").isPresent());
//
//        // Eliminar
//        reporteRepository.deleteById("02");
//        assertTrue(reporteRepository.findById("02").isEmpty());
//    }
}
