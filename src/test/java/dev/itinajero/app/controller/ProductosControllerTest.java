package dev.itinajero.app.controller;

import static org.mockito.ArgumentMatchers.any;
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
// Indicamos aMockMvc que debe inicializarse sin aplicar los filtros de seguridad (como autenticación y autorización) definidos en tu aplicación.
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

   @Test
   void crearProducto_exitoso() throws Exception {
      Producto producto = new Producto();
      producto.setId(1L);
      producto.setNombre("Nuevo Producto");
      producto.setDescripcion("Desc");
      producto.setPrecio(100.0);
      producto.setCantidad(10);
      String json = "{" +
         "\"nombre\":\"Nuevo Producto\"," +
         "\"descripcion\":\"Desc\"," +
         "\"precio\":100.0," +
         "\"cantidad\":10" +
         "}";
      doNothing().when(productosService).guardar(any(Producto.class));

      mockMvc.perform(post("/api/productos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.nombre").value("Nuevo Producto"));
   }

   @Test
   void crearProducto_error() throws Exception {
      String json = "{" +
         "\"nombre\":\"\"," +
         "\"descripcion\":\"\"," +
         "\"precio\":null," +
         "\"cantidad\":null" +
         "}";
      doThrow(new RuntimeException("Error")).when(productosService).guardar(any(Producto.class));

      mockMvc.perform(post("/api/productos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isBadRequest());
   }

   @Test
   void actualizarProducto_exitoso() throws Exception {
      Producto existente = new Producto();
      existente.setId(1L);
      existente.setNombre("Existente");
      when(productosService.buscarPorId(1)).thenReturn(existente);
      doNothing().when(productosService).guardar(any(Producto.class));
      String json = "{" +
         "\"nombre\":\"Actualizado\"," +
         "\"descripcion\":\"Desc\"," +
         "\"precio\":200.0," +
         "\"cantidad\":5" +
         "}";

      mockMvc.perform(put("/api/productos/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre").value("Actualizado"));
   }

   @Test
   void actualizarProducto_noExiste() throws Exception {
      when(productosService.buscarPorId(2)).thenReturn(null);
      String json = "{" +
         "\"nombre\":\"Actualizado\"," +
         "\"descripcion\":\"Desc\"," +
         "\"precio\":200.0," +
         "\"cantidad\":5" +
         "}";

      mockMvc.perform(put("/api/productos/2")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isNotFound());
   }

   @Test
   void eliminarProducto_exitoso() throws Exception {
      Producto existente = new Producto();
      existente.setId(1L);
      when(productosService.buscarPorId(1)).thenReturn(existente);
      doNothing().when(productosService).eliminar(1);

      mockMvc.perform(delete("/api/productos/1"))
            .andExpect(status().isNoContent());
   }

   @Test
   void eliminarProducto_noExiste() throws Exception {
      when(productosService.buscarPorId(2)).thenReturn(null);

      mockMvc.perform(delete("/api/productos/2"))
            .andExpect(status().isNotFound());
   }

}
