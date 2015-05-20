package com.cj.pojos;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table
public class Vehiculo {

	@Id
	@GeneratedValue
	private Integer vehLla;
	private Date vehFAl;
	private Date vehFBa;
	private String vehDes;
	private String vehMar;
	private String vehTip;
	private String vehMod;
	private String vehTar;
	private String vehCap;
	private String vehCol;
	private Double vehVeM;
	private Integer vehEsD;

	@ManyToOne
	private Usuario usuario;
	
	private String vehImei;
	private String vehNoCel;
	private Integer vehPuerto;
	private Integer vehTanGas;
	
	
	private Character vehComAct;
	private Boolean estadoRegistro;
	
	
	public Vehiculo(){}

	

	public Integer getVehTanGas() {
		return vehTanGas;
	}



	public void setVehTanGas(Integer vehTanGas) {
		this.vehTanGas = vehTanGas;
	}



	public Usuario getUsuario() {
		return usuario;
	}



	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}



	public Integer getVehLla() {
		return vehLla;
	}


	public void setVehLla(Integer vehLla) {
		this.vehLla = vehLla;
	}


	public Date getVehFAl() {
		return vehFAl;
	}


	public void setVehFAl(Date vehFAl) {
		this.vehFAl = vehFAl;
	}


	public Date getVehFBa() {
		return vehFBa;
	}


	public void setVehFBa(Date vehFBa) {
		this.vehFBa = vehFBa;
	}


	public String getVehDes() {
		return vehDes;
	}


	public void setVehDes(String vehDes) {
		this.vehDes = vehDes;
	}


	public String getVehMar() {
		return vehMar;
	}


	public void setVehMar(String vehMar) {
		this.vehMar = vehMar;
	}


	public String getVehTip() {
		return vehTip;
	}


	public void setVehTip(String vehTip) {
		this.vehTip = vehTip;
	}


	public String getVehMod() {
		return vehMod;
	}


	public void setVehMod(String vehMod) {
		this.vehMod = vehMod;
	}


	public String getVehTar() {
		return vehTar;
	}


	public void setVehTar(String vehTar) {
		this.vehTar = vehTar;
	}


	public String getVehCap() {
		return vehCap;
	}


	public void setVehCap(String vehCap) {
		this.vehCap = vehCap;
	}


	public String getVehCol() {
		return vehCol;
	}


	public void setVehCol(String vehCol) {
		this.vehCol = vehCol;
	}



	public Double getVehVeM() {
		return vehVeM;
	}



	public void setVehVeM(Double vehVeM) {
		this.vehVeM = vehVeM;
	}



	public Integer getVehEsD() {
		return vehEsD;
	}


	public void setVehEsD(Integer vehEsD) {
		this.vehEsD = vehEsD;
	}






	public String getVehImei() {
		return vehImei;
	}


	public void setVehImei(String vehImei) {
		this.vehImei = vehImei;
	}


	public String getVehNoCel() {
		return vehNoCel;
	}


	public void setVehNoCel(String vehNoCel) {
		this.vehNoCel = vehNoCel;
	}


	public Integer getVehPuerto() {
		return vehPuerto;
	}


	public void setVehPuerto(Integer vehPuerto) {
		this.vehPuerto = vehPuerto;
	}


	public Character getVehComAct() {
		return vehComAct;
	}


	public void setVehComAct(Character vehComAct) {
		this.vehComAct = vehComAct;
	}


	public Boolean getEstadoRegistro() {
		return estadoRegistro;
	}


	public void setEstadoRegistro(Boolean estadoRegistro) {
		this.estadoRegistro = estadoRegistro;
	}
}
