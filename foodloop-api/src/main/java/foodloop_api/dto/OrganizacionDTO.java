package foodloop_api.dto;

public class OrganizacionDTO {
 private String razonSocial;
 private String identificacion;
 private String direccion;
 private String distrito;
 private String contacto;
 private Integer capacidadRaciones;
 private Boolean transportePropio;
 private Boolean refrigeracion;
 private Double latitud;
 private Double longitud;

    // Metodos escritos de forma explicita, sin Lombok.
    public String getRazonSocial() { return this.razonSocial; }
    public void setRazonSocial(String value) { this.razonSocial = value; }
    public String getIdentificacion() { return this.identificacion; }
    public void setIdentificacion(String value) { this.identificacion = value; }
    public String getDireccion() { return this.direccion; }
    public void setDireccion(String value) { this.direccion = value; }
    public String getDistrito() { return this.distrito; }
    public void setDistrito(String value) { this.distrito = value; }
    public String getContacto() { return this.contacto; }
    public void setContacto(String value) { this.contacto = value; }
    public Integer getCapacidadRaciones() { return this.capacidadRaciones; }
    public void setCapacidadRaciones(Integer value) { this.capacidadRaciones = value; }
    public Boolean getTransportePropio() { return this.transportePropio; }
    public void setTransportePropio(Boolean value) { this.transportePropio = value; }
    public Boolean getRefrigeracion() { return this.refrigeracion; }
    public void setRefrigeracion(Boolean value) { this.refrigeracion = value; }
    public Double getLatitud() { return this.latitud; }
    public void setLatitud(Double value) { this.latitud = value; }
    public Double getLongitud() { return this.longitud; }
    public void setLongitud(Double value) { this.longitud = value; }
}
