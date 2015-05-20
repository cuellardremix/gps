package com.cj.Interfaz;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;

import org.hibernate.Query;
import org.hibernate.Session;

import com.cj.configuracion.HibernateUtil;
import com.cj.dao.InterfazDAO;
import com.cj.geografia.CalculosGeograficos;
import com.cj.pojos.Cerca;
import com.cj.pojos.GPSData;
import com.cj.pojos.Usuario;
import com.cj.utils.Constantes;
import com.googlecode.gmaps4jsf.component.circle.Circle;
import com.googlecode.gmaps4jsf.component.map.Map;

public class InterfazCercas {

	public static final Double kmEnGrados=1/((2*6378*3.1416)/360);
	private Map map;
	private Cerca cerca;
	private Circle circulo;
	private InterfazDAO interfazDAO=new InterfazDAO();	
	private Boolean inicio=true;
	private Usuario usuario=getUsuario();
	private HashMap<String,String> vehiculos;
	private String vehiculoSeleccionado;

	@PostConstruct
public void init(){
	vehiculos=interfazDAO.obtenerVehiculosString(this.usuario);
	vehiculoSeleccionado=this.primerVehiculo();
}
	public Usuario getUsuario() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Login login
	    = (Login)facesContext.getApplication()
	      .createValueBinding("#{login}").getValue(facesContext);
		this.usuario=login.geteUsuario();
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String primerVehiculo(){
		if(!vehiculos.isEmpty())
			return vehiculos.get(0);
		return null;
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

	public Circle getCirculo() {
		return circulo;
	}

	public void setCirculo(Circle circulo) {
		this.circulo = circulo;
	}

	public Cerca getCerca() {
		return cerca;
	}

	public void setCerca(Cerca cerca) {
		this.cerca = cerca;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
	this.map=map;
	GPSData gps=interfazDAO.ultimaposicion(vehiculoSeleccionado);
	map.setLatitude(gps.getLatitude().toString());
	map.setLongitude(gps.getLongitude().toString());
	if(inicio){
		this.map.getChildren().clear();
		List<GPSData> historial=interfazDAO.historialReciente(Constantes.maxResults,vehiculoSeleccionado,new Date());
		if(!historial.isEmpty())
    	this.map= interfazDAO.agregarMarcas(historial, map,vehiculoSeleccionado,true,Constantes.color1,false);
		this.map=this.agregarCerca(map);
		inicio=false;
	}
    }
	
	public Map agregarCerca(Map map){
		cerca=this.obtenerCerca();
		
			circulo=new Circle();
			circulo.setLatitude(cerca.getLatitude().toString());
			circulo.setLongitude(cerca.getLongitude().toString());
			circulo.setUnit("KM");
//			circulo.setRaduis(cerca.getRadio().toString());
			map.getChildren().add(circulo);
			map.setLatitude(circulo.getLatitude());
			map.setLongitude(circulo.getLongitude());
		
				return map;
	}
	
	public void refresh(){
		
		if(Interfaz.refrescar){
			
			List<GPSData> noLeidos=interfazDAO.addMarksUnreaded(vehiculoSeleccionado,new Date(),Constantes.maxResults);
			if(!noLeidos.isEmpty()){
				if(noLeidos.size()==1){
//					setMap(interfazDAO.addMark(noLeidos.get(0), this.getMap(), vehiculoSeleccionado));
				}else{
					setMap(interfazDAO.agregarMarcas(noLeidos, this.getMap(),vehiculoSeleccionado,true,Constantes.color1,false));
				}
				GPSData ultima=interfazDAO.ultimaposicion(vehiculoSeleccionado);
				Cerca cerca=this.getCerca();
				Double distancia=CalculosGeograficos.haversine(ultima, new GPSData(cerca.getLatitude(),cerca.getLongitude()));
				
//				if(distancia>cerca.getRadio()){
//				this.getCirculo().setHexStrokeColor("#cc0033");
//				}else{
//				this.getCirculo().setHexStrokeColor("#ff0033");	
//				}
				FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("gmapCerca");
					Interfaz.refrescar=false;
			}	
		}
	}
	
	private Cerca obtenerCerca() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from Cerca order by id");
		Cerca c=(Cerca) query.uniqueResult();
		session.close();
		return c;
	}

	public Map agregarCercas(Map map){
		List<Cerca> cercas=this.obtenerCercas();
		for(Cerca cerca:cercas){
			Circle c=new Circle();
			c.setLatitude(cerca.getLatitude().toString());
			c.setLongitude(cerca.getLongitude().toString());
			c.setUnit("KM");
//			c.setRaduis(cerca.getRadio().toString());
			map.getChildren().add(c);
			map.setLatitude(c.getLatitude());
			map.setLongitude(c.getLongitude());
		}
				return map;
	}
	
	public List<Cerca> obtenerCercas(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from Cerca order by id");
//		query.setMaxResults(1);
		List<Cerca> c=(List<Cerca>)query.list();
		session.close();
//		return (GPSData) query.uniqueResult();
		return c;
	}
	
}
