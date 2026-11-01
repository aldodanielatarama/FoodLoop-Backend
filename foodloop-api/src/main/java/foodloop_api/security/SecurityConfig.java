package foodloop_api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtFilter filtro;
    public SecurityConfig(JwtFilter filtro) { this.filtro = filtro; }

    @Bean public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean public SecurityFilterChain filter(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/index.html", "/portal.html", "/css/**", "/js/**", "/assets/**", "/favicon.ico", "/error",
                        "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/auth/register", "/auth/login", "/auth/session").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/productos/**", "/api/v1/establecimientos/**", "/api/foodloop/lotes/**").permitAll()
                .anyRequest().authenticated())
            .addFilterBefore(filtro, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
