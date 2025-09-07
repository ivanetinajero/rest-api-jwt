package dev.itinajero.app.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import dev.itinajero.app.model.Producto;
import dev.itinajero.app.repository.IProductosRepository;

@Service
public class ProductosServiceImpl implements IProductosService {

    @Autowired
    private IProductosRepository productosRepository;

    @Override
    public Page<Producto> buscarTodos(Pageable pageable) {
        return productosRepository.findAll(pageable);
    }

    @Override
    public java.util.List<Producto> buscarTodos() {
        return productosRepository.findAll();
    }

    @Override
    public Page<Producto> buscarTodos(Example<Producto> example, Pageable pageable) {
        return productosRepository.findAll(example, pageable);
    }

    @Override
    public void guardar(Producto producto) {
        productosRepository.save(producto);
    }

    @Override
    public Producto buscarPorId(int idProducto) {
        Optional<Producto> optional = productosRepository.findById((long) idProducto);
        return optional.orElse(null);
    }

    @Override
    public void eliminar(int idProducto) {
        productosRepository.deleteById((long) idProducto);
    }

}
