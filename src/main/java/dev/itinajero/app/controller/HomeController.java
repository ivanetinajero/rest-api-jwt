package dev.itinajero.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String landing(Model model) {
        // Secciones principales para la landing
        model.addAttribute("title", "Spring Boot REST API con JWT - Referencia Profesional");
        model.addAttribute("intro", "Esta referencia muestra cómo implementar y entender el flujo de autenticación JWT en una API REST profesional usando Spring Boot, Bootstrap 5 y Thymeleaf.");
        // El resto de los datos se pasan en la plantilla Thymeleaf
        return "landing";
    }
    
}
