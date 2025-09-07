package dev.itinajero.app.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import io.jsonwebtoken.Claims;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LogManager.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            logger.info("Token JWT recibido en el header Authorization");
            // 4. Validación del token en cada petición
            Claims claims = jwtUtil.validateToken(token);
            if (claims != null) {
                String username = claims.getSubject();
                logger.info("Token JWT válido para usuario: {}", username);
                Collection<SimpleGrantedAuthority> authorities = extractAuthorities(claims);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                logger.warn("Token JWT inválido o expirado");
            }
        } else {
            logger.debug("No se encontró el header Authorization o no tiene formato Bearer");
        }
        filterChain.doFilter(request, response);
    }

    private Collection<SimpleGrantedAuthority> extractAuthorities(Claims claims) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Object perfilesObj = claims.get("perfiles");
        if (perfilesObj instanceof List<?>) {
            for (Object perfil : (List<?>) perfilesObj) {
                authorities.add(new SimpleGrantedAuthority(perfil.toString()));
            }
        } else if (perfilesObj instanceof String) {
            authorities.add(new SimpleGrantedAuthority(perfilesObj.toString()));
        }
        return authorities;
    }

}
