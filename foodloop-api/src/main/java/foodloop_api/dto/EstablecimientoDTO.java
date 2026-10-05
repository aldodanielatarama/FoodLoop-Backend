package foodloop_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstablecimientoDTO {

    @NotBlank(message = "El nombre comercial es obligatorio")
    private String nombreComercial;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    private Double latitud;
    private Double longitud;

    private String telefono;

    @Email(message = "El correo debe tener un formato válido")
    private String correo;

    private String horarioAtencion;

    private String nombreResponsable;
    private String telefonoResponsable;
