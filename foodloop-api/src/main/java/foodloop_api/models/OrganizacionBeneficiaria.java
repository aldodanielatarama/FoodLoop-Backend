package foodloop_api.models;
import jakarta.persistence.*;
@Entity @Table(name="organizaciones_beneficiarias") 
public class OrganizacionBeneficiaria {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,unique=true) private Long usuarioId;
 private String razonSocial;
 private String identificacion;
 private String direccion;
 private String distrito;
 private Double latitud; private Double longitud;
 private String contacto;
 private Integer capacidadRaciones;
 private Boolean transportePropio=false;
 private Boolean refrigeracion=false;
 private Boolean verificada=false;

    // Metodos escritos de forma explicita, sin Lombok.
    public Long getId() { return this.id; }
    public void setId(Long value) { this.id = value; }
    public Long getUsuarioId() { return this.usuarioId; }
    public void setUsuarioId(Long value) { this.usuarioId = value; }
    public String getRazonSocial() { return this.razonSocial; }
    public void setRazonSocial(String value) { this.razonSocial = value; }
    public String getIdentificacion() { return this.identificacion; }
    public void setIdentificacion(String value) { this.identificacion = value; }
    public String getDireccion() { return this.direccion; }
    public void setDireccion(String value) { this.direccion = value; }
    public String getDistrito() { return this.distrito; }
    public void setDistrito(String value) { this.distrito = value; }
    public Double getLatitud() { return this.latitud; }
    public void setLatitud(Double value) { this.latitud = value; }
    public Double getLongitud() { return this.longitud; }
    public void setLongitud(Double value) { this.longitud = value; }
    public String getContacto() { return this.contacto; }
    public void setContacto(String value) { this.contacto = value; }
    public Integer getCapacidadRaciones() { return this.capacidadRaciones; }
    public void setCapacidadRaciones(Integer value) { this.capacidadRaciones = value; }
    public Boolean getTransportePropio() { return this.transportePropio; }
    public void setTransportePropio(Boolean value) { this.transportePropio = value; }
    public Boolean getRefrigeracion() { return this.refrigeracion; }
    public void setRefrigeracion(Boolean value) { this.refrigeracion = value; }
    public Boolean getVerificada() { return this.verificada; }
    public void setVerificada(Boolean value) { this.verificada = value; }
}
