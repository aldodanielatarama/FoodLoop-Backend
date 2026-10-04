package foodloop_api.exceptions;

// Excepcion  para cuando se busca un producto por ID y no existe en la bd
public class ProductoNoEncontradoException extends RuntimeException {

    public ProductoNoEncontradoException(Long id) {
        super("Producto no encontrado con el ID " + id);
    }
}