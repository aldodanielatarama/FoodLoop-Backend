package foodloop_api.repositories;
import foodloop_api.models.HistorialConservacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface HistorialConservacionRepository extends JpaRepository<HistorialConservacion,Long> {
List<HistorialConservacion> findByLoteIdOrderByFechaDesc(Long id);
}
