package foodloop_api.security;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtUtilTest {

    private static final String CLAVE_VALIDA = "clave-de-prueba-con-mas-de-32-caracteres-123";

    @Test
    void generaUnTokenYRecuperaElUsuario() {
        JwtUtil jwt = new JwtUtil(CLAVE_VALIDA);
        String token = jwt.generarToken("comercio_demo");
        assertEquals("comercio_demo", jwt.leerUsuario(token));
    }

    @Test
    void rechazaUnaClaveVacia() {
        assertThrows(IllegalStateException.class, () -> new JwtUtil(""));
    }

    @Test
    void rechazaUnaClaveMenorA32Bytes() {
        assertThrows(IllegalStateException.class, () -> new JwtUtil("clave-corta"));
    }

    @Test
    void rechazaUnTokenFirmadoConOtraClave() {
        String token = new JwtUtil(CLAVE_VALIDA).generarToken("ana");
        JwtUtil otro = new JwtUtil("otra-clave-distinta-con-mas-de-32-caracteres-999");
        assertThrows(JwtException.class, () -> otro.leerUsuario(token));
    }
}
