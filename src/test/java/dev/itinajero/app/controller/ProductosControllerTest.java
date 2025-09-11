package dev.itinajero.app.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import dev.itinajero.app.model.Producto;
import dev.itinajero.app.security.JwtUtil;
import dev.itinajero.app.service.IProductosService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductosController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductosControllerTest {

   @Autowired
   private MockMvc mockMvc;

   @MockitoBean
   private IProductosService productosService;

   @MockitoBean
   private JwtUtil jwtUtil;

   @Test
   void consultarPorId_productoExiste() throws Exception {
      Producto producto = new Producto();
      producto.setId(1L);
      producto.setNombre("Test Producto");
      when(productosService.buscarPorId(1)).thenReturn(producto);

      mockMvc.perform(get("/api/productos/1")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.nombre").value("Test Producto"));
   }

   @Test
   void consultarPorId_productoNoExiste() throws Exception {
      when(productosService.buscarPorId(2)).thenReturn(null);

      mockMvc.perform(get("/api/productos/2")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
   }

}
