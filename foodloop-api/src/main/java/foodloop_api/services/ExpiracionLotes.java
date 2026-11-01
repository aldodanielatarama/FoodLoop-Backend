package foodloop_api.services;
import foodloop_api.repositories.LoteExcedenteRepository;
import foodloop_api.repositories.NotificacionRepository;
import foodloop_api.models.*;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
@Component 
public class ExpiracionLotes {
 private final LoteExcedenteRepository lotes;
 private final NotificacionRepository avisos;
 @Scheduled(fixedDelay=60000)
 @Transactional
 public void revisar(){
    LocalDateTime ahora=LocalDateTime.now(java.time.ZoneId.of("America/Lima"));
    for(LoteExcedente l:lotes.findAll()){
        if("ACTIVO".equals(l.getEstado()) &&
            (!ahora.isBefore(l.getLimiteReserva()) || !ahora.isBefore(l.getLimiteRecojo()) || !ahora.isBefore(l.getFechaVencimiento()))) {
            l.setEstado("VENCIDO"); Notificacion n=new Notificacion();n.setUsuarioId(l.getPropietarioId());
            n.setTitulo("Publicación cerrada por horario");n.setMensaje("El lote #"+l.getId()+" ya no acepta solicitudes");avisos.save(n);
        }
    }
 }

    // Metodos escritos de forma explicita, sin Lombok.
    public ExpiracionLotes(LoteExcedenteRepository lotes, NotificacionRepository avisos) {
        this.lotes = lotes;
        this.avisos = avisos;
    }

}
