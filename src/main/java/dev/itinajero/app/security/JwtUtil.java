package dev.itinajero.app.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import dev.itinajero.app.model.Usuario;

@Component
public class JwtUtil {

    private static final Logger logger = LogManager.getLogger(JwtUtil.class);
    private static final String SECRET_KEY = "mi_clave_secreta_para_jwt_2025_12345678901234567890123456789012"; // 32+
                                                                                                                // chars
    private static final long EXPIRATION_MS = 3600000; // 1 hora

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(Usuario usuario) {
        logger.info("Generando token JWT para usuario: {}", usuario.getUsername());
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", usuario.getId());
        claims.put("nombreCompleto", usuario.getNombreCompleto());
        claims.put("email", usuario.getEmail());
        claims.put("idSucursal", usuario.getSucursal() != null ? usuario.getSucursal().getId() : null);
        claims.put("sucursal", usuario.getSucursal() != null ? usuario.getSucursal().getNombre() : null);
        // Agregar claim 'perfiles' como lista de strings
        claims.put("perfiles", usuario.getPerfiles().stream().map(p -> p.getPerfil()).toList());

        String token = Jwts.builder()
                .subject(usuario.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .claims(claims)
                .signWith(getKey())
                .compact();
        logger.debug("Token generado: {}", token);
        return token;
    }

    public String validateToken(String token) {
        try {
            logger.info("Validando token JWT");
            Claims claims = getClaims(token);
            logger.debug("Token válido para usuario: {}", claims != null ? claims.getSubject() : null);
            return claims != null ? claims.getSubject() : null;
        } catch (JwtException e) {
            logger.error("Token JWT inválido: {}", e.getMessage());
            return null;
        }
    }

    public Claims getClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            logger.error("Error al extraer claims del token JWT: {}", e.getMessage());
            return null;
        }
    }

}
