package demoapp.model;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import pojo.ProductoRecetaId;

@Entity
@Table(name = "producto_receta")
public class ProductoReceta implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    private ProductoRecetaId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idReceta")
    @JoinColumn(name = "id_receta")
    private Receta idReceta;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idProducto")
    @JoinColumn(name = "id_producto")
    private Producto idProducto;
    @NotNull
    private Long valor;
	public Receta getIdReceta() {
		return idReceta;
	}
	public void setIdReceta(Receta idReceta) {
		this.idReceta = idReceta;
	}
	public Producto getIdProducto() {
		return idProducto;
	}
	public void setIdProducto(Producto idProducto) {
		this.idProducto = idProducto;
	}
	public Long getValor() {
		return valor;
	}
	public void setValor(Long valor) {
		this.valor = valor;
	}
	
	public ProductoRecetaId getId() {
		return id;
	}
	public void setId(ProductoRecetaId id) {
		this.id = id;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
