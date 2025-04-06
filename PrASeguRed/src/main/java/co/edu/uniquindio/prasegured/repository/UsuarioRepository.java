package co.edu.uniquindio.prasegured.repository;

import co.edu.uniquindio.prasegured.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String>{
    Usuario findByCorreo(String correo);
}