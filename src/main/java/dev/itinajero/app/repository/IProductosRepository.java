package dev.itinajero.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import dev.itinajero.app.model.Producto;

public interface IProductosRepository extends JpaRepository<Producto, Long> {

}
