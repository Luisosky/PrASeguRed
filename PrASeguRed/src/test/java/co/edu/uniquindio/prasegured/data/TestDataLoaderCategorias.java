package co.edu.uniquindio.prasegured.data;

import co.edu.uniquindio.prasegured.model.Categoria;
import co.edu.uniquindio.prasegured.repository.CategoriaRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Profile("test")
public class TestDataLoaderCategorias {
    public static Map<String, Categoria> loadTestData(CategoriaRepository categoriaRepository, MongoTemplate mongoTemplate) {
        Categoria categoria1 = new Categoria();
        categoria1.setId("01");
        categoria1.setName("Animales");
        categoria1.setStatus("Activo");

        Categoria categoria2 = new Categoria();
        categoria2.setId("02");
        categoria2.setName("Tecnología");
        categoria2.setStatus("Inactivo");

        return loadTestData(
                List.of(categoria1, categoria2),
                categoriaRepository,
                mongoTemplate
        );
    }

    public static Map<String, Categoria> loadTestData
            (Collection<Categoria> newCategorias, CategoriaRepository categoriaRepository, MongoTemplate mongoTemplate) {
        mongoTemplate.getDb().listCollectionNames().forEach(mongoTemplate::dropCollection);
        return categoriaRepository.saveAll(newCategorias).stream()
                .collect(Collectors.toMap(Categoria::getId, categoria -> categoria));
    }
}
