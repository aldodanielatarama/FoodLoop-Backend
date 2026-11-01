package foodloop_api.controllers;

import foodloop_api.dto.UsuarioDTO;
import foodloop_api.models.Usuario;
import foodloop_api.repositories.UsuarioRepository;
import foodloop_api.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UsuarioRepository usuarios;
    private final JwtUtil jwt;
    private final PasswordEncoder passwords;
    public AuthController(UsuarioRepository usuarios, JwtUtil jwt, PasswordEncoder passwords) {
        this.usuarios=usuarios; this.jwt=jwt; this.passwords=passwords;
    }
    @PostMapping("/register")
    public String register(@Valid @RequestBody UsuarioDTO dto) {
        if (usuarios.findByUsername(dto.getUsername()) != null) throw new ResponseStatusException(HttpStatus.CONFLICT,"Usuario existente");
        String rol = dto.getRol() == null ? "COMPRADOR" : dto.getRol().toUpperCase();
        if (!rol.equals("COMPRADOR") && !rol.equals("VENDEDOR") && !rol.equals("ORGANIZACION"))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Rol no permitido en registro público");
        if (dto.getPassword().length() < 8) throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Contraseña mínima: 8 caracteres");
        Usuario u = new Usuario(); u.setUsername(dto.getUsername()); u.setPassword(passwords.encode(dto.getPassword()));
        u.setRol(rol); u.setNombreCompleto(dto.getNombreCompleto()); u.setCorreo(dto.getCorreo()); u.setTelefono(dto.getTelefono());
        u.setVerificado(false); usuarios.save(u); return "Usuario registrado con éxito";
    }
    @PostMapping("/login")
    public String login(@Valid @RequestBody UsuarioDTO dto) { return autenticar(dto).get("token"); }
    @PostMapping("/session")
    public Map<String,String> autenticar(@Valid @RequestBody UsuarioDTO dto) {
        Usuario u = usuarios.findByUsername(dto.getUsername());
        if (u == null || !passwords.matches(dto.getPassword(),u.getPassword()) || "SUSPENDIDO".equals(u.getEstadoCuenta()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Credenciales incorrectas");
        return Map.of("token", jwt.generarToken(u.getUsername()), "username",u.getUsername(), "rol", u.getRol()==null?"COMPRADOR":u.getRol());
    }
    @GetMapping("/me")
    public Map<String,Object> me(Authentication auth) {
        Usuario u = usuarios.findByUsername(auth.getName());
        return Map.of("id",u.getId(),"username",u.getUsername(),"rol",u.getRol()==null?"COMPRADOR":u.getRol(),
                "verificado",Boolean.TRUE.equals(u.getVerificado()),"nombre",u.getNombreCompleto()==null?"":u.getNombreCompleto());
    }
}
