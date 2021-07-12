package pojo;

import java.io.Serializable;

import javax.persistence.Column;

public class ProductoRecetaId implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "id_receta")
	private Long idReceta;
	
	@Column(name = "id_producto")
	private Long idProducto;
	
	
	
	public ProductoRecetaId() {
	}
	
	public ProductoRecetaId(Long idReceta, Long idProducto) {
		this.idReceta = idReceta;
		this.idProducto = idProducto;
	}
	public Long getIdReceta() {
		return idReceta;
	}
	public void setIdReceta(Long idReceta) {
		this.idReceta = idReceta;
	}
	public Long getIdProducto() {
		return idProducto;
	}
	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}
	 
	 
	
}
