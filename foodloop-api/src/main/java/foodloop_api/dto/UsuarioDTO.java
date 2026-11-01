package foodloop_api.dto;

import jakarta.validation.constraints.NotBlank;





public class UsuarioDTO {
    
    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
    private String rol;
    private String nombreCompleto;
    private String correo;
    private String telefono;

    // Metodos escritos de forma explicita, sin Lombok.
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
}