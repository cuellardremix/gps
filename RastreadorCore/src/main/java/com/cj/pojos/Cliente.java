package com.cj.pojos;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table
public class Cliente {

	@Id
	@GeneratedValue
	private Integer cliLla;
	private Date cliFAl;
	private Date clifBa;
	private String cliNom;
	private String cliDom;
	private String cliCol;
	private Integer cliCOP;
	private String cliCiu;
	private String cliEdo;
	private String cliPai;
	private String cliCo1;
	private String cliT11;
	private String cliT21;
	private String cliC11;
	private String cliC21;
	private String cliCo2;
	private String cliT12;
	private String cliT22;
	private String cliC12;
	private String cliC22;
	@OneToOne
	private Usuario usuario;
	@OneToMany(fetch = FetchType.EAGER)
	private List<Vehiculo> vehiculos;
	
	
	private Boolean estadoRegistro;
	
	public Cliente(){}
	
	
	public Boolean getEstadoRegistro() {
		return estadoRegistro;
	}


	public void setEstadoRegistro(Boolean estadoRegistro) {
		this.estadoRegistro = estadoRegistro;
	}





	

	public List<Vehiculo> getVehiculos() {
		return vehiculos;
	}


	public void setVehiculos(List<Vehiculo> vehiculos) {
		this.vehiculos = vehiculos;
	}


	public Usuario getUsuario() {
		return usuario;
	}


	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}


	public Integer getCliLla() {
		return cliLla;
	}
	public void setCliLla(Integer cliLla) {
		this.cliLla = cliLla;
	}
	public Date getCliFAl() {
		return cliFAl;
	}
	public void setCliFAl(Date cliFAl) {
		this.cliFAl = cliFAl;
	}
	public Date getClifBa() {
		return clifBa;
	}
	public void setClifBa(Date clifBa) {
		this.clifBa = clifBa;
	}
	public String getCliNom() {
		return cliNom;
	}
	public void setCliNom(String cliNom) {
		this.cliNom = cliNom;
	}
	public String getCliDom() {
		return cliDom;
	}
	public void setCliDom(String cliDom) {
		this.cliDom = cliDom;
	}
	public String getCliCol() {
		return cliCol;
	}
	public void setCliCol(String cliCol) {
		this.cliCol = cliCol;
	}
	public Integer getCliCOP() {
		return cliCOP;
	}
	public void setCliCOP(Integer cliCOP) {
		this.cliCOP = cliCOP;
	}
	public String getCliCiu() {
		return cliCiu;
	}
	public void setCliCiu(String cliCiu) {
		this.cliCiu = cliCiu;
	}
	public String getCliEdo() {
		return cliEdo;
	}
	public void setCliEdo(String cliEdo) {
		this.cliEdo = cliEdo;
	}
	public String getCliPai() {
		return cliPai;
	}
	public void setCliPai(String cliPai) {
		this.cliPai = cliPai;
	}
	public String getCliCo1() {
		return cliCo1;
	}
	public void setCliCo1(String cliCo1) {
		this.cliCo1 = cliCo1;
	}
	public String getCliT11() {
		return cliT11;
	}
	public void setCliT11(String cliT11) {
		this.cliT11 = cliT11;
	}
	public String getCliT21() {
		return cliT21;
	}
	public void setCliT21(String cliT21) {
		this.cliT21 = cliT21;
	}
	public String getCliC11() {
		return cliC11;
	}
	public void setCliC11(String cliC11) {
		this.cliC11 = cliC11;
	}
	public String getCliC21() {
		return cliC21;
	}
	public void setCliC21(String cliC21) {
		this.cliC21 = cliC21;
	}
	public String getCliCo2() {
		return cliCo2;
	}
	public void setCliCo2(String cliCo2) {
		this.cliCo2 = cliCo2;
	}
	public String getCliT12() {
		return cliT12;
	}
	public void setCliT12(String cliT12) {
		this.cliT12 = cliT12;
	}
	public String getCliT22() {
		return cliT22;
	}
	public void setCliT22(String cliT22) {
		this.cliT22 = cliT22;
	}
	public String getCliC12() {
		return cliC12;
	}
	public void setCliC12(String cliC12) {
		this.cliC12 = cliC12;
	}
	public String getCliC22() {
		return cliC22;
	}
	public void setCliC22(String cliC22) {
		this.cliC22 = cliC22;
	}
	
	
}
