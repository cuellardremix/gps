package com.cj.pojos;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table
public class Perfil {

	@Id
	@GeneratedValue
	private Integer perLla;
	private Date perFAl;
	private Date perFBa;
	
	private String perNom;
	private String perTip;
	
//
//	@ManyToMany
//	private List<Menu> menus;
	
	private Boolean estadoRegistro;
	
	public Perfil(){}

	public Perfil(Integer perLla){
		this.perLla=perLla;
	}
//	public List<Menu> getMenus() {
//		return menus;
//	}


//	public void setMenus(List<Menu> menus) {
//		this.menus = menus;
//	}


	public Integer getPerLla() {
		return perLla;
	}

	public void setPerLla(Integer perLla) {
		this.perLla = perLla;
	}

	public Date getPerFAl() {
		return perFAl;
	}

	public void setPerFAl(Date perFAl) {
		this.perFAl = perFAl;
	}

	public Date getPerFBa() {
		return perFBa;
	}

	public void setPerFBa(Date perFBa) {
		this.perFBa = perFBa;
	}

	public String getPerNom() {
		return perNom;
	}

	public void setPerNom(String perNom) {
		this.perNom = perNom;
	}

	public String getPerTip() {
		return perTip;
	}

	public void setPerTip(String perTip) {
		this.perTip = perTip;
	}

	public Boolean getEstadoRegistro() {
		return estadoRegistro;
	}

	public void setEstadoRegistro(Boolean estadoRegistro) {
		this.estadoRegistro = estadoRegistro;
	}
	

	
	
}
