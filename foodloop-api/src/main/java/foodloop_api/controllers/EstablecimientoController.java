package foodloop_api.controllers;

import foodloop_api.dto.EstablecimientoDTO;
import foodloop_api.models.Establecimiento;
import foodloop_api.services.EstablecimientoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/establecimientos")
@Tag(name = "Establecimientos", description = "API para la gestión de locales y comercios gastronómicos")
public class EstablecimientoController {

    private final EstablecimientoService service;

    public EstablecimientoController(EstablecimientoService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar establecimientos", description = "Devuelve todos los comercios afiliados")
    public List<Establecimiento> listar() {
        return service.listar();
    }

    @PostMapping
    @Operation(summary = "Registrar establecimiento", description = "Registra un nuevo local asociado a FoodLoop")
    public Establecimiento guardar(@Valid @RequestBody EstablecimientoDTO dto) {
        return service.guardar(dto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar establecimiento por ID", description = "Obtiene los detalles de un comercio por su ID")
    public Establecimiento buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar establecimiento", description = "Actualiza la información comercial del establecimiento")
    public Establecimiento actualizar(@PathVariable Long id, @Valid @RequestBody EstablecimientoDTO dto) {
        return service.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar establecimiento", description = "Elimina un comercio del registro")
    public String eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return "Establecimiento eliminado exitosamente";
    }
}