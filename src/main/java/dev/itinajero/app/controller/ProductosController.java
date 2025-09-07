package dev.itinajero.app.controller;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import dev.itinajero.app.model.Producto;
import dev.itinajero.app.security.JwtUtil;
import dev.itinajero.app.service.IProductosService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/productos")

public class ProductosController {

    private static final Logger logger = LogManager.getLogger(ProductosController.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private IProductosService productosService;

    @GetMapping
    public ResponseEntity<List<Producto>> listarProductos(Authentication auth, HttpServletRequest request) {        
        
        /*******************************************************
         * Ejemplo: obtener claims del usuario autenticado *****
         *******************************************************/

        String username = auth.getName();
        Object principal = auth.getPrincipal();
        var authorities = auth.getAuthorities();
        logger.info("Usuario autenticado: {}", username);
        logger.info("Detalles del principal: {}", principal);
        logger.info("Authorities: {}", authorities);           

        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7); // Omitimos el prefijo "Bearer "
        Claims claims =jwtUtil.validateToken(token);
        logger.info("Claims obtenidos del token: {}", claims);
        logger.info("idSucursal: {}", claims.get("idSucursal"));
        logger.info("sucursal: {}", claims.get("sucursal"));
        logger.info("nombreCompleto: {}", claims.get("nombreCompleto"));
        logger.info("email: {}", claims.get("email"));

        /*******************************************************
         * Ejemplo: obtener claims del usuario autenticado *****
         *******************************************************/

        logger.info("Consultando todos los productos");
        List<Producto> productos = productosService.buscarTodos();
        if (productos == null || productos.isEmpty()) {
            logger.warn("No se encontraron productos");
            return ResponseEntity.noContent().build();
        }
        logger.info("Productos encontrados: {}", productos.size());
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> consultarPorId(@PathVariable int id) {
        logger.info("Consultando producto por ID: {}", id);
        Producto producto = productosService.buscarPorId(id);
        if (producto == null) {
            logger.warn("Producto no encontrado con ID: {}", id);
            return ResponseEntity.notFound().build();
        }
        logger.info("Producto encontrado: {}", producto.getNombre());
        return ResponseEntity.ok(producto);
    }

    @PostMapping
    public ResponseEntity<Producto> crearProducto(@Validated @RequestBody Producto producto) {
        logger.info("Creando producto: {}", producto.getNombre());
        try {
            productosService.guardar(producto);
            logger.info("Producto creado exitosamente: {}", producto.getId());
            return ResponseEntity.status(201).body(producto);
        } catch (Exception e) {
            logger.error("Error al crear producto: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable int id,
            @Validated @RequestBody Producto producto) {
        logger.info("Actualizando producto con ID: {}", id);
        Producto existente = productosService.buscarPorId(id);
        if (existente == null) {
            logger.warn("Producto no encontrado con ID: {}", id);
            return ResponseEntity.notFound().build();
        }
        try {
            producto.setId(Long.valueOf(id));
            productosService.guardar(producto);
            logger.info("Producto actualizado exitosamente: {}", producto.getId());
            return ResponseEntity.ok(producto);
        } catch (Exception e) {
            logger.error("Error al actualizar producto: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable int id) {
        logger.info("Eliminando producto con ID: {}", id);
        Producto existente = productosService.buscarPorId(id);
        if (existente == null) {
            logger.warn("Producto no encontrado con ID: {}", id);
            return ResponseEntity.notFound().build();
        }
        try {
            productosService.eliminar(id);
            logger.info("Producto eliminado exitosamente: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error al eliminar producto: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

}