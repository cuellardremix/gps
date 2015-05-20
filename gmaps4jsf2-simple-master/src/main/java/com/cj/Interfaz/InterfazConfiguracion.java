package com.cj.Interfaz;

import com.cj.dao.ConfiguracionDAO;
import com.cj.pojos.Configuracion;

public class InterfazConfiguracion {
	private Integer segundos;
	private ConfiguracionDAO configDAO;
	
	public InterfazConfiguracion(){
		configDAO=new ConfiguracionDAO();
		Configuracion c=configDAO.obtenerRetraso();
		if(c!=null)
		this.segundos=c.getRetraso();
	}
	
	public Integer getSegundos() {
		return segundos;
	}

	public void setSegundos(Integer segundos) {
		this.segundos = segundos;
	}
	
	public String guardarRetraso(){
		Configuracion c=configDAO.obtenerRetraso();
		if(c==null){
			Configuracion c1=new Configuracion();
			c1.setRetraso(getSegundos());
			this.configDAO.guardarRetraso(c1);
		}else{
			c.setRetraso(getSegundos());
			this.configDAO.guardarRetraso(c);	
		}
		return "";
	}
}
