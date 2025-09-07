package dev.itinajero.app.service;

import dev.itinajero.app.model.Usuario;

public interface IUsuariosService {
	Usuario autenticar(String username, String password);
}
