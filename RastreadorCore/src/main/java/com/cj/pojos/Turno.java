package com.cj.pojos;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Turno {

	@Id
	@GeneratedValue
	private Integer turLla;
	private Date turFAl;
	private Date turFBa;
	private String turDes;
	private Boolean turLun;
	private Boolean turMar;
	private Boolean turMie;
	private Boolean turJue;
	private Boolean turVie;
	private Boolean turSab;
	private Boolean turDom;
	private Boolean estadoRegistro;
	private Date turHI1;
	private Date truHF1;
	
	public Turno(){}
	
	
	
	public Boolean getEstadoRegistro() {
		return estadoRegistro;
	}


	public void setEstadoRegistro(Boolean estadoRegistro) {
		this.estadoRegistro = estadoRegistro;
	}



	public Integer getTurLla() {
		return turLla;
	}
	public void setTurLla(Integer turLla) {
		this.turLla = turLla;
	}
	public Date getTurFAl() {
		return turFAl;
	}
	public void setTurFAl(Date turFAl) {
		this.turFAl = turFAl;
	}
	public Date getTurFBa() {
		return turFBa;
	}
	public void setTurFBa(Date turFBa) {
		this.turFBa = turFBa;
	}
	public String getTurDes() {
		return turDes;
	}
	public void setTurDes(String turDes) {
		this.turDes = turDes;
	}
	public Boolean getTurLun() {
		return turLun;
	}
	public void setTurLun(Boolean turLun) {
		this.turLun = turLun;
	}
	public Boolean getTurMar() {
		return turMar;
	}
	public void setTurMar(Boolean turMar) {
		this.turMar = turMar;
	}
	public Boolean getTurMie() {
		return turMie;
	}
	public void setTurMie(Boolean turMie) {
		this.turMie = turMie;
	}
	public Boolean getTurJue() {
		return turJue;
	}
	public void setTurJue(Boolean turJue) {
		this.turJue = turJue;
	}
	public Boolean getTurVie() {
		return turVie;
	}
	public void setTurVie(Boolean turVie) {
		this.turVie = turVie;
	}
	public Boolean getTurSab() {
		return turSab;
	}
	public void setTurSab(Boolean turSab) {
		this.turSab = turSab;
	}
	public Boolean getTurDom() {
		return turDom;
	}
	public void setTurDom(Boolean turDom) {
		this.turDom = turDom;
	}
	public Date getTurHI1() {
		return turHI1;
	}
	public void setTurHI1(Date turHI1) {
		this.turHI1 = turHI1;
	}
	public Date getTruHF1() {
		return truHF1;
	}
	public void setTruHF1(Date truHF1) {
		this.truHF1 = truHF1;
	}
	
	
}
