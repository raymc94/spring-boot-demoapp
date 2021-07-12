package demoapp.model;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import pojo.RecetaFavoritaId;

@Entity
@Table(name = "receta_favorita")
public class RecetaFavorita implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    private RecetaFavoritaId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idReceta")
    @JoinColumn(name = "id_receta")
    private Receta idReceta;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idUsuario")
    @JoinColumn(name = "id_usuario")
    private Usuario idUsuario;

    
	public Receta getIdReceta() {
		return idReceta;
	}
	public void setIdReceta(Receta idReceta) {
		this.idReceta = idReceta;
	}
	public Usuario getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Usuario idUsuario) {
		this.idUsuario = idUsuario;
	}
	public RecetaFavoritaId getId() {
		return id;
	}
	public void setId(RecetaFavoritaId id) {
		this.id = id;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
