package foodloop_api.models;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Table(name="notificaciones") 
public class Notificacion {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 private Long usuarioId;
 private String titulo;
 @Column(length=1000) private String mensaje;
 private boolean leida=false;
 private LocalDateTime fecha=LocalDateTime.now();

    // Metodos escritos de forma explicita, sin Lombok.
    public Long getId() { return this.id; }
    public void setId(Long value) { this.id = value; }
    public Long getUsuarioId() { return this.usuarioId; }
    public void setUsuarioId(Long value) { this.usuarioId = value; }
    public String getTitulo() { return this.titulo; }
    public void setTitulo(String value) { this.titulo = value; }
    public String getMensaje() { return this.mensaje; }
    public void setMensaje(String value) { this.mensaje = value; }
    public boolean isLeida() { return this.leida; }
    public void setLeida(boolean value) { this.leida = value; }
    public LocalDateTime getFecha() { return this.fecha; }
    public void setFecha(LocalDateTime value) { this.fecha = value; }
}
