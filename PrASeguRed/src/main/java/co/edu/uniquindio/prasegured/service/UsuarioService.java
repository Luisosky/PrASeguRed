package co.edu.uniquindio.prasegured.service;

import co.edu.uniquindio.prasegured.model.ESTADOS;
import co.edu.uniquindio.prasegured.model.Location;
import co.edu.uniquindio.prasegured.model.ROL;
import co.edu.uniquindio.prasegured.model.Usuario;
import co.edu.uniquindio.prasegured.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

import static co.edu.uniquindio.prasegured.model.ESTADOS.ACTIVO;

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
        usuario.setEstado(ESTADOS.EN_ESPERA.toString());
        return usuarioRepository.save(usuario);
    }

    public void deleteUsuarioByCorreo(String correo) {
        var usuario = usuarioRepository.findByCorreo(correo);
        if (usuario != null) {
            usuario.setEstado(ESTADOS.INACTIVO.toString());
        }
        usuarioRepository.save(usuario);
    }

    public void deleteUsuarioById(String id) {
        var user = usuarioRepository.getUsuarioById(id);
        if (user != null) {
            user.setEstado(ESTADOS.INACTIVO.toString());
        }
        usuarioRepository.save(user);
    }

    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

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
}