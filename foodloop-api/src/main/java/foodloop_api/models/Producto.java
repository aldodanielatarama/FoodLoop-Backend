package foodloop_api.models;

import jakarta.persistence.*;

import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "productos")




@Schema(description = "Entidad que representa un excedente alimentario en la tienda")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del producto", example = "1")
    private Long id;

    @Column(nullable = false, length = 100)
    @Schema(description = "Nombre del excedente", example = "Menú Ejecutivo (Sobrante)")
    private String nombre;

    @Column(nullable = false)
    @Schema(description = "Categoría del alimento", example = "Comida Preparada")
    private String categoria;

    @Column(name = "precio_descuento", nullable = false)
    @Schema(description = "Precio de liquidación", example = "8.50")
    private Double precioDescuento;

    @Column(nullable = false)
    @Schema(description = "Cantidad de raciones disponibles", example = "5")
    private Integer stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "establecimiento_id")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Establecimiento establecimiento;

    private String descripcion;
    private String ingredientes;
    private String alergenos;
    private Double precioOriginal;
    private Double pesoPorUnidadKg;
    private Boolean requiereRefrigeracion;
    private String imagenUrl;

    // Metodos escritos de forma explicita, sin Lombok.
    public Long getId() { return this.id; }
    public void setId(Long value) { this.id = value; }
    public String getNombre() { return this.nombre; }
    public void setNombre(String value) { this.nombre = value; }
    public String getCategoria() { return this.categoria; }
    public void setCategoria(String value) { this.categoria = value; }
    public Double getPrecioDescuento() { return this.precioDescuento; }
    public void setPrecioDescuento(Double value) { this.precioDescuento = value; }
    public Integer getStock() { return this.stock; }
    public void setStock(Integer value) { this.stock = value; }
    public Establecimiento getEstablecimiento() { return this.establecimiento; }
    public void setEstablecimiento(Establecimiento value) { this.establecimiento = value; }
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
