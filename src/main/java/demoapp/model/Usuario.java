package demoapp.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String email;
    private String nombre;
    private String password;
    private Boolean administrador;
    private Boolean bloqueado;
    @Column(name = "fecha_nacimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;
    
    @OneToMany(mappedBy = "idUsuario")
    private Set<RecetaFavorita> recetasFavoritas = new HashSet<RecetaFavorita>();

    // Definimos el tipo de fetch como EAGER para que
    // cualquier consulta que devuelve un usuario rellene automáticamente
    // toda su lista de tareas
    // CUIDADO!! No es recomendable hacerlo en aquellos casos en los
    // que la relación pueda traer a memoria una gran cantidad de entidades
    /*@OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER)
    Set<Tarea> tareas = new HashSet<>();
*/
    // Constructor vacío necesario para JPA/Hibernate.
    // Lo hacemos privado para que no se pueda usar desde el código de la aplicación. Para crear un
    // usuario en la aplicación habrá que llamar al constructor público. Hibernate sí que lo puede usar, a pesar
    // de ser privado.
    private Usuario() {}

    // Constructor público con los atributos obligatorios. En este caso el correo electrónico.
    public Usuario(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

 /*   public Set<Tarea> getTareas() {
        return tareas;
    }

    public void setTareas(Set<Tarea> tareas) {
        this.tareas = tareas;
    }
   */ 
    public Boolean getAdministrador() {
		return administrador;
	}

	public void setAdministrador(Boolean administrador) {
		this.administrador = administrador;
	}

    public Boolean getBloqueado() {
		return bloqueado;
	}

	public void setBloqueado(Boolean bloqueado) {
		this.bloqueado = bloqueado;
	}
	
	public Set<RecetaFavorita> getRecetasFavoritas() {
		return recetasFavoritas;
	}

	public void setRecetasFavoritas(Set<RecetaFavorita> recetasFavoritas) {
		this.recetasFavoritas = recetasFavoritas;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        if (id != null && usuario.id != null)
            // Si tenemos los ID, comparamos por ID
            return Objects.equals(id, usuario.id);
        // sino comparamos por campos obligatorios
        return email.equals(usuario.email);
    }
    

	@Override
    public int hashCode() {
        // Generamos un hash basado en los campos obligatorios
        return Objects.hash(email);
    }
}
