package foodloop_api.exceptions;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.access.AccessDeniedException;
import java.util.Map;
@RestControllerAdvice
public class GlobalExceptionHandler {
 @ExceptionHandler(ProductoNoEncontradoException.class)
 public ResponseEntity<Map<String,String>> noProducto(ProductoNoEncontradoException e) {return ResponseEntity.status(404).body(Map.of("error",e.getMessage()));}
 @ExceptionHandler(ResponseStatusException.class)
 public ResponseEntity<Map<String,String>> status(ResponseStatusException e) {return ResponseEntity.status(e.getStatusCode()).body(Map.of("error",e.getReason()==null?"Operación rechazada":e.getReason()));}
 @ExceptionHandler(AccessDeniedException.class)
 public ResponseEntity<Map<String,String>> acceso(AccessDeniedException e) {return ResponseEntity.status(403).body(Map.of("error","No autorizado para esta operación"));}
 @ExceptionHandler(IllegalArgumentException.class)
 public ResponseEntity<Map<String,String>> argumento(IllegalArgumentException e) {return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));}
 @ExceptionHandler(Exception.class)
 public ResponseEntity<Map<String,String>> inesperado(Exception e) {return ResponseEntity.status(500).body(Map.of("error","Error interno. Revisa los registros del servidor."));}
}
