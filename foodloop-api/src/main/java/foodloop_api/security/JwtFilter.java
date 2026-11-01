package foodloop_api.security;

import foodloop_api.models.Usuario;
import foodloop_api.repositories.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwt;
    private final UsuarioRepository usuarios;
    public JwtFilter(JwtUtil jwt, UsuarioRepository usuarios) { this.jwt = jwt; this.usuarios = usuarios; }
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        String h = req.getHeader("Authorization");
        if (h != null && h.startsWith("Bearer ")) {
            try {
                String username = jwt.leerUsuario(h.substring(7));
                Usuario u = usuarios.findByUsername(username);
                if (u != null && !"SUSPENDIDO".equals(u.getEstadoCuenta())) {
                    String rol = u.getRol() == null ? "COMPRADOR" : u.getRol();
                    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                            username, null, List.of(new SimpleGrantedAuthority("ROLE_" + rol))));
                }
            } catch (Exception ex) { SecurityContextHolder.clearContext(); }
        }
        chain.doFilter(req, res);
    }
}
