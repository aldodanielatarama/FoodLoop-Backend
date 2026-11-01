package foodloop_api.dto;

public class IncidenciaDTO {
 private Long solicitudId;
 private String tipo;
 private String descripcion;

    // Metodos escritos de forma explicita, sin Lombok.
    public Long getSolicitudId() { return this.solicitudId; }
    public void setSolicitudId(Long value) { this.solicitudId = value; }
    public String getTipo() { return this.tipo; }
    public void setTipo(String value) { this.tipo = value; }
    public String getDescripcion() { return this.descripcion; }
    public void setDescripcion(String value) { this.descripcion = value; }
}
