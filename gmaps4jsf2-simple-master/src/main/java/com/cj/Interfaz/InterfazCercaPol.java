package com.cj.Interfaz;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import com.cj.dao.InterfazCercaPolDAO;
import com.cj.dao.InterfazDAO;
import com.cj.pojos.Cerca;
import com.cj.pojos.GPSData;
import com.cj.pojos.Usuario;
import com.cj.pojos.Vehiculo;
import com.cj.utils.Constantes;
import com.googlecode.gmaps4jsf.component.common.Position;
import com.googlecode.gmaps4jsf.component.icon.Icon;
import com.googlecode.gmaps4jsf.component.map.Map;
import com.googlecode.gmaps4jsf.component.marker.Marker;

public class InterfazCercaPol {
	private Map map;
	InterfazDAO interfazDAO = new InterfazDAO();
	InterfazCercaPolDAO interfazCercaDAO = new InterfazCercaPolDAO();
	private HashMap<String, Integer> Cercas = interfazCercaDAO
			.obtenerCercasString();
	private Integer CercaSeleccionada;
	private Marker inicioCerca;
	private Usuario usuario=this.obtenerUsuario();
	private HashMap<String,String> vehiculos = interfazDAO.obtenerVehiculosString(this.usuario);
	private String vehiculoSeleccionado = vehiculos.get(0);
	private GPSData gpsData;// = interfazDAO.ultimaposicion(vehiculoSeleccionado);
	private Boolean bloqueoVehiculo = false;
	private GPSData inicio, finali;
	private String nuevaCerca;
	private Double anchoNuevo;
	private Date fechaA;
	private List<GPSData> puntos;
	
	
	@PostConstruct
	public void init(){
		puntos=new ArrayList<GPSData>();
		this.gpsData=interfazDAO.ultimaposicion(vehiculoSeleccionado);
		if(gpsData==null){
			gpsData=new GPSData(19.055663,-98.154700);
		}
	}

	
	public Usuario getUsuario() {
		return this.obtenerUsuario();
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getNuevaCerca() {
		return nuevaCerca;
	}

	public void setNuevaCerca(String nuevaCerca) {
		this.nuevaCerca = nuevaCerca;
	}

	public Double getAnchoNuevo() {
		return anchoNuevo;
	}

	public void setAnchoNuevo(Double anchoNuevo) {
		this.anchoNuevo = anchoNuevo;
	}

	public GPSData getGpsData() {
		return gpsData;
	}

	public void setGpsData(GPSData gpsData) {
		this.gpsData = gpsData;
	}

	public Boolean getBloqueoVehiculo() {
		return bloqueoVehiculo;
	}

	public void setBloqueoVehiculo(Boolean bloqueoVehiculo) {
		this.bloqueoVehiculo = bloqueoVehiculo;
	}

	public Map getMap() {
		return map;
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

	public void setMap(Map map) {
		this.map = map;
	}

	public Integer getCercaSeleccionada() {
		return CercaSeleccionada;
	}

	public void setCercaSeleccionada(Integer CercaSeleccionada) {
		this.CercaSeleccionada = CercaSeleccionada;
	}

	public HashMap<String, Integer> getCercas() {
		return Cercas;
	}

	public void setCercas(HashMap<String, Integer> Cercas) {
		this.Cercas = Cercas;
	}

	public String pintarCerca() {
//		interfazCercaDAO.pintarCerca(CercaSeleccionada, getMap());
		interfazCercaDAO.pintarCercaSimple(puntos, getMap());
		return null;
	}

	public String cambiarCerca() {
		System.out.println(" ##### " + CercaSeleccionada);
		return null;
	}

	public String iniciarGrabacionCerca() {
		if(inicioCerca!=null){
			fechaA=new Date();
			inicio=new GPSData(new Double(inicioCerca.getLatitude()),new Double(inicioCerca.getLongitude()));
				inicio=interfazCercaDAO.guardaMarca(inicio);
				List<GPSData> noLeidos=interfazDAO.addMarksUnreaded(vehiculoSeleccionado,fechaA,Constantes.maxResults);
				interfazDAO.updateReaded(noLeidos);
		}else{
			inicio = interfazDAO.ultimaposicion(vehiculoSeleccionado);	
		}
		
		bloqueoVehiculo = true;
		return null;
	}

	public String detenerGrabacionCerca() {
		if(inicio!=null){
		finali = interfazDAO.ultimaposicion(vehiculoSeleccionado);
		bloqueoVehiculo = false;
		interfazCercaDAO.generarCercaBase(inicio, finali, CercaSeleccionada,vehiculoSeleccionado);
		pintarCerca();
		}
		return null;
	}

	public String cambiarVehiculo() {
//		interfazDAO.actualizarMapa(vehiculoSeleccionado, getMap());
		gpsData=interfazDAO.ultimaposicion(vehiculoSeleccionado);
		return null;
	}

	public void refresh() {
		if (bloqueoVehiculo) {
			List<GPSData> noLeidos = interfazDAO
					.addMarksUnreaded(vehiculoSeleccionado);
			if (!noLeidos.isEmpty()) {
//				setMap(iniciarMapa());
//				getMap().getChildren().add(inicioCerca);
				setMap(interfazDAO.agregarMarcas(noLeidos, this.getMap(),
						vehiculoSeleccionado,true,Constantes.color3,false));
				FacesContext.getCurrentInstance().getPartialViewContext()
						.getRenderIds().add("gmap");
			}
		}
	}
	

	public void manejar(ActionEvent actionEvent) {

			Map map = (Map) actionEvent.getComponent();
			System.out.println(map.getLatitude()+" "+map.getLongitude());

	}
	
    public void listen(){
        FacesContext context = FacesContext.getCurrentInstance();
        java.util.Map<String,String> params = context.getExternalContext().getRequestParameterMap();
        puntos.add(new GPSData(Double.parseDouble(params.get("latitud")),Double.parseDouble(params.get("longitud"))));
    }
	
public String agregarNuevaCerca(){
	if(this.nuevaCerca!=null && !this.nuevaCerca.equals("") && !this.puntos.isEmpty()){
		
	}
		if(this.anchoNuevo!=null ){
			Cerca cerca=interfazCercaDAO.nuevaCerca(this.nuevaCerca, this.anchoNuevo,
					null,this.puntos,this.getUsuario());
			if(cerca.getGeoLla()!=null){
				this.Cercas.put(cerca.getGeoNom(), cerca.getGeoLla());
			}
		}
	return null;
}

public Usuario obtenerUsuario(){
	FacesContext facesContext = FacesContext.getCurrentInstance();
	Login login
    = (Login)facesContext.getApplication()
      .createValueBinding("#{login}").getValue(facesContext);
	return login.geteUsuario();		
}

public String iniciar(){
	this.iniciarMapa();
	puntos.clear();
	return "";
}
public Map iniciarMapa(){
	Map map=new Map();
	map.setLatitude("19.055663");
	map.setLongitude("-98.154700");
	return map;
}

}
