package dev.itinajero.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Sucursales") // Hibernate mapea a esta tabla con MySQL.
public class Sucursal {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment MySQL
	private Integer id;
	private String nombre;
	private String clave;
	private String direccion;
	private String telefono;
	private String estatus;
	
	public Sucursal(Integer id) {
		this.id = id;
	}

	public Sucursal() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEstatus() {
		return estatus;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}
	
	public String getDetalleSucursal() {
		return nombre + " (" + clave + ")";
	}

	@Override
	public String toString() {
		return "Sucursal [id=" + id + ", nombre=" + nombre + ", clave=" + clave + ", direccion=" + direccion
				+ ", telefono=" + telefono + ", estatus=" + estatus + "]";
	}

}