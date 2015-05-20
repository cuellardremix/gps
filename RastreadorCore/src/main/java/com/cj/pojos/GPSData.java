package com.cj.pojos;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.cj.geografia.CalculosGeograficos;


@Entity
@Table
public class GPSData implements Comparable<GPSData>{
	
	
	@Id
	@GeneratedValue
	private Integer idRegistro;
	private String imei;
	private String alarm;
	private Date fecha;
	private Boolean valid;
	private Double latitude;
	private Double longitude;
	private Double speed;
	private Double curso;
	private Double altitud;
	private Boolean noLeido;
	
	//datos adicionales(sensores no conectados
	private String io1;
	private String io2;
	private String io3;
	private String io4;
	
	private String evento;
	
	
	@ManyToOne//(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
	Ruta ruta;
	@ManyToOne//(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
	Cerca cerca;
	private Boolean estadoRegistro;
	public GPSData(){}
	
	public String getEvento() {
		return evento;
	}



	public void setEvento(String evento) {
		this.evento = evento;
	}



	public Boolean getEstadoRegistro() {
		return estadoRegistro;
	}


	public void setEstadoRegistro(Boolean estadoRegistro) {
		this.estadoRegistro = estadoRegistro;
	}



	public Cerca getCerca() {
		return cerca;
	}


	public void setCerca(Cerca cerca) {
		this.cerca = cerca;
	}


	public Ruta getRuta() {
		return ruta;
	}


	public void setRuta(Ruta ruta) {
		this.ruta = ruta;
	}


	public Boolean getNoLeido() {
		return noLeido;
	}




	public void setNoLeido(Boolean noLeido) {
		this.noLeido = noLeido;
	}







	public Integer getIdRegistro() {
		return idRegistro;
	}


	public void setIdRegistro(Integer idRegistro) {
		this.idRegistro = idRegistro;
	}





	public GPSData(Double latitude, Double longitude,String imei) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.imei=imei;
	}

	public GPSData(Double latitude, Double longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
	}

public Double gpsH(GPSData g1,GPSData g2){
	return CalculosGeograficos.haversine(g1, g2);
}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getAlarm() {
		return alarm;
	}

	public void setAlarm(String alarm) {
		this.alarm = alarm;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Boolean getValid() {
		return valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
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

	public Double getSpeed() {
		return speed;
	}

	public void setSpeed(Double speed) {
		this.speed = speed;
	}

	public Double getCurso() {
		return curso;
	}

	public void setCurso(Double curso) {
		this.curso = curso;
	}

	public Double getAltitud() {
		return altitud;
	}

	public void setAltitud(Double altitud) {
		this.altitud = altitud;
	}

	public String getIo1() {
		return io1;
	}

	public void setIo1(String io1) {
		this.io1 = io1;
	}

	public String getIo2() {
		return io2;
	}

	public void setIo2(String io2) {
		this.io2 = io2;
	}

	public String getIo3() {
		return io3;
	}

	public void setIo3(String io3) {
		this.io3 = io3;
	}

	public String getIo4() {
		return io4;
	}

	public void setIo4(String io4) {
		this.io4 = io4;
	}


	public int compareTo(GPSData arg0) {
//		return arg0.getIdRegistro().compareTo(this.idRegistro);
		return this.idRegistro.compareTo(arg0.getIdRegistro());
	}
}
