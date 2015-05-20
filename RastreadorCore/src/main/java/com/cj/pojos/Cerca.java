package com.cj.pojos;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table
public class Cerca {
	
	@Id
	@GeneratedValue
	private Integer geoLla;
	private Date geoFAl;
	private Date geoFBa;
	private String geoDes;
	
	@OneToOne
	private Ruta geoRut;
	@ManyToOne
	private Plaza geoPla;
	@ManyToOne
	private Usuario usuario;
	
	private String geoNom;
	private Double latitude;
	private Double longitude;

	@OneToMany
	private List<GPSData> marcas;
	private Boolean estadoRegistro;

	@OneToMany(fetch = FetchType.EAGER)
	private List<Vehiculo> vehiculos;
	
	
	public List<Vehiculo> getVehiculos() {
		return vehiculos;
	}


	public void setVehiculos(List<Vehiculo> vehiculos) {
		this.vehiculos = vehiculos;
	}


	public Cerca(){}

	
	public Usuario getUsuario() {
		return usuario;
	}


	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Boolean getEstadoRegistro() {
		return estadoRegistro;
	}


	public void setEstadoRegistro(Boolean estadoRegistro) {
		this.estadoRegistro = estadoRegistro;
	}


	public Integer getGeoLla() {
		return geoLla;
	}

	public void setGeoLla(Integer geoLla) {
		this.geoLla = geoLla;
	}

	public Date getGeoFAl() {
		return geoFAl;
	}

	public void setGeoFAl(Date geoFAl) {
		this.geoFAl = geoFAl;
	}

	public Date getGeoFBa() {
		return geoFBa;
	}

	public void setGeoFBa(Date geoFBa) {
		this.geoFBa = geoFBa;
	}

	public String getGeoDes() {
		return geoDes;
	}

	public void setGeoDes(String geoDes) {
		this.geoDes = geoDes;
	}

	public Ruta getGeoRut() {
		return geoRut;
	}

	public void setGeoRut(Ruta geoRut) {
		this.geoRut = geoRut;
	}

	public Plaza getGeoPla() {
		return geoPla;
	}

	public void setGeoPla(Plaza geoPla) {
		this.geoPla = geoPla;
	}

	public String getGeoNom() {
		return geoNom;
	}

	public void setGeoNom(String geoNom) {
		this.geoNom = geoNom;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public List<GPSData> getMarcas() {
		return marcas;
	}

	public void setMarcas(List<GPSData> marcas) {
		this.marcas = marcas;
	}	
}
