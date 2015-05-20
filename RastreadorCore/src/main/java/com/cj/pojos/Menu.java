package com.cj.pojos;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table
public class Menu {
	
	@Id
	@GeneratedValue
	private Integer idMenu;
	private String nombre;
	private String pagina;
	private Boolean estadoRegistro;
	@ManyToMany
	private List<Perfil> perfiles;
	
	public Menu(){}
	
	
	
	public List<Perfil> getPerfiles() {
		return perfiles;
	}



	public void setPerfiles(List<Perfil> perfiles) {
		this.perfiles = perfiles;
	}



	public Boolean getEstadoRegistro() {
		return estadoRegistro;
	}


	public void setEstadoRegistro(Boolean estadoRegistro) {
		this.estadoRegistro = estadoRegistro;
	}


	public String getPagina() {
		return pagina;
	}


	public void setPagina(String pagina) {
		this.pagina = pagina;
	}


	public Integer getIdMenu() {
		return idMenu;
	}
	public void setIdMenu(Integer idMenu) {
		this.idMenu = idMenu;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
	
}
