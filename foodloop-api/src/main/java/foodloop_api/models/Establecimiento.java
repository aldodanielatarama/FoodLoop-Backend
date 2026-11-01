package foodloop_api.models;

import jakarta.persistence.*;




@Entity
@Table(name = "establecimientos")



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

    private Long usuarioResponsableId;
    private String ruc;
    private String distrito;
    private String horarioRecojo;
    private Boolean vende = true;
    private Boolean dona = true;
    private Boolean verificado = false;
    private Integer minutosPreparacion = 10;

    // Metodos escritos de forma explicita, sin Lombok.
    public Long getId() { return this.id; }
    public void setId(Long value) { this.id = value; }
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
    public Long getUsuarioResponsableId() { return this.usuarioResponsableId; }
    public void setUsuarioResponsableId(Long value) { this.usuarioResponsableId = value; }
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
    public Boolean getVerificado() { return this.verificado; }
    public void setVerificado(Boolean value) { this.verificado = value; }
    public Integer getMinutosPreparacion() { return this.minutosPreparacion; }
    public void setMinutosPreparacion(Integer value) { this.minutosPreparacion = value; }
}
