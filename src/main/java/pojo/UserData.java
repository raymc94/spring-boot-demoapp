package pojo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class UserData {

	private String email;
	private String password;
	private String name;
	private Boolean administrador;
	
	@DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date fechaNacimiento;
	
	public UserData() {}
	public UserData(String email, String password, String name, Date fechaNacimiento) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.fechaNacimiento = fechaNacimiento;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	public Boolean getAdministrador() {
		return administrador;
	}
	public void setAdministrador(Boolean administrador) {
		this.administrador = administrador;
	}
	
	
}
