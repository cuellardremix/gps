package com.cj.dao;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.hibernate.Query;
import org.hibernate.Session;

import com.cj.catalogosDAO.AdminCercasDAO;
import com.cj.catalogosDAO.AdminClientesDAO;
import com.cj.catalogosDAO.AdminTurnosDAO;
import com.cj.configuracion.HibernateUtil;
import com.cj.geografia.CalculosGeograficos;
import com.cj.pojos.Cerca;
import com.cj.pojos.Cliente;
import com.cj.pojos.ControlRuta;
import com.cj.pojos.GPSData;
import com.cj.pojos.Ruta;
import com.cj.pojos.Usuario;
import com.cj.pojos.Vehiculo;
import com.cj.utils.Constantes;
import com.googlecode.gmaps4jsf.component.circle.Circle;
import com.googlecode.gmaps4jsf.component.icon.Icon;
import com.googlecode.gmaps4jsf.component.map.Map;
import com.googlecode.gmaps4jsf.component.marker.Marker;
import com.googlecode.gmaps4jsf.component.point.Point;
import com.googlecode.gmaps4jsf.component.polyline.Polyline;
import com.googlecode.gmaps4jsf.component.window.HTMLInformationWindow;

public class InterfazDAO {

//	public static final Integer resultMaxperConsult=3000; 
	private GPSData gpsData;
	private InterfazMonitoreoGeneralDAO interfazMGDAO=new InterfazMonitoreoGeneralDAO();
	private InterfazRutaDAO interfazRutaDAO = new InterfazRutaDAO();
	private AdminCercasDAO cercasDAO=new AdminCercasDAO();
	private AdminTurnosDAO turnosDAO=new AdminTurnosDAO();	
	
	public List<GPSData> eliminarRegistros(List<GPSData> marcas,Double velVehi){
		List<GPSData> r=new ArrayList<GPSData>();
		boolean speed=true;
		boolean sos=true;
		boolean encendido=true;
		boolean apagado=true;
		boolean detenido=true;
		boolean reActivado=true;
		boolean puerta=true;
		boolean fueraRuta=true;
		boolean fueraCerca=true;
		
		
		for(GPSData dato:marcas){
			if(speed)
			if (dato.getSpeed()>=velVehi) {
				r.add(dato);
				speed=false;
			}
			if(sos)
			if (dato.getAlarm().equals(Constantes.help)) {
				r.add(dato);
				sos=false;
			}
			if(encendido)
			if(dato.getAlarm().equals(Constantes.encendido)){
				r.add(dato);
				encendido=false;
			}
			if(apagado)
			if(dato.getAlarm().equals(Constantes.apagado)){
				r.add(dato);
				apagado=false;
			}
			if(detenido)
			if (dato.getAlarm().equals(Constantes.detenido)) {
				r.add(dato);
				detenido=false;
			}
			if(reActivado)
			if (dato.getAlarm().equals(Constantes.reActivado)) {
				r.add(dato);
				reActivado=false;
			}
			if(puerta)
			if (dato.getAlarm().equals(Constantes.alarmaPuerta)) {
				r.add(dato);
				puerta=false;
			}

			if(dato.getEvento()!=null)
				if(fueraRuta)
			if (dato.getEvento().equals(Constantes.fueraDeRuta)) {
				r.add(dato);
				fueraRuta=false;
			}
			if(dato.getEvento()!=null)
				if(fueraCerca)
			if (dato.getEvento().equals(Constantes.fueraDeCerca)) {
				r.add(dato);
				fueraCerca=false;
			}
		}
		return r;
	}
	
	public void agregarAlertas(List<GPSData> marcas,Map map, String imei,boolean isReporte){
		Vehiculo v=this.obtenerVehiculo(imei);
		if(!isReporte)
		marcas=this.eliminarRegistros(marcas, v.getVehVeM());
		List<Marker> marcasA=new ArrayList<Marker>();
		int i=0;
		for(GPSData dato:marcas){
			if(procesarMarca(dato,v.getVehVeM())){
	        	Marker mark = new Marker();
	        	Icon icon=new Icon();
				icon.setId("Icon"+dato.getIdRegistro()+new Double(Math.random()*1000).toString().replace('.', '0'));
				icon.setImageURL(this.procesarColor(dato,v.getVehVeM()));
				mark.getChildren().add(icon);
//				System.out.println(icon.getImageURL()+"##$$$#");
	        	mark.setId("Marca"+dato.getIdRegistro()+new Double(Math.random()*1000).toString().replace('.', '0'));
	        	mark.setLatitude(dato.getLatitude().toString());
	        	mark.setLongitude(dato.getLongitude().toString());
	        	HTMLInformationWindow h=new HTMLInformationWindow();
	        	String pattern="####.##";
	        	DecimalFormat df=new DecimalFormat(pattern);
	        	if(Constantes.help.equals(dato.getAlarm())){
	        		h.setHtmlText(df.format(dato.getSpeed()));
	        	}else{
	        		h.setHtmlText("V:"+df.format(dato.getSpeed()));
	        	}
	        	h.setId("Html"+dato.getIdRegistro()+new Double(Math.random()*1000).toString().replace('.', '0'));
	        	mark.getChildren().add(h);
	        		marcasA.add(mark);
	        		if(isReporte){
//	        			HTMLInformationWindow h1=new HTMLInformationWindow();
//	        			h1.setHtmlText(i+"");
//	        			h1.setLatitude(dato.getLatitude().toString());
//	        			h1.setLongitude(dato.getLongitude().toString());
//	        			map.getChildren().add(h1);
//	        			i++;
	        		}
	        }
		}
		map.getChildren().addAll(marcasA);
        
	}
	
	public Map agregarMarcas(List<GPSData> marcas, Map map,String imei, Boolean alertas,String color,
			Boolean isReporte){
		List<Marker> marcasA=new ArrayList<Marker>();
		Polyline p=new Polyline();
		p.setLineWidth("2");
		p.setGeodesic("true");
		
		gpsData=this.ultimaposicionLeida(imei);
		if(gpsData!=null){
		p.setId("Poli"+gpsData.getIdRegistro()+new Double(Math.random()*1000).toString().replace('.', '0'));
		}
		if(marcas!=null)
		for(GPSData dato:marcas){
	        Point punto=new Point();
	        punto.setId("Punto"+dato.getIdRegistro()+new Double(Math.random()*1000).toString().replace('.', '0'));

			punto.setLatitude(dato.getLatitude().toString());
	        punto.setLongitude(dato.getLongitude().toString());
	        p.getChildren().add(punto);
		}
		if(!marcas.isEmpty()){
//			gpsData=marcas.get(marcas.size()-1);	
			gpsData=marcas.get(0);
			Marker mark = new Marker();
        	Icon icon=new Icon();
			icon.setId("Icon"+gpsData.getIdRegistro()+new Double(Math.random()*1000).toString().replace('.', '0'));
			icon.setImageURL(this.curso(gpsData));
			mark.getChildren().add(icon);
        	System.out.println(icon.getImageURL()+"##$$$#");
        	mark.setId("Marca"+"blue"+gpsData.getIdRegistro()+new Double(Math.random()*1000).toString().replace('.', '0'));
        	mark.setLatitude(gpsData.getLatitude().toString());
        	mark.setLongitude(gpsData.getLongitude().toString());
        	HTMLInformationWindow h=new HTMLInformationWindow();
        	Vehiculo v=this.obtenerVehiculo(imei);
        		h.setHtmlText(v.getVehMar()+" "+v.getVehMod()+" "+gpsData.getSpeed());
        		String hex=Integer.toHexString(v.getVehLla());
        		p.setHexaColor(color);
        	h.setId("Html"+gpsData.getIdRegistro()+new Double(Math.random()*1000).toString().replace('.', '0'));
        	mark.getChildren().add(h);
//        	mark.setShowInformationEvent("open");
        	marcasA.add(mark);
        	
        	String pattern="####.##";
        	DecimalFormat df=new DecimalFormat(pattern);
        	HTMLInformationWindow h1=new HTMLInformationWindow();
        	gpsData.setSpeed(new Double(df.format(gpsData.getSpeed())));
        	h1.setHtmlText(v.getVehMar()+" "+v.getVehMod()+" "+gpsData.getSpeed());
        	h1.setId("Html"+gpsData.getIdRegistro()+new Double(Math.random()*1000).toString().replace('.', '0'));
        	h1.setLatitude(gpsData.getLatitude().toString());
        	h1.setLongitude(gpsData.getLongitude().toString());
        	map.getChildren().add(h1);
		}
		if(alertas)
		this.agregarAlertas(marcas, map, imei,isReporte);
//		this.agregarAlertas(this.deteccion(marcas), map, imei);
		map.getChildren().addAll(marcasA);
		map.getChildren().add(p);
		
		GPSData gps=ultimaposicion(imei);
//	    map.setLatitude(gps.getLatitude().toString());
//        map.setLongitude(gps.getLongitude().toString());
        return map;
	}
	
	public String curso(GPSData gps){
//		if(gps.getCurso()==0.0){
//			return Constantes.dir0;
//		}
//		if(gps.getCurso()>0.0 && gps.getCurso()<=45.0){
//			return Constantes.dir45;
//		}
//		if(gps.getCurso()>45.0 && gps.getCurso()<=90.0){
//			return Constantes.dir90;
//		}
//		if(gps.getCurso()>90.0 && gps.getCurso()<=135.0){
//			return Constantes.dir135;
//		}
//		if(gps.getCurso()>135.0 && gps.getCurso()<=180.0){
//			return Constantes.dir180;
//		}
//		if(gps.getCurso()>180.0 && gps.getCurso()<=225.0){
//			return Constantes.dir225;
//		}
//		if(gps.getCurso()>225.0 && gps.getCurso()<=270.0){
//			return Constantes.dir270;
//		}
//		if(gps.getCurso()>270.0 && gps.getCurso()<=315.0){
//			return Constantes.dir315;
//		}
//		if(gps.getCurso()>315){
//			return Constantes.dir315;
//		}
		return Constantes.bluePoint;
	}
	public Boolean procesarMarca(GPSData dato, Double velVehi){

		if (dato.getSpeed()>=velVehi) {
			return true;
		}
		if (dato.getAlarm().equals(Constantes.help)) {
			return true;
		}
		if(dato.getAlarm().equals(Constantes.encendido)){
			return true;
		}
		if(dato.getAlarm().equals(Constantes.apagado)){
			return true;
		}
		if (dato.getAlarm().equals(Constantes.detenido)) {
			return true;
		}
		if (dato.getAlarm().equals(Constantes.reActivado)) {
			return true;
		}
		if (dato.getAlarm().equals(Constantes.alarmaPuerta)) {
			return true;
		}
//		if (dato.getSpeed() == 0.0) {
//			return true;
//		}
		if(dato.getEvento()!=null)
		if (dato.getEvento().equals(Constantes.fueraDeRuta)) {
			return true;
		}
		if(dato.getEvento()!=null)
		if (dato.getEvento().equals(Constantes.fueraDeCerca)) {
			return true;
		}
		return false;
	}
	
	public void estanEnCerca(List<GPSData> marcas,List<Circle> cercas){
		for(GPSData marca:marcas){
			for(Circle cerca:cercas){
				Double distancia=CalculosGeograficos.haversine(marca, new GPSData(Double.parseDouble(cerca.getLatitude()),Double.parseDouble(cerca.getLongitude())));
				if(distancia>10){
					break;
				}else{
					if(distancia<=Double.parseDouble(cerca.getRaduis())){
						cerca.setHexFillColor(Constantes.cercaVerde);
					}
				}
			}
		}

	}
	
	public void estaEnCerca(GPSData marca, List<Circle> cercas){
		
			for(Circle cerca:cercas){
				Double distancia=CalculosGeograficos.haversine(marca, new GPSData(Double.parseDouble(cerca.getLatitude()),Double.parseDouble(cerca.getLongitude())));
				if(distancia>10){
					break;
				}else{
					if(distancia<=Double.parseDouble(cerca.getRaduis())){
						cerca.setHexFillColor(Constantes.cercaVerde);
					}
				}
			}
		
	}
	public String procesarColor(GPSData data, Double velVeh){
		if(data.getAlarm()!=null){
		if(data.getSpeed()>=velVeh){
			return Constantes.redMarker;
		}
		if(data.getAlarm().equals(Constantes.help)){
			return Constantes.sos;
		}
		if(data.getAlarm().equals(Constantes.detenido)){
			return Constantes.detenidoI;
		}
		if(data.getAlarm().equals(Constantes.reActivado)){
			return Constantes.continuar;
		}
		if(data.getAlarm().equals(Constantes.encendido)){
			return Constantes.encendidoIc;
		}
		if(data.getAlarm().equals(Constantes.apagado)){
			return Constantes.apagadoIc;
		}
		if(data.getAlarm().equals(Constantes.alarmaPuerta)){
			return Constantes.puertaAl;
		}
		}
		if(data.getEvento()!=null){
		if(data.getEvento().equals(Constantes.fueraDeRuta) ){
			return Constantes.fueraRutaIc;
		}
		if(data.getEvento().equals(Constantes.fueraDeCerca)){
			return Constantes.fueraCercaIc;
		}
		}
//		if(data.getSpeed()==0.0){
//			return Constantes.greenMarker;
//		}else{
//			return Constantes.blueMarker;
//		}
		return null;
	}
	
//	public Map addMark(GPSData data, Map map,String imei){
//		GPSData penultima=this.penultimaposicion(imei);
//		GPSData gpsData=this.ultimaposicionLeida(imei);
//		Polyline p=new Polyline();
//		p.setLineWidth("2");
//		Marker mark = new Marker();
//		mark.setId("Marca"+gpsData.getIdRegistro().toString());
//		Point punto1=new Point();
////		p.setId("Poly"+gpsData.getIdRegistro());
//		Point punto2=new Point();
//		punto2.setId("Punto"+gpsData.getIdRegistro());
//        mark.setLatitude(gpsData.getLatitude().toString());
//        punto1.setLatitude(data.getLatitude().toString());
//        punto1.setId("Punto"+data.getIdRegistro());
//        mark.setLongitude(gpsData.getLongitude().toString());
//        punto1.setLongitude(data.getLongitude().toString());
//        p.getChildren().add(punto1);
//        punto2.setLatitude(penultima.getLatitude().toString());
//        punto2.setLongitude(penultima.getLongitude().toString());
//        p.getChildren().add(punto2);
//        
////        mark.setDraggable("true");
//        
//        map.getChildren().add(mark);
//        map.getChildren().add(p);
//        return map;
//	}
	
	public List<Vehiculo> obtenerVehiculos(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from Vehiculo v where  v.estadoRegistro=true order by v.id");
		
		List<Vehiculo> v=query.list();
		session.close();
		return v;
	}
	
	public List<Vehiculo> obtenerVehiculos(List<String> imeis){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from Vehiculo v where v.vehImei IN (:imeis) AND v.estadoRegistro=true order by v.id");
		query.setParameterList("imeis", imeis);
		List<Vehiculo> v=query.list();
		session.close();
		return v;
	}
	
	public List<Vehiculo> obtenerVehiculos(Usuario usuario){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from Vehiculo v where v.usuario.id=:idUsuario AND v.estadoRegistro=true order by v.id");
		query.setParameter("idUsuario", usuario.getUsuLla());
		List<Vehiculo> v=query.list();
		session.close();
		List<Vehiculo>  vs;
		if(usuario.getPerfil().getPerLla()==Constantes.perfilCliente){
			vs=usuario.getCliente().getVehiculos();
			

		}else{
			vs=v;
		}

		return vs;
	}

	public HashMap<String, String> obtenerVehiculosString(Usuario usuario){
		List<Vehiculo>  vs;
if(usuario.getPerfil().getPerLla()==Constantes.perfilCliente){
	vs=usuario.getCliente().getVehiculos();

}else{
	vs=this.obtenerVehiculos(usuario);
}
		HashMap<String, String> nombres=new HashMap<String,String>();
		for(Vehiculo v:vs){
			nombres.put(v.getVehMod()+" "+v.getVehMar(), v.getVehImei());
		}
		return nombres;
	}

	public List<GPSData> leerMarcasNoLeidas(String imei){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from GPSData g WHERE g.imei=:imei AND g.noLeido=true order by g.id");
		query.setParameter("imei", imei);
		List<GPSData> l=query.list();
		
		session.close();
		return l;
	}


	public List<GPSData> addMarksUnreaded(String imei,Date fecha,Integer maxResults){
		Session session = HibernateUtil.getSessionFactory().openSession();
//		Query query = session.createQuery("from GPSData g WHERE g.imei=:imei AND g.noLeido=true order by g.id");
		Query query = session.createQuery("from GPSData g WHERE g.imei=:imei AND g.fecha<=:fecha order by g.id DESC");
		query.setParameter("imei", imei);
		query.setParameter("fecha", fecha);
		query.setMaxResults(maxResults);
		List<GPSData> l=query.list();
		
		if(!l.isEmpty())
			updateReaded(l);
		
		session.close();
		return l;
	}
	
	public List<GPSData> addMarksUnreaded(String imei){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from GPSData g WHERE g.imei=:imei AND g.noLeido=true order by g.id");
//		Query query = session.createQuery("from GPSData g WHERE g.imei=:imei AND g.fecha<=:fecha order by g.id DESC");
		query.setParameter("imei", imei);
//		query.setParameter("fecha", fecha);
//		query.setMaxResults(Constantes.maxResults);
		List<GPSData> l=query.list();
		
		if(!l.isEmpty())
			updateReaded(l);
		
		session.close();
		return l;
	}
	
	public void updateReaded(List<GPSData> marcas){
		Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
		for(GPSData marca:marcas){
			marca.setNoLeido(false);
			session.saveOrUpdate(marca);
		}
		session.getTransaction().commit();
		session.close();
	}
	
	public List<GPSData> historialReciente(Integer numResultados,String imei, Date fecha) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from GPSData WHERE imei=:imei AND noLeido=false AND fecha>=:fecha order by id DESC");
//		Query query = session.createQuery("from GPSData g WHERE g.imei=:imei AND g.noLeido=0 AND g.speed<>0.0 order by g.id");
		query.setParameter("imei", imei);
		query.setParameter("fecha", fecha);
		query.setMaxResults(numResultados);
		List<GPSData> l=query.list();
//		if(!l.isEmpty()){
//			this.updateReaded(l);
//		}
		session.close();
		return l;
	}

	public GPSData penultimaposicion(String imei) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from GPSData g WHERE g.imei=:imei AND g.noLeido=true order by g.id DESC");
		query.setMaxResults(2);
		query.setParameter("imei", imei);
		List<GPSData> l=query.list();
		session.close();
		if(l.isEmpty()){
		return this.ultimaposicion(imei);
		}else{
			return l.get(1);
		}
	}

//	public List<GPSData> ultimasNPosiciones() {
//		Session session = HibernateUtil.getSessionFactory().openSession();
//		Query query = session.createQuery("from GPSData g order by g.id DESC");
//		query.setMaxResults(Interfaz.conteoActualizados);
//
////		if(Interfaz.conteoActualizados==0)
////			return null;
//		List<GPSData> l=query.list();
//		session.close();
//		Interfaz.conteoActualizados=0;
//		if(l.isEmpty()){
//		return null;
//		}else{
//			return l;
//		}
//	}

	
	
	public HashMap<String,GPSData> ultimoEncendidoApagado(List<String> imeis) {
		List<GPSData> rets=new ArrayList<GPSData>();
		
		for(String imei: imeis){
			Session session = HibernateUtil.getSessionFactory().openSession();
			Query query = session.createQuery("from GPSData g WHERE g.imei =:imei AND (g.alarm=:accon OR g.alarm=:accoff) order by g.id DESC");
			query.setParameter("imei", imei);
			query.setParameter("accon", Constantes.encendido);
			query.setParameter("accoff", Constantes.apagado);
			query.setMaxResults(1);
			GPSData gps=(GPSData) query.uniqueResult();
			session.close();
			rets.add(gps);
		}
		
		HashMap<String,GPSData> r=new HashMap<String,GPSData>();
		for(GPSData g:rets){
			if(g!=null)
			r.put(g.getImei(), g);
		}
		return r;
	}
	
	public List<GPSData> ultimasPosiciones(List<String> vehiculos){
		List<GPSData> vReturn=new ArrayList<GPSData>();
		for(String v:vehiculos){
			GPSData gps=this.ultimaposicion(v);
			vReturn.add(gps);
		}
		return vReturn;
	}
	
	public GPSData ultimaposicion(String imei) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from GPSData g WHERE g.imei=:imei order by g.id DESC");
		query.setParameter("imei", imei);
		query.setMaxResults(1);
		GPSData gps=(GPSData) query.uniqueResult();
		session.close();
		return gps;
	}
	
	public GPSData ultimaposicionLeida(String imei) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from GPSData g WHERE g.imei=:imei AND g.noLeido=false order by g.id DESC");
		query.setParameter("imei", imei);
		query.setMaxResults(1);
		GPSData gps=(GPSData) query.uniqueResult();
		session.close();
		return gps;
	}
	
	public SelectItem[] comandosToItems(){
		SelectItem[] comandos={
//		new SelectItem(Constantes.leerPosicion,"Leer Posicion Actual"),
		new SelectItem(Constantes.activar, "Habilitar Vehiculo"),
		new SelectItem(Constantes.detener, "Inhabilitar vehiculo")
		};
		return comandos;
	}
	
	public void updateComandoActual(String imei,Character comando){
		Session session = HibernateUtil.getSessionFactory().openSession();
		System.out.println(comando+"**");
        session.beginTransaction();
		Vehiculo vehiculo=this.obtenerVehiculo(imei);
		vehiculo.setVehComAct(comando);
			session.saveOrUpdate(vehiculo);
		
		session.getTransaction().commit();
		session.close();
	}
	
	public Vehiculo obtenerVehiculo(String imei){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from Vehiculo v WHERE v.vehImei=:imei AND v.estadoRegistro=true");
		query.setParameter("imei", imei);
		Vehiculo v=(Vehiculo)query.uniqueResult();
		session.close();
		return v;
	}
	
	public List<Circle> transformarCercas(List<Cerca> cercas){
		List<Circle> circulos=new ArrayList<Circle>();
		for(Cerca cerca:cercas){
			Circle c=new Circle();
			c.setLatitude(cerca.getLatitude().toString());
			c.setId("Cerca"+cerca.getGeoLla()+new Double(Math.random()*1000).toString().replace('.', '0'));
			c.setLongitude(cerca.getLongitude().toString());
			c.setUnit("KM");
//			c.setRaduis(cerca.getRadio().toString());
//			c.setId("Cerca"+cerca.getIdCerca());
			circulos.add(c);
		}
		return circulos;
	}
	public void agregarCercas(List<Circle> cercas, Map map,List<GPSData> ultimos){
		for(Circle cerca:cercas){
			map.getChildren().add(cerca);
		}
		cambioCercas(cercas, ultimos);
	}
	
	public void cambioCercas(List<Circle> cercas,List<GPSData> ultimos){
		for(Circle cerca:cercas){
			for(GPSData data:ultimos){
				Double distancia=CalculosGeograficos.haversine(new GPSData(Double.parseDouble(cerca.getLatitude()),Double.parseDouble(cerca.getLongitude())), data);
//				System.out.println("Distancia: "+distancia+"Radio: "+cerca.getRaduis());
				if(distancia>10.0){
					break;
				}
				else if(Double.parseDouble(cerca.getRaduis())>distancia){
					cerca.setHexStrokeColor("#cc0033");
				}
					
			}
		}
	}
	
	public void actualizarMapa(String imei,Map map,Date fecha,String color){
		List<GPSData> historialReciente=historialReciente(Constantes.maxResults,imei,fecha);
		if(!historialReciente.isEmpty())
		agregarMarcas(historialReciente, map,imei,true,color,false);
	}

	public List<Cerca> leerCercas(Integer idRuta) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		 /*c WHERE c.ruta.id=:idRuta*/ 
		Query query = session.createQuery("from Cerca c WHERE c.ruta.id=:idRuta order by c.id");
		query.setParameter("idRuta", idRuta);
//		query.setMaxResults(numResultados);
		List<Cerca> l=query.list();
		session.close();
		return l;
	}

	public List<UIComponent> eliminarIcon(Map map){
		List<UIComponent> comps=map.getChildren();
		for(UIComponent comp:comps){
			if(comp instanceof Marker){
				Marker m=(Marker)comp;
				for(UIComponent cicon:m.getChildren()){
					if(cicon instanceof Icon){
						Icon icon=(Icon)cicon;
//							if(icon.getImageURL().equals(Constantes.dir45)
//									||icon.getImageURL().equals(Constantes.dir0)
//									||icon.getImageURL().equals(Constantes.dir90)
//									||icon.getImageURL().equals(Constantes.dir135)
//									||icon.getImageURL().equals(Constantes.dir180)
//									||icon.getImageURL().equals(Constantes.dir225)
//									||icon.getImageURL().equals(Constantes.dir270)
//									||icon.getImageURL().equals(Constantes.dir315))
						if(icon.getImageURL().equals(Constantes.bluePoint))
								map.getChildren().remove(m);
					}
				}
				
			}
		}
		return null;
	}

	public void updateGPSData(GPSData dato){
		Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
		
			
			session.saveOrUpdate(dato);
		
		session.getTransaction().commit();
		session.close();
	}	

	public List<GPSData> eliminarVelocidad0(List<GPSData> datos){
		List<GPSData> nuevos=new ArrayList<GPSData>();
		for(GPSData dato:datos){
			if(dato.getSpeed()==0.0){
				if(!dato.getAlarm().equals(Constantes.normal)){
					nuevos.add(dato);	
				}
			}else{
				nuevos.add(dato);
			}
		}
		return nuevos;
	}

//	public Map agregarMarcas(List<GPSData> marcas, Map map,String imei){
//		List<Marker> marcasA=new ArrayList<Marker>();
//		Polyline p=new Polyline();
//		p.setLineWidth("2");
//		p.setGeodesic("true");
//		Vehiculo v=this.obtenerVehiculo(imei);
//		gpsData=this.ultimaposicionLeida(imei);
//		if(gpsData!=null){
//		p.setId("Poli"+gpsData.getIdRegistro()+new Double(Math.random()*1000).toString().replace('.', '0'));
//		}
//		if(marcas!=null)
//		for(GPSData dato:marcas){
//	        Point punto=new Point();
//	        punto.setId("Punto"+dato.getIdRegistro()+new Double(Math.random()*1000).toString().replace('.', '0'));
//	        if(procesarMarca(dato,v.getVehVeM())){
//	        	Marker mark = new Marker();
//	        	Icon icon=new Icon();
//				icon.setId("Icon"+dato.getIdRegistro()+new Double(Math.random()*1000).toString().replace('.', '0'));
//				icon.setImageURL(this.procesarColor(dato,v.getVehVeM()));
//				mark.getChildren().add(icon);
////				System.out.println(icon.getImageURL()+"##$$$#");
//	        	mark.setId("Marca"+dato.getIdRegistro()+new Double(Math.random()*1000).toString().replace('.', '0'));
//	        	mark.setLatitude(dato.getLatitude().toString());
//	        	mark.setLongitude(dato.getLongitude().toString());
//	        	HTMLInformationWindow h=new HTMLInformationWindow();
//	        	String pattern="####.##";
//	        	DecimalFormat df=new DecimalFormat(pattern);
//	        	if(Constantes.help.equals(dato.getAlarm())){
//	        		h.setHtmlText(df.format(dato.getSpeed()));
//	        	}else{
//	        		h.setHtmlText("V: "+df.format(dato.getSpeed()));
//	        	}
//	        	h.setId("Html"+dato.getIdRegistro()+new Double(Math.random()*1000).toString().replace('.', '0'));
//	        	mark.getChildren().add(h);
//	        		marcasA.add(mark);
//	        	
//	        }
//			punto.setLatitude(dato.getLatitude().toString());
//	        punto.setLongitude(dato.getLongitude().toString());
//	        p.getChildren().add(punto);
//		}
//		if(!marcas.isEmpty()){
////			gpsData=marcas.get(marcas.size()-1);	
//			gpsData=marcas.get(0);
//			Marker mark = new Marker();
//        	Icon icon=new Icon();
//			icon.setId("Icon"+gpsData.getIdRegistro()+new Double(Math.random()*1000).toString().replace('.', '0'));
//			icon.setImageURL(this.curso(gpsData));
//			mark.getChildren().add(icon);
//        	System.out.println(icon.getImageURL()+"##$$$#");
//        	mark.setId("Marca"+"blue"+gpsData.getIdRegistro()+new Double(Math.random()*1000).toString().replace('.', '0'));
//        	mark.setLatitude(gpsData.getLatitude().toString());
//        	mark.setLongitude(gpsData.getLongitude().toString());
//        	HTMLInformationWindow h=new HTMLInformationWindow();
//        		h.setHtmlText(" Velocidad: "+gpsData.getSpeed());	
//        	
//        	h.setId("Html"+gpsData.getIdRegistro()+new Double(Math.random()*1000).toString().replace('.', '0'));
//        	mark.getChildren().add(h);
////        	map.getChildren().add(mark);
//        	marcasA.add(mark);
//		}
//		map.getChildren().addAll(marcasA);
//		map.getChildren().add(p);
//		
//		GPSData gps=ultimaposicion(imei);
//	    map.setLatitude(gps.getLatitude().toString());
//        map.setLongitude(gps.getLongitude().toString());
//        return map;
//	}

public List<GPSData> deteccion(List<GPSData> hist){
		
		List<GPSData> pros=new ArrayList<GPSData>();
		
			pros.addAll(interfazMGDAO.deteccionDeTiempo(hist));
//			pros.addAll(interfazMGDAO.deteccionDeNormal(hist));
//			pros.addAll(interfazMGDAO.deteccionDeFueraCerca(hist));
//			pros.addAll(interfazMGDAO.deteccionDeFueraRuta(hist));
			if(!hist.isEmpty()){
				GPSData g=this.ultimaFueraCerca(hist.get(0).getImei());
				if(g!=null)
			pros.add(g);
				g=this.ultimaFueraRuta(hist.get(0).getImei());
				if(g!=null)
			pros.add(g);
				Vehiculo v=this.obtenerVehiculo(hist.get(0).getImei());
				if(v!=null){
					GPSData t=this.ultimaVelocidadEx(v);
//					int rest=(t.getIdRegistro()-this.ultimaposicion(v.getVehImei()).getIdRegistro());
//					if(rest<=30)
					pros.add(t);
				}
					
					
				
				g=this.ultimaSOS(hist.get(0).getImei());
//					int rest=g.getIdRegistro()-this.ultimaposicion(v.getVehImei()).getIdRegistro();
				if(g!=null)
//					if(rest<=30)
					pros.add(g);
			}
//			pros.addAll(interfazMGDAO.deteccionDeVelocidadMax(hist));
//			pros.addAll(interfazMGDAO.deteccionDeSOS(hist));
			pros.addAll(interfazMGDAO.deteccionEncendido(hist));
		
		Set<GPSData> s=new LinkedHashSet<GPSData>(pros);
		pros=new ArrayList<GPSData>(s);
		List<GPSData> temp=new ArrayList<GPSData>();
		for(GPSData g:pros){
			if(g!=null)
				temp.add(g);
		}
		
		Collections.sort(temp);
//		Arrays.sort(pros,Collections.reverseOrder());
		return temp;
	}

public GPSData ultimaFueraCerca(String imei){
	Session session = HibernateUtil.getSessionFactory().openSession();
	Query query = session.createQuery("from GPSData g WHERE g.imei=:imei AND g.evento=:evento order by g.id DESC");
	query.setParameter("imei", imei);
	query.setParameter("evento", Constantes.fueraDeCerca);
	query.setMaxResults(1);
	GPSData gps=(GPSData) query.uniqueResult();
	session.close();
	return gps;
}

public GPSData ultimaFueraRuta(String imei){
	Session session = HibernateUtil.getSessionFactory().openSession();
	Query query = session.createQuery("from GPSData g WHERE g.imei=:imei AND g.evento=:evento order by g.id DESC");
	query.setParameter("imei", imei);
	query.setParameter("evento", Constantes.fueraDeRuta);
	query.setMaxResults(1);
	GPSData gps=(GPSData) query.uniqueResult();
	session.close();
	return gps;
}

public GPSData ultimaVelocidadEx(Vehiculo v){
	Session session = HibernateUtil.getSessionFactory().openSession();
	Query query = session.createQuery("from GPSData g WHERE g.imei=:imei AND g.speed>=:velocidad order by g.id DESC");
	query.setParameter("imei", v.getVehImei());
	query.setParameter("velocidad", v.getVehVeM());
	query.setMaxResults(1);
	GPSData gps=(GPSData) query.uniqueResult();
	session.close();
	return gps;
}

public GPSData ultimaSOS(String imei){
	Session session = HibernateUtil.getSessionFactory().openSession();
	Query query = session.createQuery("from GPSData g WHERE g.imei=:imei AND g.alarm=:sos order by g.id DESC");
	query.setParameter("imei", imei);
	query.setParameter("sos", Constantes.help);
	query.setMaxResults(1);
	GPSData gps=(GPSData) query.uniqueResult();
	session.close();
	return gps;
}

//public void isInCerca(Usuario usuario) {
//	List<Vehiculo> vehiculos = this.obtenerVehiculos(usuario);
//	for (Vehiculo vehiculo : vehiculos) {
//		List<GPSData> puntos=this.addMarksUnreaded(vehiculo.getVehImei(), new Date(),Constantes.maxResultsCyR);
//		if(!puntos.isEmpty())
//		if(puntos.size()>30){
//			puntos=puntos.subList(0, 15*2);
//		}else{
//			puntos=puntos.subList(0, 1);
//		}
//		List<Cerca> cercas = this.cercasDAO.obtenerCercaPorVehiculo(vehiculo.getVehLla()
//				);
//		if(cercas!=null){
//		for(Cerca cerca:cercas){
//		List<GPSData> datosCerca = cercasDAO.obtenerMarcasPorCerca(cerca.getGeoLla());
//		if (!CalculosGeograficos.isInCerca(datosCerca, puntos)) {
//			FacesContext context = FacesContext.getCurrentInstance();
//			context.addMessage(null, new FacesMessage("Vehiculo: "
//					+ vehiculo.getVehMar() + " " + vehiculo.getVehMar()
//					+ " esta fuera de Cerca" + "Información"));
//			if(!puntos.isEmpty()){
//			GPSData temp = puntos.get(puntos.size() - 1);
//			temp.setEvento(Constantes.fueraDeCerca);
//			this.updateGPSData(temp);
//			}
//		}
//		}
//	}
//	}
//}

public void isInCerca(List<GPSData> puntos,List<Vehiculo> vehiculos) {
	for (Vehiculo vehiculo : vehiculos) {
		List<Cerca> cercas = this.cercasDAO.obtenerCercaPorVehiculo(vehiculo.getVehLla()
				);
		if(cercas!=null){
		for(Cerca cerca:cercas){
		List<GPSData> datosCerca = cercasDAO.obtenerMarcasPorCerca(cerca.getGeoLla());
		GPSData temp=null;
		for(GPSData g:puntos){
			if(g!=null)
			if(g.getImei().equals(vehiculo.getVehImei()))
				temp=g;
		}
		if (!CalculosGeograficos.isInPolygon(datosCerca, temp)) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("Vehiculo: "
					+ vehiculo.getVehMar() + " " + vehiculo.getVehMar()
					+ " esta fuera de Cerca" + "Información"));
			if(!puntos.isEmpty()){
			temp.setEvento(Constantes.fueraDeCerca);
			this.updateGPSData(temp);
			}
		}
		}
	}
	}
}
public void isInRuta(){
	List<ControlRuta> activos=interfazRutaDAO.obtenerRutasActivasControl();
	for(ControlRuta cr:activos){
		List<GPSData> datos=this.interfazRutaDAO.obtenerMarcasPorRuta(cr.getRutLla());
		Ruta rutaActual=this.interfazRutaDAO.obtenerRutaPorId(cr.getRutLla());
		List<GPSData> puntos=this.addMarksUnreaded(cr.getVehImei(), new Date(),Constantes.maxResultsCyR);
		if(!puntos.isEmpty())
		if(puntos.size()>30){
			puntos=puntos.subList(0, 15*2);
		}else{
			puntos=puntos.subList(0, 1);
		}
			if(!CalculosGeograficos.isInRuta(datos, puntos,rutaActual)){
				FacesContext context = FacesContext.getCurrentInstance();
				Vehiculo v=this.obtenerVehiculo(cr.getVehImei());
				if(v!=null){
		        context.addMessage(null, new FacesMessage("Vehiculo: "+v.getVehMar()+" "+v.getVehMar()+
		        		" esta fuera de Ruta"+"Información"  ) );
		        if(!puntos.isEmpty()){
		        GPSData temp=puntos.get(puntos.size()-1);
		        temp.setEvento(Constantes.fueraDeRuta);
		        this.updateGPSData(temp);
		        }
		        }
			}

	}
}

}
