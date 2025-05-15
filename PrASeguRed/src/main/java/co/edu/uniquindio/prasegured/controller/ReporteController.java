package co.edu.uniquindio.prasegured.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniquindio.prasegured.dto.ReporteDTO;
import co.edu.uniquindio.prasegured.dto.ReporteRequest;
import co.edu.uniquindio.prasegured.model.Usuario;
import co.edu.uniquindio.prasegured.repository.UsuarioRepository;
import co.edu.uniquindio.prasegured.security.JwtService;
import co.edu.uniquindio.prasegured.service.ReporteService;

@RestController
@RequestMapping("/reportes")
@CrossOrigin("*")
public class ReporteController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ReporteService reporteService;

    @PostMapping
    public ResponseEntity<?> reporteCreacion(@RequestHeader("Authorization") String token, @RequestBody ReporteRequest reporteRequest) {
        try {
            // Extraer el token sin el prefijo "Bearer "
            String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;

            // Validar el token y extraer el correo electrónico del usuario
            String correo = jwtService.extractUsername(jwtToken);

            // Buscar el usuario por correo electrónico
            Usuario usuario = usuarioRepository.findByCorreo(correo);

            if (usuario != null) {
                // Asociar el ID del usuario al reporte
                ReporteRequest reporteConUsuario = new ReporteRequest(
                    reporteRequest.id(),
                    usuario.getId(), // Asignar el ID del usuario autenticado
                    reporteRequest.titulo(),
                    reporteRequest.descripcion(),
                    reporteRequest.categoria(),
                    reporteRequest.locations(),
                    reporteRequest.imagenes()
                );
                
                // Guardar el reporte usando el servicio 
                ReporteDTO nuevoReporte = reporteService.save(reporteConUsuario);
                
                return ResponseEntity.status(HttpStatus.CREATED).body(
                    Map.of("message", "Reporte creado exitosamente.", "reporte", nuevoReporte)
                );
            } else {
                return ResponseEntity.status(404).body(Map.of("error", "Usuario no encontrado"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Token inválido o expirado", "detalle", e.getMessage()));
        }
    }
}