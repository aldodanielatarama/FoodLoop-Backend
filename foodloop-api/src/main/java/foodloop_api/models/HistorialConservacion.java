package foodloop_api.models;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name="historial_conservacion") 
public class HistorialConservacion {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 private Long loteId;
 private Long responsableId;
 private Double temperaturaCelsius;
 private String observaciones;
 private LocalDateTime fecha=LocalDateTime.now();

    // Metodos escritos de forma explicita, sin Lombok.
    public Long getId() { return this.id; }
    public void setId(Long value) { this.id = value; }
    public Long getLoteId() { return this.loteId; }
    public void setLoteId(Long value) { this.loteId = value; }
    public Long getResponsableId() { return this.responsableId; }
    public void setResponsableId(Long value) { this.responsableId = value; }
    public Double getTemperaturaCelsius() { return this.temperaturaCelsius; }
    public void setTemperaturaCelsius(Double value) { this.temperaturaCelsius = value; }
    public String getObservaciones() { return this.observaciones; }
    public void setObservaciones(String value) { this.observaciones = value; }
    public LocalDateTime getFecha() { return this.fecha; }
    public void setFecha(LocalDateTime value) { this.fecha = value; }
}
