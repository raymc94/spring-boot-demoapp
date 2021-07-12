package demoapp.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "receta")
public class Receta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String nombre;
    private String descripcion;
    
    @Column(name = "id_owner")
    private Long idOwner;
    
    @Column(name = "type_receta")
    private Integer typeReceta;
    
    @OneToMany(mappedBy = "idReceta")
    private Set<ProductoReceta> productosAsignados = new HashSet<ProductoReceta>();
    
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
	public Long getIdOwner() {
		return idOwner;
	}
	public void setIdOwner(Long idOwner) {
		this.idOwner = idOwner;
	}
	public Integer getTypeReceta() {
		return typeReceta;
	}
	public void setTypeReceta(Integer typeReceta) {
		this.typeReceta = typeReceta;
	}
	public Set<ProductoReceta> getProductosAsignados() {
		return productosAsignados;
	}
	public void setProductosAsignados(Set<ProductoReceta> productosAsignados) {
		this.productosAsignados = productosAsignados;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
