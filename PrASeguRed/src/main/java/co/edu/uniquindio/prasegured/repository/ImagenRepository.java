package co.edu.uniquindio.prasegured.repository;

import co.edu.uniquindio.prasegured.model.Imagen;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagenRepository extends MongoRepository<Imagen, String> {
}
