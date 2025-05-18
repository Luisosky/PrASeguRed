package co.edu.uniquindio.prasegured.security; 

import co.edu.uniquindio.prasegured.model.Usuario;
import co.edu.uniquindio.prasegured.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
        
        // Usamos el método User.withUsername para construir correctamente el UserDetails
        return User.withUsername(usuario.getCorreo())
                .password(usuario.getCorreo() + usuario.getContraseña())
                // Añadimos una autoridad que indica si el usuario está activo o no
                .authorities(usuario.isActivo() ? 
                    Collections.singletonList(new SimpleGrantedAuthority("ACTIVE_USER")) :
                    Collections.singletonList(new SimpleGrantedAuthority("INACTIVE_USER")))
                .build();
    }
}