package co.edu.uniquindio.prasegured.controller;

import co.edu.uniquindio.prasegured.model.Imagen;
import co.edu.uniquindio.prasegured.service.ImagenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/imagen")
public class ImagenController {
    @Autowired
    private ImagenService imagenService;

    @PostMapping("/Subir")
    public ResponseEntity<Imagen> subirImagen(@RequestParam("file") MultipartFile file) {
        try {
            imagenService.saveImagen(file);
            return new ResponseEntity<>("Imagen successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error uploading image", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
