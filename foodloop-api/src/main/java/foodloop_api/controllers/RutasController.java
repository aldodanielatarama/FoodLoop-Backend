package foodloop_api.controllers;
import foodloop_api.services.RutasService;

import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController @RequestMapping("/api/foodloop/rutas") 
public class RutasController {
 private final RutasService servicio;
 @GetMapping("/eta")
 public Map<String,Object> eta(@RequestParam double origenLat,@RequestParam double origenLon,
         @RequestParam double destinoLat,@RequestParam double destinoLon) {
     return servicio.calcular(origenLat,origenLon,destinoLat,destinoLon);
 }

    // Metodos escritos de forma explicita, sin Lombok.
    public RutasController(RutasService servicio) {
        this.servicio = servicio;
    }

}
