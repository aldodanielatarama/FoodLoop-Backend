package foodloop_api.services;

import foodloop_api.dto.EstablecimientoDTO;
import foodloop_api.models.Establecimiento;
import foodloop_api.repositories.EstablecimientoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstablecimientoService {

    private final EstablecimientoRepository repository;

    public EstablecimientoService(EstablecimientoRepository repository) {
        this.repository = repository;
    }

    public List<Establecimiento> listar() {
        return repository.findAll();
    }

    public Establecimiento buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Establecimiento no encontrado con id: " + id));
    }

    public Establecimiento guardar(EstablecimientoDTO dto) {
        Establecimiento est = new Establecimiento();
        mapearDtoAEntidad(est, dto);
        return repository.save(est);
    }

    public Establecimiento actualizar(Long id, EstablecimientoDTO dto) {
        Establecimiento est = buscarPorId(id);
        mapearDtoAEntidad(est, dto);
        return repository.save(est);
    }

    public void eliminar(Long id) {
        Establecimiento est = buscarPorId(id);
        repository.delete(est);
    }

    private void mapearDtoAEntidad(Establecimiento est, EstablecimientoDTO dto) {
        est.setNombreComercial(dto.getNombreComercial());
        est.setDireccion(dto.getDireccion());
        est.setLatitud(dto.getLatitud());
        est.setLongitud(dto.getLongitud());
        est.setTelefono(dto.getTelefono());
        est.setCorreo(dto.getCorreo());
        est.setHorarioAtencion(dto.getHorarioAtencion());
        est.setNombreResponsable(dto.getNombreResponsable());
        est.setTelefonoResponsable(dto.getTelefonoResponsable());
    }
}
