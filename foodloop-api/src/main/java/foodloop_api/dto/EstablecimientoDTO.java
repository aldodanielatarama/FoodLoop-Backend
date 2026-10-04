package foodloop_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EstablecimientoDTO {

    @NotBlank(message = "El nombre comercial no puede estar vacío")
    private String nombreComercial;

    @NotBlank(message = "La dirección no puede estar vacía")
    private String direccion;

    private String telefono;

    @Email(message = "Debe proporcionar un correo electrónico válido")
    private String correo;

    private String horarioAtencion;
}