package foodloop_api.controllers;

import foodloop_api.dto.ProductoDTO;
import foodloop_api.models.Producto;
import foodloop_api.services.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
@Tag(name = "Productos", description = "API para gestionar los excedentes alimentarios")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    @Operation(summary = "Listar excedentes", description = "Devuelve todos los productos disponibles en la base de datos")
    public List<Producto> listar() {
        return productoService.listar();
    }

    @PostMapping
    @Operation(summary = "Registrar excedente", description = "Crea un nuevo producto a partir de un DTO validado")
    public Producto guardar(@Valid @RequestBody ProductoDTO dto) {
        return productoService.insertar(dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar excedente", description = "Elimina un producto mediante su ID")
    public String eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return "Producto eliminado exitosamente";
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID", description = "Devuelve un único producto filtrado por su ID")
    public Producto buscarPorId(@PathVariable Long id) {
        return productoService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar excedente", description = "Modifica los datos de un producto ya existente")
    public Producto actualizar(@PathVariable Long id, @Valid @RequestBody ProductoDTO dto) {
        return productoService.actualizar(id, dto);
    }
}