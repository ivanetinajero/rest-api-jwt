package dev.itinajero.app.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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

    @Test
    void testBuscarTodosConPaginacion() {
        Producto p = new Producto();
        Page<Producto> page = new PageImpl<>(Collections.singletonList(p));
        when(productosRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<Producto> resultado = productosService.buscarTodos(PageRequest.of(0, 10));
        assertEquals(1, resultado.getTotalElements());
    }

    @Test
    void testBuscarTodosConExampleYPageable() {
        Producto p = new Producto();
        Example<Producto> example = Example.of(p);
        Page<Producto> page = new PageImpl<>(Collections.singletonList(p));
        when(productosRepository.findAll(eq(example), any(PageRequest.class))).thenReturn(page);

        Page<Producto> resultado = productosService.buscarTodos(example, PageRequest.of(0, 10));
        assertEquals(1, resultado.getTotalElements());
    }

    @Test
    void testGuardar() {
        Producto p = new Producto();
        when(productosRepository.save(p)).thenReturn(p);
        productosService.guardar(p);
        verify(productosRepository, times(1)).save(p);
    }

    @Test
    void testEliminar() {
        doNothing().when(productosRepository).deleteById(1L);
        productosService.eliminar(1);
        verify(productosRepository, times(1)).deleteById(1L);
    }

}
