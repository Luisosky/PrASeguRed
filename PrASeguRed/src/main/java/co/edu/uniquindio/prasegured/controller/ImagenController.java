package co.edu.uniquindio.prasegured.controller;

import co.edu.uniquindio.prasegured.dto.ImagenDTO;
import co.edu.uniquindio.prasegured.service.ImagenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/imagenes")
@RequiredArgsConstructor
public class ImagenController {

    private final ImagenService imagenService;

    @PostMapping("/subir")
    public ResponseEntity<ImagenDTO> subirImagen(
            @RequestParam("file") MultipartFile file,
            @RequestParam("reporteId") String reporteId,
            @RequestParam("usuarioId") String usuarioId
    ) throws IOException {
        ImagenDTO dto = imagenService.saveImagen(file, reporteId, usuarioId);
        return ResponseEntity.ok(dto);
    }
}