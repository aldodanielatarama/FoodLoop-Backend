package foodloop_api.controllers;

import foodloop_api.dto.UsuarioDTO;
import foodloop_api.models.Usuario;
import foodloop_api.repositories.UsuarioRepository;
import foodloop_api.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "Endpoints para registro y login de usuarios")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;

    public AuthController(UsuarioRepository usuarioRepository, JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar nuevo usuario", description = "Crea un usuario con la contraseña encriptada en la base de datos")
    public String register(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        // Verificar si el usuario ya existe para evitar errores SQL
        if(usuarioRepository.findByUsername(usuarioDTO.getUsername()) != null) {
            return "El usuario ya existe";
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(usuarioDTO.getUsername());
        // Encriptar contraseña según el manual S4-TA2
        usuario.setPassword(new BCryptPasswordEncoder().encode(usuarioDTO.getPassword()));
        
        usuarioRepository.save(usuario);
        return "Usuario registrado con éxito";
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Valida credenciales y devuelve un Token JWT")
    public String login(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        Usuario user = usuarioRepository.findByUsername(usuarioDTO.getUsername());
        
        if (user != null && new BCryptPasswordEncoder().matches(usuarioDTO.getPassword(), user.getPassword())) {
            // Genera y devuelve el Token si las credenciales coinciden
            return jwtUtil.generarToken(user.getUsername());
        }
        return "Credenciales incorrectas";
    }
}