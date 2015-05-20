package com.cj.pojos;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class ControlRuta {
	@Id
	@GeneratedValue
	private Integer conLla;
	private Integer rutLla;
	private String vehImei;
	private Boolean activo;
	
	private Date fechaIni;
	private Date fechaFin;
	private Integer idRegistroI;
	private Integer idRegistroF;
	
	public ControlRuta(){}
	
	public Date getFechaIni() {
		return fechaIni;
	}

	public void setFechaIni(Date fechaIni) {
		this.fechaIni = fechaIni;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}



	public Integer getIdRegistroI() {
		return idRegistroI;
	}

	public void setIdRegistroI(Integer idRegistroI) {
		this.idRegistroI = idRegistroI;
	}

	public Integer getIdRegistroF() {
		return idRegistroF;
	}

	public void setIdRegistroF(Integer idRegistroF) {
		this.idRegistroF = idRegistroF;
	}

	public Integer getConLla() {
		return conLla;
	}
	public void setConLla(Integer conLla) {
		this.conLla = conLla;
	}
	public Integer getRutLla() {
		return rutLla;
	}
	public void setRutLla(Integer rutLla) {
		this.rutLla = rutLla;
	}
	public String getVehImei() {
		return vehImei;
	}
	public void setVehImei(String vehImei) {
		this.vehImei = vehImei;
	}
	public Boolean getActivo() {
		return activo;
	}
	public void setActivo(Boolean activo) {
		this.activo = activo;
	}
	
	
}
