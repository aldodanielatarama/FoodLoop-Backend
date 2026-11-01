package foodloop_api.dto;

public class SolicitudFoodDTO {
 private Long loteId;
 private Integer cantidad;
 private String comentario;

    // Metodos escritos de forma explicita, sin Lombok.
    public Long getLoteId() { return this.loteId; }
    public void setLoteId(Long value) { this.loteId = value; }
    public Integer getCantidad() { return this.cantidad; }
    public void setCantidad(Integer value) { this.cantidad = value; }
    public String getComentario() { return this.comentario; }
    public void setComentario(String value) { this.comentario = value; }
}
