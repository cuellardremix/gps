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
public class Ruta {
	@Id
	@GeneratedValue
	private Integer rutLla;
	private String rutNom;
	private Double rutAnc;
	private Date rutFAl;
	private Date rutFBa;
	private String rutDes;
	private Date rutFec;
	private Date rutFeI;
	private Date rutFeF;
	@ManyToOne
	private Plaza plaza;
	private Boolean estadoRegistro;
	@OneToMany
	private List<Turno> turnos;
	@OneToMany
	private List<GPSData> marcas;

	@ManyToOne
	private Usuario usuario;
	
	
	@ManyToOne
	private Cerca rutGeo;

	@ManyToOne
	private Vehiculo vehiculo1;
	@ManyToOne
	private Vehiculo vehiculo2;
	@ManyToOne
	private Vehiculo vehiculo3;
	@ManyToOne
	private Vehiculo vehiculo4;
	@ManyToOne
	private Vehiculo vehiculo5;
	
	public Ruta(){}

	
	
	public Vehiculo getVehiculo1() {
		return vehiculo1;
	}


	public void setVehiculo1(Vehiculo vehiculo1) {
		this.vehiculo1 = vehiculo1;
	}


	public Vehiculo getVehiculo2() {
		return vehiculo2;
	}


	public void setVehiculo2(Vehiculo vehiculo2) {
		this.vehiculo2 = vehiculo2;
	}


	public Vehiculo getVehiculo3() {
		return vehiculo3;
	}


	public void setVehiculo3(Vehiculo vehiculo3) {
		this.vehiculo3 = vehiculo3;
	}


	public Vehiculo getVehiculo4() {
		return vehiculo4;
	}


	public void setVehiculo4(Vehiculo vehiculo4) {
		this.vehiculo4 = vehiculo4;
	}


	public Vehiculo getVehiculo5() {
		return vehiculo5;
	}


	public void setVehiculo5(Vehiculo vehiculo5) {
		this.vehiculo5 = vehiculo5;
	}

	
	public Cerca getRutGeo() {
		return rutGeo;
	}



	public void setRutGeo(Cerca rutGeo) {
		this.rutGeo = rutGeo;
	}





	public Usuario getUsuario() {
		return usuario;
	}


	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}


	public Ruta(Integer rutLla){
		this.rutLla=rutLla;
	}
	
	public List<Turno> getTurnos() {
		return turnos;
	}


	public void setTurnos(List<Turno> turnos) {
		this.turnos = turnos;
	}


	public Integer getRutLla() {
		return rutLla;
	}

	public void setRutLla(Integer rutLla) {
		this.rutLla = rutLla;
	}

	public String getRutNom() {
		return rutNom;
	}

	public void setRutNom(String rutNom) {
		this.rutNom = rutNom;
	}

	public Double getRutAnc() {
		return rutAnc;
	}

	public void setRutAnc(Double rutAnc) {
		this.rutAnc = rutAnc;
	}

	public Date getRutFAl() {
		return rutFAl;
	}

	public void setRutFAl(Date rutFAl) {
		this.rutFAl = rutFAl;
	}

	public Date getRutFBa() {
		return rutFBa;
	}

	public void setRutFBa(Date rutFBa) {
		this.rutFBa = rutFBa;
	}

	public String getRutDes() {
		return rutDes;
	}

	public void setRutDes(String rutDes) {
		this.rutDes = rutDes;
	}

	public Date getRutFec() {
		return rutFec;
	}

	public void setRutFec(Date rutFec) {
		this.rutFec = rutFec;
	}

	public Date getRutFeI() {
		return rutFeI;
	}

	public void setRutFeI(Date rutFeI) {
		this.rutFeI = rutFeI;
	}

	public Date getRutFeF() {
		return rutFeF;
	}

	public void setRutFeF(Date rutFeF) {
		this.rutFeF = rutFeF;
	}

	public Plaza getPlaza() {
		return plaza;
	}

	public void setPlaza(Plaza plaza) {
		this.plaza = plaza;
	}

	public Boolean getEstadoRegistro() {
		return estadoRegistro;
	}

	public void setEstadoRegistro(Boolean estadoRegistro) {
		this.estadoRegistro = estadoRegistro;
	}

	public List<GPSData> getMarcas() {
		return marcas;
	}

	public void setMarcas(List<GPSData> marcas) {
		this.marcas = marcas;
	}
	
	
	
//	public List<Cerca> getCercas() {
//		return cercas;
//	}
//	public void setCercas(List<Cerca> cercas) {
//		this.cercas = cercas;
//	}
	
}
