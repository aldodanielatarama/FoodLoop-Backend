package foodloop_api.repositories;

import foodloop_api.models.Establecimiento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstablecimientoRepository extends JpaRepository<Establecimiento, Long> {
}