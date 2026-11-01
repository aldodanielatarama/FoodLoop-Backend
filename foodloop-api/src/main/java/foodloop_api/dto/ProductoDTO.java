package foodloop_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;





public class ProductoDTO {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "La categoría es obligatoria")
    private String categoria;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor a 0")
    private Double precioDescuento;

    @NotNull(message = "El stock es obligatorio")
    @Positive(message = "El stock debe ser al menos 1")
    private Integer stock;
    private Long establecimientoId;
    private String descripcion;
    private String ingredientes;
    private String alergenos;
    private Double precioOriginal;
    private Double pesoPorUnidadKg;
    private Boolean requiereRefrigeracion;
    private String imagenUrl;

    // Metodos escritos de forma explicita, sin Lombok.
    public String getNombre() { return this.nombre; }
    public void setNombre(String value) { this.nombre = value; }
    public String getCategoria() { return this.categoria; }
    public void setCategoria(String value) { this.categoria = value; }
    public Double getPrecioDescuento() { return this.precioDescuento; }
    public void setPrecioDescuento(Double value) { this.precioDescuento = value; }
    public Integer getStock() { return this.stock; }
    public void setStock(Integer value) { this.stock = value; }
    public Long getEstablecimientoId() { return this.establecimientoId; }
    public void setEstablecimientoId(Long value) { this.establecimientoId = value; }
    public String getDescripcion() { return this.descripcion; }
    public void setDescripcion(String value) { this.descripcion = value; }
    public String getIngredientes() { return this.ingredientes; }
    public void setIngredientes(String value) { this.ingredientes = value; }
    public String getAlergenos() { return this.alergenos; }
    public void setAlergenos(String value) { this.alergenos = value; }
    public Double getPrecioOriginal() { return this.precioOriginal; }
    public void setPrecioOriginal(Double value) { this.precioOriginal = value; }
    public Double getPesoPorUnidadKg() { return this.pesoPorUnidadKg; }
    public void setPesoPorUnidadKg(Double value) { this.pesoPorUnidadKg = value; }
    public Boolean getRequiereRefrigeracion() { return this.requiereRefrigeracion; }
    public void setRequiereRefrigeracion(Boolean value) { this.requiereRefrigeracion = value; }
    public String getImagenUrl() { return this.imagenUrl; }
    public void setImagenUrl(String value) { this.imagenUrl = value; }
}
