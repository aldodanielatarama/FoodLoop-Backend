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

    public Establecimiento guardar(EstablecimientoDTO dto) {
        Establecimiento est = new Establecimiento();
        est.setNombreComercial(dto.getNombreComercial());
        est.setDireccion(dto.getDireccion());
        est.setTelefono(dto.getTelefono());
        est.setCorreo(dto.getCorreo());
        est.setHorarioAtencion(dto.getHorarioAtencion());
        return repository.save(est);
    }

    public Establecimiento buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Establecimiento no encontrado con ID: " + id));
    }

    public Establecimiento actualizar(Long id, EstablecimientoDTO dto) {
        Establecimiento est = buscarPorId(id);
        est.setNombreComercial(dto.getNombreComercial());
        est.setDireccion(dto.getDireccion());
        est.setTelefono(dto.getTelefono());
        est.setCorreo(dto.getCorreo());
        est.setHorarioAtencion(dto.getHorarioAtencion());
        return repository.save(est);
    }

    public void eliminar(Long id) {
        Establecimiento est = buscarPorId(id);
        repository.delete(est);
    }
}
