package co.edu.uniquindio.prasegured.data;

import co.edu.uniquindio.prasegured.model.EnumEstado;
import co.edu.uniquindio.prasegured.model.Reporte;
import co.edu.uniquindio.prasegured.repository.ReporteRepository;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.*;
import java.util.stream.Collectors;

public class TestDataLoaderReportes {
    public static Map<String, Reporte> loadTestData(ReporteRepository reporteRepository, MongoTemplate mongoTemplate) {
        Reporte reporte = new Reporte();
        reporte.setId("01");
        reporte.setIdUsuario("02");
        reporte.setEstado(EnumEstado.Espera);
        reporte.setCreadorAnuncio("Carlos");
        reporte.setTitulo("Se regalan gatos");
        reporte.setFechaPublicacion(new Date());
        reporte.setFechaActualizacion(null);
        reporte.setDescripcion("todo el que quiera adoptar puede");
        reporte.setUbicacion("Casa azul");
        reporte.setLikes(6);
        reporte.setDislikes(2);
        reporte.setCategoria(null);
        reporte.setLocations(null);

        Reporte reportea = new Reporte();
        reportea.setId("02");
        reportea.setIdUsuario("03");
        reportea.setEstado(EnumEstado.Espera);
        reportea.setCreadorAnuncio("Ana");
        reportea.setTitulo("Se regalan perros");
        reportea.setFechaPublicacion(new Date());
        reportea.setDescripcion("perros de raza pequeña");
        reportea.setUbicacion("Parque central");
        reportea.setLikes(10);
        reportea.setDislikes(1);
        reportea.setCategoria(null);
        reportea.setLocations(null);
        return loadTestData(
                List.of(reporte,reportea),
                reporteRepository,
                mongoTemplate
        );
    }

    public static Map<String, Reporte> loadTestData
            (Collection<Reporte> newReportes, ReporteRepository reporteRepository, MongoTemplate mongoTemplate) {
        mongoTemplate.getDb().listCollectionNames().forEach(mongoTemplate::dropCollection);
        return reporteRepository.saveAll(newReportes).stream()
                .collect(Collectors.toMap(Reporte::getId, reporte -> reporte));
    }
}
