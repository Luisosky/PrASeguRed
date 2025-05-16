package co.edu.uniquindio.prasegured.service;

import co.edu.uniquindio.prasegured.dto.ActualizacionCuentaDTO;
import co.edu.uniquindio.prasegured.model.ESTADOSUSUARIO;
import co.edu.uniquindio.prasegured.model.ROL;
import co.edu.uniquindio.prasegured.model.Usuario;
import co.edu.uniquindio.prasegured.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Usuario registrarUsuario(Usuario usuario){
        String rawPassword = usuario.getCorreo() + usuario.getContraseña();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        usuario.setContraseña(encodedPassword);
        usuario.setRol(ROL.USUARIO.toString());
        usuario.setEstado(ESTADOSUSUARIO.EN_ESPERA.toString());
        return usuarioRepository.save(usuario);
    }

    public Usuario actualizarDatosUsuario(String userId, ActualizacionCuentaDTO datosActualizados) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        // Verificar que el usuario esté activo
        if (!usuario.getEstado().equals(ESTADOSUSUARIO.ACTIVO.toString())) {
            throw new RuntimeException("El usuario no está activo");
        }
        
        // Actualizar solo los campos que no sean nulos
        if (datosActualizados.getNombreCom() != null) {
            usuario.setNombreCom(datosActualizados.getNombreCom());
        }
        
        if (datosActualizados.getCiudadResidencia() != null) {
            usuario.setCiudadResidencia(datosActualizados.getCiudadResidencia());
        }
        
        if (datosActualizados.getDireccion() != null) {
            usuario.setDireccion(datosActualizados.getDireccion());
        }
        
        if (datosActualizados.getTelefono() != null) {
            usuario.setTelefono(datosActualizados.getTelefono());
        }
        
        if (datosActualizados.getCorreo() != null) {
            // Verificar que el correo no esté siendo usado por otro usuario
            Usuario usuarioExistente = usuarioRepository.findByCorreo(datosActualizados.getCorreo());
            if (usuarioExistente != null && !usuarioExistente.getId().equals(userId)) {
                throw new RuntimeException("El correo ya está siendo utilizado por otro usuario");
            }
            usuario.setCorreo(datosActualizados.getCorreo());
        }
        
        if (datosActualizados.getPreferencias() != null) {
            usuario.setPreferencias(datosActualizados.getPreferencias());
        }
        
        return usuarioRepository.save(usuario);
}

    public void deleteUsuarioByCorreo(String correo) {
        var usuario = usuarioRepository.findByCorreo(correo);
        if (usuario != null) {
            usuario.setEstado(ESTADOSUSUARIO.INACTIVO.toString());
        }
        usuarioRepository.save(usuario);
    }

    public void deleteUsuarioById(String id) {
        var user = usuarioRepository.getUsuarioById(id);
        if (user != null) {
            user.setEstado(ESTADOSUSUARIO.INACTIVO.toString());
        }
        usuarioRepository.save(user);
    }

    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    // Método para obtener usuarios por estado, lista
    public List<Usuario> getUsuariosByEstado(String estado) {
        return usuarioRepository.findByEstado(estado);
    }

    public List<Usuario> getUsuariosActivos() {
        return usuarioRepository.findUsuariosActivos();
    }

    public List<Usuario> getUsuariosInactivos() {
        return usuarioRepository.findUsuariosInactivos();
    }

    public List<Usuario> getUsuariosBloqueados() {
        return usuarioRepository.findUsuariosBloqueados();
    }

    public List<Usuario> getUsuariosEnEspera() {
        return usuarioRepository.findUsuariosEnEspera();
    }

    // Método para obtener usuarios por estado y ID/ estado y correo, objeto usuario
    public Usuario getUsuarioByIdAndEstado(String id, String estado) {
        return usuarioRepository.findByIdAndEstado(id, estado);
    }

    public Usuario getUsuarioByCorreoAndEstado(String correo, String estado) {
        return usuarioRepository.findByCorreoAndEstado(correo, estado);
    }

    public Usuario getUsuarioActivoById(String id) {
        return usuarioRepository.findUsuarioActivoById(id);
    }

    public Usuario getUsuarioActivoByCorreo(String correo) {
        return usuarioRepository.findUsuarioActivoByCorreo(correo);
    }

}