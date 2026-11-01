package foodloop_api.repositories;
import foodloop_api.models.Incidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List; import java.util.Optional;
public interface IncidenciaRepository extends JpaRepository<Incidencia,Long> {
List<Incidencia> findByUsuarioIdOrderByIdDesc(Long id);
}
