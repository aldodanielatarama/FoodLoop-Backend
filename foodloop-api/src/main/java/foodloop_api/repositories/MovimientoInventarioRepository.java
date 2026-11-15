package foodloop_api.repositories;
import foodloop_api.models.MovimientoInventario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario,Long> {
List<MovimientoInventario> findByLoteIdOrderByIdDesc(Long id);
}
