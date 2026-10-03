package foodloop_api.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    // Usamos una clave más larga y segura generada por Keys.hmacShaKeyFor
    private final String SECRET = "FoodLoopSecretoJWT12345678901234567890";
    private final Key SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());
    private final long EXPIRATION_TIME = 86400000;

    public String generarToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                // Se usa el nuevo método recomendado
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }
}
