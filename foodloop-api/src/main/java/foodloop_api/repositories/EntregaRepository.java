package foodloop_api.repositories;
import foodloop_api.models.Entrega;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List; import java.util.Optional;
public interface EntregaRepository extends JpaRepository<Entrega,Long> {
Optional<Entrega> findBySolicitudId(Long id);
}
