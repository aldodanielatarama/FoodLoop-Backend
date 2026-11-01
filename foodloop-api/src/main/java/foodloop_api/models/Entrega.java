package foodloop_api.models;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name="entregas") 
public class Entrega {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(unique=true,nullable=false) private Long solicitudId;
 private String modalidad="RECOJO";
 private String estado="PENDIENTE";
 private String origen;
 private String destino;
 private LocalDateTime horaEstimada;
 private LocalDateTime listaEn;
 private LocalDateTime entregadaEn;
 private String observaciones;

    // Metodos escritos de forma explicita, sin Lombok.
    public Long getId() { return this.id; }
    public void setId(Long value) { this.id = value; }
    public Long getSolicitudId() { return this.solicitudId; }
    public void setSolicitudId(Long value) { this.solicitudId = value; }
    public String getModalidad() { return this.modalidad; }
    public void setModalidad(String value) { this.modalidad = value; }
    public String getEstado() { return this.estado; }
    public void setEstado(String value) { this.estado = value; }
    public String getOrigen() { return this.origen; }
    public void setOrigen(String value) { this.origen = value; }
    public String getDestino() { return this.destino; }
    public void setDestino(String value) { this.destino = value; }
    public LocalDateTime getHoraEstimada() { return this.horaEstimada; }
    public void setHoraEstimada(LocalDateTime value) { this.horaEstimada = value; }
    public LocalDateTime getListaEn() { return this.listaEn; }
    public void setListaEn(LocalDateTime value) { this.listaEn = value; }
    public LocalDateTime getEntregadaEn() { return this.entregadaEn; }
    public void setEntregadaEn(LocalDateTime value) { this.entregadaEn = value; }
    public String getObservaciones() { return this.observaciones; }
    public void setObservaciones(String value) { this.observaciones = value; }
}
