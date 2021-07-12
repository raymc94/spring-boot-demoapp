package pojo;

import java.io.Serializable;

import javax.persistence.Column;

public class RecetaFavoritaId implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "id_receta")
	private Long idReceta;
	
	@Column(name = "id_usuario")
	private Long idUsuario;
	
	
	
	public RecetaFavoritaId() {
	}
	
	public RecetaFavoritaId(Long idReceta, Long idUsuario) {
		this.idReceta = idReceta;
		this.idUsuario = idUsuario;
	}
	public Long getIdReceta() {
		return idReceta;
	}
	public void setIdReceta(Long idReceta) {
		this.idReceta = idReceta;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	 
	 
	
}
