package co.edu.uniquindio.prasegured.controller;

import co.edu.uniquindio.prasegured.model.Usuario;
import co.edu.uniquindio.prasegured.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registro")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/usuario")
    public ResponseEntity<String> registrarUsuario(@RequestBody Usuario usuario){
        usuarioService.registrarUsuario(usuario);
        return ResponseEntity.status(201).body("Registro exitoso");
    }
}