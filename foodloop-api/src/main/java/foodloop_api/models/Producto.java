package foodloop_api.models;

import jakarta.persistence.*;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
}
