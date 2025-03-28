package co.edu.uniquindio.prasegured.controller;

import co.edu.uniquindio.prasegured.dto.CredencialesDTO;
import co.edu.uniquindio.prasegured.model.Usuario;
import co.edu.uniquindio.prasegured.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/auth/login")
    public ResponseEntity<Void> login(@RequestBody CredencialesDTO credenciales) {
        Usuario usuario = usuarioRepository.findByCorreo(credenciales.correo());
        if(usuario != null){
            String rawPassword = credenciales.correo() + credenciales.contraseña();
            if(bCryptPasswordEncoder.matches(rawPassword, usuario.getContraseña())){
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.status(401).build();
    }
}



