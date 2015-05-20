package com.cj.pojos;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table
public class Usuario {

	@Id
	@GeneratedValue
	private Integer usuLla;
	
	private Date usuFal;
	private Date usuFBa;
	
	
	private String usuNom;
	private String usuCon;
	private Boolean estadoRegistro;
	
	@ManyToOne
	private Perfil perfil;
	@OneToOne
	private Cliente cliente;
	
	public Usuario(){}


	public Boolean getEstadoRegistro() {
		return estadoRegistro;
	}


	public void setEstadoRegistro(Boolean estadoRegistro) {
		this.estadoRegistro = estadoRegistro;
	}


	public Cliente getCliente() {
		return cliente;
	}


	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}


	public Usuario(String usuNom,String usuCon){
		this.usuCon=usuCon;
		this.usuNom=usuNom;
	}
	
	public Integer getUsuLla() {
		return usuLla;
	}

	public void setUsuLla(Integer usuLla) {
		this.usuLla = usuLla;
	}

	public Date getUsuFal() {
		return usuFal;
	}

	public void setUsuFal(Date usuFal) {
		this.usuFal = usuFal;
	}

	public Date getUsuFBa() {
		return usuFBa;
	}

	public void setUsuFBa(Date usuFBa) {
		this.usuFBa = usuFBa;
	}

	public String getUsuNom() {
		return usuNom;
	}

	public void setUsuNom(String usuNom) {
		this.usuNom = usuNom;
	}

	public String getUsuCon() {
		return usuCon;
	}

	public void setUsuCon(String usuCon) {
		this.usuCon = usuCon;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}
	
}
