package foodloop_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;







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
    private String ruc;
    private String distrito;
    private String horarioRecojo;
    private Boolean vende;
    private Boolean dona;
    private Integer minutosPreparacion;

    // Metodos escritos de forma explicita, sin Lombok.
    public String getNombreComercial() { return this.nombreComercial; }
    public void setNombreComercial(String value) { this.nombreComercial = value; }
    public String getDireccion() { return this.direccion; }
    public void setDireccion(String value) { this.direccion = value; }
    public Double getLatitud() { return this.latitud; }
    public void setLatitud(Double value) { this.latitud = value; }
    public Double getLongitud() { return this.longitud; }
    public void setLongitud(Double value) { this.longitud = value; }
    public String getTelefono() { return this.telefono; }
    public void setTelefono(String value) { this.telefono = value; }
    public String getCorreo() { return this.correo; }
    public void setCorreo(String value) { this.correo = value; }
    public String getHorarioAtencion() { return this.horarioAtencion; }
    public void setHorarioAtencion(String value) { this.horarioAtencion = value; }
    public String getNombreResponsable() { return this.nombreResponsable; }
    public void setNombreResponsable(String value) { this.nombreResponsable = value; }
    public String getTelefonoResponsable() { return this.telefonoResponsable; }
    public void setTelefonoResponsable(String value) { this.telefonoResponsable = value; }
    public String getRuc() { return this.ruc; }
    public void setRuc(String value) { this.ruc = value; }
    public String getDistrito() { return this.distrito; }
    public void setDistrito(String value) { this.distrito = value; }
    public String getHorarioRecojo() { return this.horarioRecojo; }
    public void setHorarioRecojo(String value) { this.horarioRecojo = value; }
    public Boolean getVende() { return this.vende; }
    public void setVende(Boolean value) { this.vende = value; }
    public Boolean getDona() { return this.dona; }
    public void setDona(Boolean value) { this.dona = value; }
    public Integer getMinutosPreparacion() { return this.minutosPreparacion; }
    public void setMinutosPreparacion(Integer value) { this.minutosPreparacion = value; }
}
