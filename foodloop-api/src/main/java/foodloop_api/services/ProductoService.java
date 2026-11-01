package foodloop_api.services;

import foodloop_api.dto.ProductoDTO;
import foodloop_api.exceptions.ProductoNoEncontradoException;
import foodloop_api.models.Producto;
import foodloop_api.repositories.ProductoRepository;
import foodloop_api.repositories.EstablecimientoRepository;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class ProductoService {

    private final ProductoRepository repository;
    private final EstablecimientoRepository establecimientos;

    public ProductoService(ProductoRepository repository, EstablecimientoRepository establecimientos) {
        this.repository = repository;
        this.establecimientos = establecimientos;
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
        actualizarExtras(p, dto);
        return repository.save(p);
    }

    // Actualiza un producto existente con los datos DTO
    public Producto actualizar(Long id, ProductoDTO dto) {
        Producto productoExistente = buscarPorId(id);

        productoExistente.setNombre(dto.getNombre());
        productoExistente.setCategoria(dto.getCategoria());
        productoExistente.setPrecioDescuento(dto.getPrecioDescuento());
        productoExistente.setStock(dto.getStock());
        actualizarExtras(productoExistente, dto);

        return repository.save(productoExistente);
    }

    private void actualizarExtras(Producto p, ProductoDTO d) {
        if (d.getEstablecimientoId() != null) p.setEstablecimiento(establecimientos.findById(d.getEstablecimientoId()).orElseThrow(() -> new IllegalArgumentException("Establecimiento inexistente")));
        p.setDescripcion(d.getDescripcion()); p.setIngredientes(d.getIngredientes());
        p.setAlergenos(d.getAlergenos()); p.setPrecioOriginal(d.getPrecioOriginal());
        p.setPesoPorUnidadKg(d.getPesoPorUnidadKg());
        p.setRequiereRefrigeracion(d.getRequiereRefrigeracion()); p.setImagenUrl(d.getImagenUrl());
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}