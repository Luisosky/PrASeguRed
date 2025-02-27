package co.edu.uniquindio.prasegured.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MapBoxController {

    private final String mapboxToken;

    @Autowired
    public MapBoxController(String mapboxToken) {
        this.mapboxToken = mapboxToken;
    }

    @GetMapping("/")
    public String mapPage(Model model) {
        // Añadir el mapbox acces token para renderizar el mapa en Thymeleaf
        model.addAttribute("mapboxAccessToken", mapboxToken);
        return "index";
    }
}