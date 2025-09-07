package dev.itinajero.app.model;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "Productos")
public class Producto {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "El [nombre] es obligatorio")
    private String nombre;

    @NotBlank(message = "La [descripcion] es obligatoria")
    private String descripcion;

    @NotNull(message = "El [precio] es obligatorio")
    @Positive(message = "El [precio] debe ser positivo")
    private Double precio;

    @NotNull(message = "La [cantidad] es obligatoria")
    @PositiveOrZero(message = "La [cantidad] no puede ser negativa")
    private Integer cantidad;

    @Column(name = "fechaCreacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

}
