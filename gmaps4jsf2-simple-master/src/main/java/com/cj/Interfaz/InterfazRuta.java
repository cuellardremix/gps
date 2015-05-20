package com.cj.Interfaz;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import com.cj.dao.InterfazDAO;
import com.cj.dao.InterfazRutaDAO;
import com.cj.pojos.GPSData;
import com.cj.pojos.Ruta;
import com.cj.pojos.Usuario;
import com.cj.utils.Constantes;
import com.googlecode.gmaps4jsf.component.common.Position;
import com.googlecode.gmaps4jsf.component.icon.Icon;
import com.googlecode.gmaps4jsf.component.map.Map;
import com.googlecode.gmaps4jsf.component.marker.Marker;
//import com.parker.leanmatics.dao.InterfazRutaPolDAO;

public class InterfazRuta{
	private Map map;
	InterfazDAO interfazDAO = new InterfazDAO();
	InterfazRutaDAO interfazRutaDAO = new InterfazRutaDAO();
	private Usuario usuario=this.obtenerUsuario();
	private HashMap<String, Integer> Rutas = interfazRutaDAO
			.obtenerRutasString(getUsuario());
	private Integer rutaSeleccionada;
	private Marker inicioRuta;
	private HashMap<String,String> vehiculos = interfazDAO.obtenerVehiculosString(getUsuario());
	private String vehiculoSeleccionado = primerVehiculo();
	private GPSData gpsData;// = interfazDAO.ultimaposicion(vehiculoSeleccionado);
	private Boolean bloqueoVehiculo = false;
	private GPSData inicio, finali;
	private String nuevaRuta;
	private Double anchoNuevo;
	private Date fechaA;
	

	public InterfazRuta(){
		this.inicioRuta=new Marker();
		Icon icon = new Icon();
		icon.setImageURL(Constantes.inicio);
		// System.out.println("Url: "+icon.getImageURL());
		icon.setId("Inicio"
				+ new Double(Math.random() * 1000).toString().replace('.',
						'0'));
		inicioRuta.getChildren().add(icon);
		GPSData t=		interfazDAO.ultimaposicion(vehiculoSeleccionado);
		if(t!=null){
		this.gpsData=t;
		inicioRuta.setLongitude(t.getLongitude().toString());
		inicioRuta.setLatitude(t.getLatitude().toString());
		this.map=this.iniciarMapa();
		this.map.getChildren().add(inicioRuta);
		}

	}
	
	public Usuario getUsuario() {
		return this.obtenerUsuario();
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public String primerVehiculo(){
		if(!this.vehiculos.isEmpty()){
			String t=this.interfazDAO.obtenerVehiculos(getUsuario()).get(0).getVehImei();
			return t;
		}
		return null;
	}
	public String getNuevaRuta() {
		return nuevaRuta;
	}

	public void setNuevaRuta(String nuevaRuta) {
		this.nuevaRuta = nuevaRuta;
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

	public Integer getrutaSeleccionada() {
		return rutaSeleccionada;
	}

	public void setrutaSeleccionada(Integer rutaSeleccionada) {
		this.rutaSeleccionada = rutaSeleccionada;
	}

	public HashMap<String, Integer> getRutas() {
		return Rutas;
	}

	public void setRutas(HashMap<String, Integer> Rutas) {
		this.Rutas = Rutas;
	}

	public String pintarRuta() {
		this.map=this.iniciarMapa();
		interfazRutaDAO.pintarRutaPol(rutaSeleccionada, getMap());
		return null;
	}

	public String cambiarRuta() {
		System.out.println(" ##### " + rutaSeleccionada);
		return null;
	}

	public String iniciarGrabacionRuta() {
		if (inicioRuta != null) {
			fechaA=new Date();
			inicio = new GPSData(new Double(inicioRuta.getLatitude()),
					new Double(inicioRuta.getLongitude()));
			inicio=interfazRutaDAO.guardaMarca(inicio);
			List<GPSData> noLeidos=interfazDAO.addMarksUnreaded(vehiculoSeleccionado,fechaA,Constantes.maxResults);
			interfazDAO.updateReaded(noLeidos);
		} else {
			inicio = interfazDAO.ultimaposicion(vehiculoSeleccionado);
		}

		bloqueoVehiculo = true;
		return null;
	}

	public String detenerGrabacionRuta() {
		finali = interfazDAO.ultimaposicion(vehiculoSeleccionado);
		bloqueoVehiculo = false;
		interfazRutaDAO.generarRutaBase(inicio, finali, rutaSeleccionada,vehiculoSeleccionado);
		pintarRuta();
		return null;
	}

	public String cambiarVehiculo() {
		// interfazDAO.actualizarMapa(vehiculoSeleccionado, getMap());
		gpsData = interfazDAO.ultimaposicion(vehiculoSeleccionado);
		return null;
	}

	public void refresh() {
		if (bloqueoVehiculo) {
			List<GPSData> noLeidos = interfazDAO
					.addMarksUnreaded(vehiculoSeleccionado);
//			interfazDAO.eliminarIcon(map);
			if (!noLeidos.isEmpty()) {
//				setMap(iniciarMapa());
//				getMap().getChildren().add(inicioRuta);
				setMap(interfazDAO.agregarMarcas(noLeidos, this.getMap(),
						vehiculoSeleccionado,true,Constantes.color1,false));
				FacesContext.getCurrentInstance().getPartialViewContext()
						.getRenderIds().add("gmap");
			}
		}
	}

	public void addMarkerHere(ActionEvent actionEvent) {
		if (!bloqueoVehiculo) {
			Map map = (Map) actionEvent.getComponent();
			map.getChildren().clear();
			inicioRuta = new Marker();
			Icon icon = new Icon();
			icon.setImageURL(Constantes.inicio);
			// System.out.println("Url: "+icon.getImageURL());
			icon.setId("Inicio"
					+ new Double(Math.random() * 1000).toString().replace('.',
							'0'));
			inicioRuta.getChildren().add(icon);
			inicioRuta.setLongitude(((Position) map.getValue()).getLongitude());
			inicioRuta.setLatitude(((Position) map.getValue()).getLatitude());

			this.map.getChildren().add(inicioRuta);
		}

	}

	public String agregarNuevaRuta() {
		if (this.nuevaRuta != null && !this.nuevaRuta.equals(""))
			if (this.anchoNuevo != null) {
				Ruta ruta = interfazRutaDAO.nuevaRuta(this.nuevaRuta,
						this.anchoNuevo,getUsuario());
				
				if (ruta.getRutLla() != null) {
					this.Rutas.put(ruta.getRutNom(), ruta.getRutLla());
				}
			}
		return null;
	}

	public String pintarCerca() {
		interfazRutaDAO.pintarRutaPol(rutaSeleccionada, getMap());
		return null;
	}
	public Usuario obtenerUsuario(){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Login login
	    = (Login)facesContext.getApplication()
	      .createValueBinding("#{login}").getValue(facesContext);
		return login.geteUsuario();		
	}
	
	public Map iniciarMapa(){
		Map map=new Map();
		map.setLatitude("19.055663");
		map.setLongitude("-98.154700");
		return map;
	}
}
