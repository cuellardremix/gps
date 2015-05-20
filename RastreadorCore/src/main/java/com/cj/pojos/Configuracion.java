package com.cj.pojos;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Configuracion {
	@Id
	@GeneratedValue
	private Integer idConfiguracion;
	private Integer retraso;

	public Integer getRetraso() {
		return retraso;
	}

	public void setRetraso(Integer retraso) {
		this.retraso = retraso;
	}
	
	
}
