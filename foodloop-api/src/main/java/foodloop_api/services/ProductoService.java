package foodloop_api.services;

import foodloop_api.dto.ProductoDTO;
import foodloop_api.exceptions.ProductoNoEncontradoException;
import foodloop_api.models.Producto;
import foodloop_api.repositories.ProductoRepository;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class ProductoService {

    private final ProductoRepository repository;

    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    public List<Producto> listar() {
        return repository.findAll();
    }

    // Busca  producto por  id
    public Producto buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProductoNoEncontradoException(id));
    }

    // Crea  nuevo producto a partir de los datos DTO
    public Producto insertar(ProductoDTO dto) {
        Producto p = new Producto();
        p.setNombre(dto.getNombre());
        p.setCategoria(dto.getCategoria());
        p.setPrecioDescuento(dto.getPrecioDescuento());
        p.setStock(dto.getStock());
        return repository.save(p);
    }

    // Actualiza un producto existente con los datos DTO
    public Producto actualizar(Long id, ProductoDTO dto) {
        Producto productoExistente = buscarPorId(id);

        productoExistente.setNombre(dto.getNombre());
        productoExistente.setCategoria(dto.getCategoria());
        productoExistente.setPrecioDescuento(dto.getPrecioDescuento());
        productoExistente.setStock(dto.getStock());

        return repository.save(productoExistente);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}