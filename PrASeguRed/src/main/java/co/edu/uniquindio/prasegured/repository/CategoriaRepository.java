package co.edu.uniquindio.prasegured.repository;

import co.edu.uniquindio.prasegured.model.Categoria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends MongoRepository<Categoria, String> {
    Optional<Categoria> findByName(String name);
    List<Categoria> findByStatusNot(String status);
}
