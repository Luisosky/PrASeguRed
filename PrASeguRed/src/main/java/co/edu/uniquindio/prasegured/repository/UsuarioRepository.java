package co.edu.uniquindio.prasegured.repository;

import co.edu.uniquindio.prasegured.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.mongodb.repository.Query;

import java.util.List;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String>{
    Usuario findByCorreo(String correo);

    Usuario getUsuarioById(String correo);

    @Query("{ 'estado' : ?0 }")
    List<Usuario> findByEstado(String estado);

    @Query("{ '_id' : ?0, 'estado' : ?1 }")
    Usuario findByIdAndEstado(String id, String estado);

    @Query("{ 'correo' : ?0, 'estado' : ?1 }")
    Usuario findByCorreoAndEstado(String correo, String estado);

    // Estados solamente, regresa una lista
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

    //Estados y ID, retorna un objeto Usuario
    default Usuario findUsuarioActivoById(String id) {
        return findByIdAndEstado(id, "ACTIVO");
    }

    default Usuario findUsuarioInactivoById(String id) {
        return findByIdAndEstado(id, "INACTIVO");
    }

    default Usuario findUsuarioBloqueadoById(String id) {
        return findByIdAndEstado(id, "BLOQUEADO");
    }

    default Usuario findUsuarioEnEsperaById(String id) {
        return findByIdAndEstado(id, "EN_ESPERA");
    }

    // Por correo y estado, devuelve objeto Usuario
    default Usuario findUsuarioActivoByCorreo(String correo) {
        return findByCorreoAndEstado(correo, "ACTIVO");
    }

    default Usuario findUsuarioInactivoByCorreo(String correo) {
        return findByCorreoAndEstado(correo, "INACTIVO");
    }

    default Usuario findUsuarioBloqueadoByCorreo(String correo) {
        return findByCorreoAndEstado(correo, "BLOQUEADO");
    }

    default Usuario findUsuarioEnEsperaByCorreo(String correo) {
        return findByCorreoAndEstado(correo, "EN_ESPERA");
    }


}