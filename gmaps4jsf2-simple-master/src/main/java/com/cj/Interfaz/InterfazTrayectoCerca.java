package com.cj.Interfaz;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import com.cj.dao.InterfazCercaPolDAO;
import com.cj.dao.InterfazDAO;
import com.cj.geografia.CalculosGeograficos;
import com.cj.pojos.GPSData;
import com.cj.pojos.Usuario;
import com.cj.utils.Constantes;
import com.googlecode.gmaps4jsf.component.map.Map;
import com.googlecode.gmaps4jsf.component.polygon.Polygon;

public class InterfazTrayectoCerca {

	private Map map;
	InterfazDAO interfazDAO=new InterfazDAO();
	private Usuario usuario=this.obtenerUsuario();
	//Mapa
	private HashMap<String,String> vehiculos=interfazDAO.obtenerVehiculosString(this.usuario);
	private String vehiculoSeleccionado=vehiculos.get(0);
	
	private SelectItem[] comandos=interfazDAO.comandosToItems();
	private Character comandoSeleccionado;
	private Boolean inicio=true;
	private GPSData gpsData = interfazDAO.ultimaposicion(vehiculoSeleccionado);
	private List<GPSData> historial= interfazDAO.historialReciente(Constantes.maxResults,vehiculoSeleccionado,new Date());
	InterfazCercaPolDAO interfazCercaDAO = new InterfazCercaPolDAO();
	private HashMap<String, Integer> Cercas = interfazCercaDAO.obtenerCercasString();
	private Integer CercaSeleccionada;
	private List<GPSData> cerca;	
	
	
	
	public Usuario getUsuario() {
		return this.obtenerUsuario();
	}


	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}


	public HashMap<String, Integer> getCercas() {
		return Cercas;
	}


	public void setCercas(HashMap<String, Integer> Cercas) {
		this.Cercas = Cercas;
	}


	public Integer getCercaSeleccionada() {
		return CercaSeleccionada;
	}


	public void setCercaSeleccionada(Integer CercaSeleccionada) {
		this.CercaSeleccionada = CercaSeleccionada;
	}


	public HashMap<String,String> getVehiculos() {
		return vehiculos;
	}


	public void setVehiculos(HashMap<String,String> vehiculos) {
		this.vehiculos = vehiculos;
	}


	public String getVehiculoSeleccionado() {
		return vehiculoSeleccionado;
	}


	public void setVehiculoSeleccionado(String vehiculoSeleccionado) {
		this.vehiculoSeleccionado = vehiculoSeleccionado;
	}


	public SelectItem[] getComandos() {
		return comandos;
	}


	public void setComandos(SelectItem[] comandos) {
		this.comandos = comandos;
	}


	public Character getComandoSeleccionado() {
		return comandoSeleccionado;
	}


	public void setComandoSeleccionado(Character comandoSeleccionado) {
		this.comandoSeleccionado = comandoSeleccionado;
	}


	public GPSData getGpsData() {
		return gpsData;
	}


	public void setGpsData(GPSData gpsData) {
		this.gpsData = gpsData;
	}


	public Map getMap() {
		return map;
	}


	public void setMap(Map map) {
		this.map=map;
		GPSData gps=interfazDAO.ultimaposicion(vehiculoSeleccionado);
		if(gps!=null){
		map.setLatitude(gps.getLatitude().toString());
		map.setLongitude(gps.getLongitude().toString());
		if(inicio){
			List<GPSData> historialReciente=interfazDAO.historialReciente(Constantes.maxResults,vehiculoSeleccionado,new Date());
			if(!this.historial.isEmpty())
	    	this.map= interfazDAO.agregarMarcas(historialReciente, map,vehiculoSeleccionado,true,Constantes.color1,false);
			inicio=false;
//			interfazDAO.eliminarIcon(map);
		}
		}//comprobacion null

	}


	public void refresh(){
		List<GPSData> noLeidos=interfazDAO.addMarksUnreaded(vehiculoSeleccionado, new Date(),Constantes.maxResults);
		
		if(!noLeidos.isEmpty()){
			isInCerca(noLeidos);
			interfazDAO.eliminarIcon(map);	
			setMap(interfazDAO.agregarMarcas(noLeidos, this.getMap(),vehiculoSeleccionado,true,Constantes.color1,false));
				FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("gmap");
//				interfazDAO.cambioCercas(cercas, noLeidos);	
				this.gpsData=noLeidos.get(noLeidos.size()-1);
				FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("infoA");
		}
	}
	
public void isInCerca(List<GPSData> ultimos){
//	List<GPSData> marcasCerca=interfazCercaDAO.obtenerMarcasPorCerca(CercaSeleccionada);
	Polygon pol=interfazCercaDAO.obtenPoligono(getMap());
	if(pol!=null)
	for(GPSData ultimo:ultimos){
		if(!CalculosGeograficos.isInPolygon(cerca, ultimo)){
			pol.setHexFillColor(Constantes.cercaVerde);
		}
	}
	
}
	
	public String pintarCerca() {
		this.cerca=interfazCercaDAO.pintarCerca(CercaSeleccionada, getMap());
		return null;
	}
	
	public String cambiarVehiculo(){
		interfazDAO.actualizarMapa(vehiculoSeleccionado, getMap(),new Date(),Constantes.color1);
		List<GPSData> historialReciente=interfazDAO.historialReciente(Constantes.maxResults,vehiculoSeleccionado,new Date());
//		interfazDAO.agregarCercas(cercas, getMap(),historialReciente);
		return "Realizado";
	}
	public Usuario obtenerUsuario(){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Login login
	    = (Login)facesContext.getApplication()
	      .createValueBinding("#{login}").getValue(facesContext);
		return login.geteUsuario();		
	}	
}
