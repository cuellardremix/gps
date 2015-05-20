package com.cj.pojos;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Table
@Entity
public class Comando {

	
	
	@Id
	@GeneratedValue
	private Integer idComando;
	private Character charComando;
	private String descripcion;
	private Boolean estadoRegistro;
	
	public Comando(){}
	
	public Comando(Character charComando){
		this.charComando=charComando;
	}
	
	public Integer getIdComando() {
		return idComando;
	}

	public void setIdComando(Integer idComando) {
		this.idComando = idComando;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Boolean getEstadoRegistro() {
		return estadoRegistro;
	}

	public void setEstadoRegistro(Boolean estadoRegistro) {
		this.estadoRegistro = estadoRegistro;
	}

	public Character getCharComando() {
		return charComando;
	}

	public void setCharComando(Character charComando) {
		this.charComando = charComando;
	}



	

}
