package foodloop_api.models;

import jakarta.persistence.*;


@Entity
@Table(name = "usuarios")




public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    // COMPRADOR / VENDEDOR / ORGANIZACION / ADMIN
    @Column(length = 25)
    private String rol = "COMPRADOR";
    private String nombreCompleto;
    private String correo;
    private String telefono;
    private String estadoCuenta = "ACTIVO";
    private Boolean verificado = false;

    @PrePersist
    void defaults() {
        if (rol == null) rol = "COMPRADOR";
        if (estadoCuenta == null) estadoCuenta = "ACTIVO";
        if (verificado == null) verificado = false;
    }

    // Metodos escritos de forma explicita, sin Lombok.
    public Long getId() { return this.id; }
    public void setId(Long value) { this.id = value; }
    public String getUsername() { return this.username; }
    public void setUsername(String value) { this.username = value; }
    public String getPassword() { return this.password; }
    public void setPassword(String value) { this.password = value; }
    public String getRol() { return this.rol; }
    public void setRol(String value) { this.rol = value; }
    public String getNombreCompleto() { return this.nombreCompleto; }
    public void setNombreCompleto(String value) { this.nombreCompleto = value; }
    public String getCorreo() { return this.correo; }
    public void setCorreo(String value) { this.correo = value; }
    public String getTelefono() { return this.telefono; }
    public void setTelefono(String value) { this.telefono = value; }
    public String getEstadoCuenta() { return this.estadoCuenta; }
    public void setEstadoCuenta(String value) { this.estadoCuenta = value; }
    public Boolean getVerificado() { return this.verificado; }
    public void setVerificado(Boolean value) { this.verificado = value; }
}