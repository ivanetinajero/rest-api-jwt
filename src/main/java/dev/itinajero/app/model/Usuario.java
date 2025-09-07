package dev.itinajero.app.model;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Usuarios")
public class Usuario {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;
    private String nombreCompleto;
    private String email;
    private Integer estatus = 1;
    private String password;

    @ManyToOne
    @JoinColumn(name = "idSucursal")
    private Sucursal sucursal;
;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "UsuariosPerfiles",
        joinColumns = @JoinColumn(name = "idUsuario"),
        inverseJoinColumns = @JoinColumn(name = "idPerfil")
    )
    private Set<Perfil> perfiles = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Perfil> getPerfiles() {
        return perfiles;
    }

    public void setPerfiles(Set<Perfil> perfiles) {
        this.perfiles = perfiles;
    }

    public Integer getEstatus() {
        return estatus;
    }

    public void setEstatus(Integer estatus) {
        this.estatus = estatus;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }
    
    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

}
