package dev.itinajero.app.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import dev.itinajero.app.model.Producto;
import dev.itinajero.app.repository.IProductosRepository;

class ProductosServiceImplTest {

    @Mock
    private IProductosRepository productosRepository;

    @InjectMocks
    private ProductosServiceImpl productosService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBuscarTodos() {
        Producto p1 = new Producto();
        Producto p2 = new Producto();
        when(productosRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Producto> productos = productosService.buscarTodos();
        assertEquals(2, productos.size());
    }

    @Test
    void testBuscarPorId() {
        Producto p = new Producto();
        when(productosRepository.findById(1L)).thenReturn(Optional.of(p));

        Producto resultado = productosService.buscarPorId(1);
        assertNotNull(resultado);
    }

    @Test
    void testBuscarPorIdNoExiste() {
        when(productosRepository.findById(2L)).thenReturn(Optional.empty());

        Producto resultado = productosService.buscarPorId(2);
        assertNull(resultado);
    }
    
}
