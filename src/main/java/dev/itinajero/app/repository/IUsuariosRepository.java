package dev.itinajero.app.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import dev.itinajero.app.model.Usuario;

public interface IUsuariosRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

}
