package co.edu.uniquindio.prasegured.data;

import co.edu.uniquindio.prasegured.model.Imagen;
import co.edu.uniquindio.prasegured.repository.ImagenRepository;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.*;
import java.util.stream.Collectors;

public class TestDataLoaderImagenes {
    public static Map<String, Imagen> loadTestData(ImagenRepository imagenRepository, MongoTemplate mongoTemplate) {
        Imagen imagen1 = new Imagen();
        imagen1.setId("01");
        imagen1.setReporteId("02");
        imagen1.setUsuarioId("03");

        Imagen imagen2 = new Imagen();
        imagen2.setId("02");
        imagen2.setReporteId("03");
        imagen2.setUsuarioId("04");

        return loadTestData(
                List.of(imagen1, imagen2),
                imagenRepository,
                mongoTemplate
        );
    }

    public static Map<String, Imagen> loadTestData
            (Collection<Imagen> newImagenes, ImagenRepository imagenRepository, MongoTemplate mongoTemplate) {
        mongoTemplate.getDb().listCollectionNames().forEach(mongoTemplate::dropCollection);
        return imagenRepository.saveAll(newImagenes).stream()
                .collect(Collectors.toMap(Imagen::getId, imagen -> imagen));
    }
}