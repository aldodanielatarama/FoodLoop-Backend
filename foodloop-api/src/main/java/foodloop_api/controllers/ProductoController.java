package foodloop_api.controllers;

import foodloop_api.dto.ProductoDTO;
import foodloop_api.models.Producto;
import foodloop_api.repositories.ProductoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
@Tag(name = "Productos", description = "API para gestionar los excedentes alimentarios")
public class ProductoController {

    private final ProductoRepository repository;

    public ProductoController(ProductoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    @Operation(summary = "Listar excedentes", description = "Devuelve todos los productos disponibles en la base de datos")
    public List<Producto> listar() {
        return repository.findAll();
    }

    @PostMapping
    @Operation(summary = "Registrar excedente", description = "Crea un nuevo producto a partir de un DTO validado")
    public Producto guardar(@Valid @RequestBody ProductoDTO dto) {
        Producto p = new Producto();
        p.setNombre(dto.getNombre());
        p.setCategoria(dto.getCategoria());
        p.setPrecioDescuento(dto.getPrecioDescuento());
        p.setStock(dto.getStock());
        return repository.save(p);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar excedente", description = "Elimina un producto mediante su ID")
    public String eliminar(@PathVariable Long id) {
        repository.deleteById(id);
        return "Producto eliminado exitosamente";
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID", description = "Devuelve un único producto filtrado por su ID")
    public Producto buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Producto no encontrado con el ID " + id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar excedente", description = "Modifica los datos de un producto ya existente")
    public Producto actualizar(@PathVariable Long id, @Valid @RequestBody ProductoDTO dto) {
        // 1. Buscamos si el producto existe
        Producto productoExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Producto no encontrado con el ID " + id));
        
        // 2. Actualizamos sus datos con lo que envíe el cliente
        productoExistente.setNombre(dto.getNombre());
        productoExistente.setCategoria(dto.getCategoria());
        productoExistente.setPrecioDescuento(dto.getPrecioDescuento());
        productoExistente.setStock(dto.getStock());
        
        // 3. Guardamos los cambios
        return repository.save(productoExistente);
    }
}
