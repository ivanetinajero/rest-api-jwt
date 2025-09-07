package dev.itinajero.app.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import dev.itinajero.app.dto.AuthResponse;
import dev.itinajero.app.dto.Login;
import dev.itinajero.app.model.Usuario;
import dev.itinajero.app.security.JwtUtil;
import dev.itinajero.app.service.IUsuariosService;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private static final Logger logger = LogManager.getLogger(AuthController.class);

    @Autowired
    private IUsuariosService usuariosService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody Login loginDTO) {
        logger.info("Intento de login para usuario: {}", loginDTO.getUsername());
        Usuario usuario = usuariosService.autenticar(loginDTO.getUsername(), loginDTO.getPassword());
        if (usuario == null) {
            logger.warn("Usuario o contraseña incorrectos: {}", loginDTO.getUsername());
            return ResponseEntity.status(401).body(new AuthResponse(null, "Usuario o contraseña incorrectos"));
        }
        String token = jwtUtil.generateToken(usuario);
        logger.info("Login exitoso, token generado para usuario: {}", loginDTO.getUsername());
        return ResponseEntity.ok(new AuthResponse(token, "Login exitoso"));
    }

}
