package foodloop_api.models;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name="incidencias") 
public class Incidencia {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 private Long usuarioId;
 private Long solicitudId;
 private String tipo;
 @Column(length=2000) private String descripcion;
 private String estado="ABIERTA";
 private String resolucion;
 private LocalDateTime fecha=LocalDateTime.now();

    // Metodos escritos de forma explicita, sin Lombok.
    public Long getId() { return this.id; }
    public void setId(Long value) { this.id = value; }
    public Long getUsuarioId() { return this.usuarioId; }
    public void setUsuarioId(Long value) { this.usuarioId = value; }
    public Long getSolicitudId() { return this.solicitudId; }
    public void setSolicitudId(Long value) { this.solicitudId = value; }
    public String getTipo() { return this.tipo; }
    public void setTipo(String value) { this.tipo = value; }
    public String getDescripcion() { return this.descripcion; }
    public void setDescripcion(String value) { this.descripcion = value; }
    public String getEstado() { return this.estado; }
    public void setEstado(String value) { this.estado = value; }
    public String getResolucion() { return this.resolucion; }
    public void setResolucion(String value) { this.resolucion = value; }
    public LocalDateTime getFecha() { return this.fecha; }
    public void setFecha(LocalDateTime value) { this.fecha = value; }
}
