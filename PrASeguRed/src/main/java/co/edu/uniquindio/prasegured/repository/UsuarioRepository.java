package co.edu.uniquindio.prasegured.repository;

import co.edu.uniquindio.prasegured.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsuarioRepository extends MongoRepository<Usuario, String>{

}
