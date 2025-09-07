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
    private static final String SECRET_KEY_HS256 = "mi_clave_secreta_para_jwt_2025_12345678901234567890123456789012"; // 32+ chars para HS256
    //private static final String SECRET_KEY_HS512 = "clave_muy_larga_de_64_caracteres_para_hs512_12345678901234567890123456789012345678901234567890123456789012"; // 64+ chars para HS512
    private static final long EXPIRATION_MS = 3600000; // 1 hora

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY_HS256.getBytes());
        //return Keys.hmacShaKeyFor(SECRET_KEY_HS512.getBytes());       
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
                .header().type("JWT")
                .and()
                .subject(usuario.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .claims(claims)                
                .signWith(getKey(), Jwts.SIG.HS256)
                //.signWith(getKey(), Jwts.SIG.HS512)
                .compact();
        logger.debug("Token generado: {}", token);
        return token;
    }

    // 5. Obtención de claims y roles
    public Claims validateToken(String token) {
        logger.info("Validando token JWT");
        try {
            Claims claims = Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload();
            logger.debug("Token válido para usuario: {}", claims.getSubject());
            return claims;
        } catch (JwtException e) {
            logger.error("Token JWT inválido: {}", e.getMessage());
            return null;
        }
    }

}
