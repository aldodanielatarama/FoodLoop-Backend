package foodloop_api.repositories;
import foodloop_api.models.LoteExcedente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List; import java.util.Optional;
public interface LoteExcedenteRepository extends JpaRepository<LoteExcedente,Long> {
 @Lock(LockModeType.PESSIMISTIC_WRITE)
 @Query("select l from LoteExcedente l where l.id = :id")
 Optional<LoteExcedente> bloquear(@Param("id") Long id);
 List<LoteExcedente> findByPropietarioIdOrderByIdDesc(Long id);
 List<LoteExcedente> findAllByOrderByIdDesc();
}
