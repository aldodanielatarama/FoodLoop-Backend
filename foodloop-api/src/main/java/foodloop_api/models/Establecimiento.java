package foodloop_api.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "establecimientos")
@Data
public class Establecimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombreComercial;

    @Column(nullable = false, length = 200)
    private String direccion;

    @Column(length = 20)
    private String telefono;

    @Column(length = 100)
    private String correo;

    @Column(length = 100)
    private String horarioAtencion;
}