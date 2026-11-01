package foodloop_api.models;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name="movimientos_inventario") 
public class MovimientoInventario {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 private Long loteId;
 private Long usuarioId;
 private Integer unidades;
 private String motivo;
 private LocalDateTime fecha=LocalDateTime.now();

    // Metodos escritos de forma explicita, sin Lombok.
    public Long getId() { return this.id; }
    public void setId(Long value) { this.id = value; }
    public Long getLoteId() { return this.loteId; }
    public void setLoteId(Long value) { this.loteId = value; }
    public Long getUsuarioId() { return this.usuarioId; }
    public void setUsuarioId(Long value) { this.usuarioId = value; }
    public Integer getUnidades() { return this.unidades; }
    public void setUnidades(Integer value) { this.unidades = value; }
    public String getMotivo() { return this.motivo; }
    public void setMotivo(String value) { this.motivo = value; }
    public LocalDateTime getFecha() { return this.fecha; }
    public void setFecha(LocalDateTime value) { this.fecha = value; }
}
