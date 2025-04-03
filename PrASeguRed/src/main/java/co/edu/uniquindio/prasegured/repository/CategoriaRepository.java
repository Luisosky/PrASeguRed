package co.edu.uniquindio.prasegured.repository;

import co.edu.uniquindio.prasegured.model.Categoria;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CategoriaRepository extends MongoRepository<Categoria, String> {
    Optional<Categoria> findByName(String name);
}