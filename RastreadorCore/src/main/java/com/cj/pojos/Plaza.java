package com.cj.pojos;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table
public class Plaza {
	@Id
	@GeneratedValue
	private Integer plaLla;
	private Date plaFAl;
	private Date plaFBa;
	//V, G, R, P
	private Character plaTip;
	private String plaDes;
	private String plaDir;
	private String plaCol;
	private Integer plaCop;
	private String plaCiu;
	private String plaEst;
	private String plaNoC;
	private String platC1;
	private String platC2;
	@OneToMany
	private List<Cerca> cercas;
	private Boolean estadoRegistro;
	
	public Plaza(){}
	
	public Boolean getEstadoRegistro() {
		return estadoRegistro;
	}
	public void setEstadoRegistro(Boolean estadoRegistro) {
		this.estadoRegistro = estadoRegistro;
	}
	public Integer getPlaLla() {
		return plaLla;
	}
	public void setPlaLla(Integer plaLla) {
		this.plaLla = plaLla;
	}
	public Date getPlaFAl() {
		return plaFAl;
	}
	public void setPlaFAl(Date plaFAl) {
		this.plaFAl = plaFAl;
	}
	public Date getPlaFBa() {
		return plaFBa;
	}
	public void setPlaFBa(Date plaFBa) {
		this.plaFBa = plaFBa;
	}
	public Character getPlaTip() {
		return plaTip;
	}
	public void setPlaTip(Character plaTip) {
		this.plaTip = plaTip;
	}
	public String getPlaDes() {
		return plaDes;
	}
	public void setPlaDes(String plaDes) {
		this.plaDes = plaDes;
	}
	public String getPlaDir() {
		return plaDir;
	}
	public void setPlaDir(String plaDir) {
		this.plaDir = plaDir;
	}
	public String getPlaCol() {
		return plaCol;
	}
	public void setPlaCol(String plaCol) {
		this.plaCol = plaCol;
	}
	public Integer getPlaCop() {
		return plaCop;
	}
	public void setPlaCop(Integer plaCop) {
		this.plaCop = plaCop;
	}
	public String getPlaCiu() {
		return plaCiu;
	}
	public void setPlaCiu(String plaCiu) {
		this.plaCiu = plaCiu;
	}
	public String getPlaEst() {
		return plaEst;
	}
	public void setPlaEst(String plaEst) {
		this.plaEst = plaEst;
	}
	public String getPlaNoC() {
		return plaNoC;
	}
	public void setPlaNoC(String plaNoC) {
		this.plaNoC = plaNoC;
	}
	public String getPlatC1() {
		return platC1;
	}
	public void setPlatC1(String platC1) {
		this.platC1 = platC1;
	}
	public String getPlatC2() {
		return platC2;
	}
	public void setPlatC2(String platC2) {
		this.platC2 = platC2;
	}
	public List<Cerca> getCercas() {
		return cercas;
	}
	public void setCercas(List<Cerca> cercas) {
		this.cercas = cercas;
	}
	
	
}
