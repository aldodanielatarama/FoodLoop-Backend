package foodloop_api.models;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name="solicitudes") 
public class Solicitud {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false) private Long loteId;
 @Column(nullable=false) private Long solicitanteId;
 @Column(nullable=false) private Long propietarioId;
 private String tipo; // VENTA DONACION
 private Integer cantidadSolicitada;
 private Integer cantidadAsignada=0;
 private String estado; // PENDIENTE, PARCIAL_OFRECIDA, ASIGNADA, LISTA, ENTREGADA, CANCELADA, RECHAZADA
 private String codigoRetiro;
 private LocalDateTime creadaEn=LocalDateTime.now();
 private LocalDateTime confirmadaEn;
 private LocalDateTime entregadaEn;
 private String comentario;

    // Metodos escritos de forma explicita, sin Lombok.
    public Long getId() { return this.id; }
    public void setId(Long value) { this.id = value; }
    public Long getLoteId() { return this.loteId; }
    public void setLoteId(Long value) { this.loteId = value; }
    public Long getSolicitanteId() { return this.solicitanteId; }
    public void setSolicitanteId(Long value) { this.solicitanteId = value; }
    public Long getPropietarioId() { return this.propietarioId; }
    public void setPropietarioId(Long value) { this.propietarioId = value; }
    public String getTipo() { return this.tipo; }
    public void setTipo(String value) { this.tipo = value; }
    public Integer getCantidadSolicitada() { return this.cantidadSolicitada; }
    public void setCantidadSolicitada(Integer value) { this.cantidadSolicitada = value; }
    public Integer getCantidadAsignada() { return this.cantidadAsignada; }
    public void setCantidadAsignada(Integer value) { this.cantidadAsignada = value; }
    public String getEstado() { return this.estado; }
    public void setEstado(String value) { this.estado = value; }
    public String getCodigoRetiro() { return this.codigoRetiro; }
    public void setCodigoRetiro(String value) { this.codigoRetiro = value; }
    public LocalDateTime getCreadaEn() { return this.creadaEn; }
    public void setCreadaEn(LocalDateTime value) { this.creadaEn = value; }
    public LocalDateTime getConfirmadaEn() { return this.confirmadaEn; }
    public void setConfirmadaEn(LocalDateTime value) { this.confirmadaEn = value; }
    public LocalDateTime getEntregadaEn() { return this.entregadaEn; }
    public void setEntregadaEn(LocalDateTime value) { this.entregadaEn = value; }
    public String getComentario() { return this.comentario; }
    public void setComentario(String value) { this.comentario = value; }
}
