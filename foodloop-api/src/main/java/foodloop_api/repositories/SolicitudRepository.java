package foodloop_api.repositories;
import foodloop_api.models.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List; import java.util.Optional;
public interface SolicitudRepository extends JpaRepository<Solicitud,Long> {
List<Solicitud> findBySolicitanteIdOrderByIdDesc(Long id);
List<Solicitud> findByPropietarioIdOrderByIdDesc(Long id);
List<Solicitud> findByLoteId(Long loteId);
}
