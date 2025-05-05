package co.edu.uniquindio.prasegured.security; // Ejemplo de paquete

import co.edu.uniquindio.prasegured.model.Usuario;
import co.edu.uniquindio.prasegured.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCorreo(correo);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado con el correo: " + correo);
        }
        // Aquí debes construir y retornar un objeto UserDetails a partir de tu entidad Usuario
        // Ten en cuenta cómo manejas la contraseña (si está concatenada o no)
        return new User(usuario.getCorreo(), usuario.getCorreo() + usuario.getContraseña(), Collections.emptyList());
    }
}