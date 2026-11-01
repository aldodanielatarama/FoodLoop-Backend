package foodloop_api.repositories;
import foodloop_api.models.OrganizacionBeneficiaria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List; import java.util.Optional;
public interface OrganizacionBeneficiariaRepository extends JpaRepository<OrganizacionBeneficiaria,Long> {
Optional<OrganizacionBeneficiaria> findByUsuarioId(Long id);
}
