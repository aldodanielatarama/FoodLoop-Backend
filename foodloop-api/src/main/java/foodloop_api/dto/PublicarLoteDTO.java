package foodloop_api.dto;

import java.time.LocalDateTime;

public class PublicarLoteDTO {
 private Long establecimientoId;
 private String nombre;
 private String categoria;
 private String descripcion;
 private String ingredientes;
 private String alergenos;
 private String imagenUrl;
 private String modalidad;
 private Integer cantidad;
 private Double precioOriginal;
 private Double precioLiquidacion;
 private Double pesoPorUnidadKg;
 private LocalDateTime fechaPreparacion;
 private LocalDateTime fechaVencimiento;
 private LocalDateTime limiteReserva;
 private LocalDateTime limiteRecojo;
 private String conservacion;
 private String politicaAsignacion;

    // Metodos escritos de forma explicita, sin Lombok.
    public Long getEstablecimientoId() { return this.establecimientoId; }
    public void setEstablecimientoId(Long value) { this.establecimientoId = value; }
    public String getNombre() { return this.nombre; }
    public void setNombre(String value) { this.nombre = value; }
    public String getCategoria() { return this.categoria; }
    public void setCategoria(String value) { this.categoria = value; }
    public String getDescripcion() { return this.descripcion; }
    public void setDescripcion(String value) { this.descripcion = value; }
    public String getIngredientes() { return this.ingredientes; }
    public void setIngredientes(String value) { this.ingredientes = value; }
    public String getAlergenos() { return this.alergenos; }
    public void setAlergenos(String value) { this.alergenos = value; }
    public String getImagenUrl() { return this.imagenUrl; }
    public void setImagenUrl(String value) { this.imagenUrl = value; }
    public String getModalidad() { return this.modalidad; }
    public void setModalidad(String value) { this.modalidad = value; }
    public Integer getCantidad() { return this.cantidad; }
    public void setCantidad(Integer value) { this.cantidad = value; }
    public Double getPrecioOriginal() { return this.precioOriginal; }
    public void setPrecioOriginal(Double value) { this.precioOriginal = value; }
    public Double getPrecioLiquidacion() { return this.precioLiquidacion; }
    public void setPrecioLiquidacion(Double value) { this.precioLiquidacion = value; }
    public Double getPesoPorUnidadKg() { return this.pesoPorUnidadKg; }
    public void setPesoPorUnidadKg(Double value) { this.pesoPorUnidadKg = value; }
    public LocalDateTime getFechaPreparacion() { return this.fechaPreparacion; }
    public void setFechaPreparacion(LocalDateTime value) { this.fechaPreparacion = value; }
    public LocalDateTime getFechaVencimiento() { return this.fechaVencimiento; }
    public void setFechaVencimiento(LocalDateTime value) { this.fechaVencimiento = value; }
    public LocalDateTime getLimiteReserva() { return this.limiteReserva; }
    public void setLimiteReserva(LocalDateTime value) { this.limiteReserva = value; }
    public LocalDateTime getLimiteRecojo() { return this.limiteRecojo; }
    public void setLimiteRecojo(LocalDateTime value) { this.limiteRecojo = value; }
    public String getConservacion() { return this.conservacion; }
    public void setConservacion(String value) { this.conservacion = value; }
    public String getPoliticaAsignacion() { return this.politicaAsignacion; }
    public void setPoliticaAsignacion(String value) { this.politicaAsignacion = value; }
}
