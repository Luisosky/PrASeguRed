package co.edu.uniquindio.prasegured.repository;

import co.edu.uniquindio.prasegured.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UsuarioRepository extends MongoRepository<Usuario, String>{
    Usuario findByCorreo(String correo);

    Usuario getUsuarioById(String correo);

    @Query("{ 'estado' : ?0 }")
    List<Usuario> findByEstado(String estado);

    default List<Usuario> findUsuariosActivos() {
        return findByEstado("ACTIVO");
    }

    default List<Usuario> findUsuariosInactivos() {
        return findByEstado("INACTIVO");
    }

    default List<Usuario> findUsuariosBloqueados() {
        return findByEstado("BLOQUEADO");
    }

    default List<Usuario> findUsuariosEnEspera() {
        return findByEstado("EN_ESPERA");
    }
}