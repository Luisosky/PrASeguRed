package co.edu.uniquindio.prasegured.service;

import co.edu.uniquindio.prasegured.model.Usuario;
import co.edu.uniquindio.prasegured.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Usuario registrarUsuario(Usuario usuario){
        String rawPassword = usuario.getDocumento() + usuario.getContraseña();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        usuario.setContraseña(encodedPassword);
        return usuarioRepository.save(usuario);
    }
}
