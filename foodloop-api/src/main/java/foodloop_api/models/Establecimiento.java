package foodloop_api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "establecimientos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Establecimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombreComercial;

    @Column(nullable = false)
    private String direccion;

   
    private Double latitud;
    private Double longitud;

    private String telefono;
    private String correo;
    private String horarioAtencion;

    private String nombreResponsable;
    private String telefonoResponsable;
}
