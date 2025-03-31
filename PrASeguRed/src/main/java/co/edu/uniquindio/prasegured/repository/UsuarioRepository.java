package co.edu.uniquindio.prasegured.repository;

import co.edu.uniquindio.prasegured.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UsuarioRepository extends MongoRepository<Usuario, String>{
    Usuario findByCorreo(String correo);

    Usuario getUsuarioById(String correo);
}