package dev.itinajero.app.service;

import dev.itinajero.app.model.Usuario;
import dev.itinajero.app.repository.IUsuariosRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuariosServiceImpl implements IUsuariosService {
	private static final Logger logger = LogManager.getLogger(UsuariosServiceImpl.class);

	@Autowired
	private IUsuariosRepository usuariosRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Usuario autenticar(String username, String password) {
		logger.info("Autenticando usuario: {}", username);
		Usuario usuario = usuariosRepository.findByUsername(username).orElse(null);
		if (usuario == null) {
			logger.warn("Usuario no encontrado: {}", username);
			return null;
		}
		// Cambia usuario.getEstatus() por usuario.getPassword() si tienes el campo correcto
		if (!passwordEncoder.matches(password, usuario.getPassword())) {
			logger.warn("Contraseña incorrecta para usuario: {}", username);
			return null;
		}
		logger.info("Usuario autenticado correctamente: {}", username);
		return usuario;
	}
    
}