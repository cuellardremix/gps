package com.cj.catalogosDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.component.UIComponent;

import org.hibernate.Query;
import org.hibernate.Session;

import com.cj.configuracion.HibernateUtil;
import com.cj.pojos.Cerca;
import com.cj.pojos.GPSData;
import com.cj.pojos.Usuario;
import com.cj.pojos.Vehiculo;
import com.cj.utils.Constantes;
import com.cj.valueChangeListeners.ValueChangerRuta;
import com.googlecode.gmaps4jsf.component.map.Map;
import com.googlecode.gmaps4jsf.component.marker.Marker;
import com.googlecode.gmaps4jsf.component.point.Point;
import com.googlecode.gmaps4jsf.component.polyline.Polyline;

public class AdminCercasDAO {

	public void guardarMarcas(Map map){
		List<UIComponent> comps=map.getChildren();
		List<GPSData> marcas=new ArrayList<GPSData>();
		for(UIComponent comp:comps){
			if(comp instanceof Marker){
				Marker m=(Marker)comp;
				Integer id=new Integer(m.getId().substring(Constantes.marca.length()));
				GPSData gps=this.obtenerMarca(id);
				gps.setLatitude(new Double(m.getLatitude()));
				gps.setLongitude(new Double(m.getLongitude()));
				marcas.add(gps);
			}
		}
		this.guardarMarcas(marcas);
	}
	
	public GPSData obtenerMarca(Integer idRegistro) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from GPSData g where g.idRegistro=:idRegistro order by g.id");
		query.setParameter("idRegistro", idRegistro);
		query.setMaxResults(1);
		GPSData r=(GPSData)query.uniqueResult();
		session.close();
		return r;
	}

	public Cerca obtenerCercaXId(Integer idCerca) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from Cerca c where c.geoLla=:idCerca order by c.id");
		query.setParameter("idCerca", idCerca);
		query.setMaxResults(1);
		Cerca c=(Cerca)query.uniqueResult();
		session.close();
		return c;
	}
	
	public void guardarMarcas(List<GPSData> marcas){
		Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
		for(GPSData marca:marcas){
			session.saveOrUpdate(marca);
		}
		session.getTransaction().commit();
		session.close();
	}
	
	public Cerca transToquens(Cerca buscar) {
		buscar.setGeoDes(buscar.getGeoDes().replace('*','%'));
		buscar.setGeoNom(buscar.getGeoNom().replace('*','%'));
		return buscar;
	}

	public List<Cerca> busqueda(Cerca c, Usuario u) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from Cerca c where c.estadoRegistro=true AND " +
				"(c.geoLla= :geoLla OR"+
				"(DAY(c.geoFAl)=DAY(:geoFAl) AND MONTH(c.geoFAl)=MONTH(:geoFAl)) OR "+
				"(DAY(c.geoFBa)=DAY(:geoFBa) AND MONTH(c.geoFBa)=MONTH(:geoFBa)) OR  "+
				"c.geoDes like :geoDes OR "+
				"c.geoNom like :geoNom OR "+
				"c.latitude=:latitude OR "+
				"c.longitude=:longitude "
				+ "c.usuario.id=:idUsuario "+
				"order by c.id");
	
	query.setParameter("geoLla", c.getGeoLla());
	query.setDate("geoFAl", c.getGeoFAl());
	query.setDate("geoFBa", c.getGeoFBa());
	query.setParameter("geoDes", c.getGeoDes());
	query.setParameter("geoNom", c.getGeoNom());
	query.setParameter("latitude", c.getLatitude());
	query.setParameter("longitude", c.getLongitude());
//	query.setParameter("radio", c.getRadio());
	query.setParameter("idUsuario", u.getUsuLla());
		List<Cerca> v=query.list();
		session.close();
		return v;
	}


	public Map agregarMarcasEditable(Integer idCerca, Map map){
		List<GPSData> marcas=this.obtenerMarcasPorCerca(idCerca);
		Polyline p=new Polyline();
		p.setLineWidth("2");
		if(marcas!=null)
		for(GPSData dato:marcas){
	        Point punto=new Point();
	        Marker marca=new Marker();
	        punto.setId("Punto"+dato.getIdRegistro()+new Double(Math.random()*1000).toString().replace('.', '0'));
	        marca.setId(Constantes.marca+dato.getIdRegistro().toString());
			punto.setLatitude(dato.getLatitude().toString());
			marca.setLatitude(dato.getLatitude().toString());
	        punto.setLongitude(dato.getLongitude().toString());
	        marca.setLongitude(dato.getLongitude().toString());
	        p.getChildren().add(punto);
	        marca.setDraggable("true");
	        marca.setSubmitOnValueChange("true");
	        marca.addValueChangeListener(new ValueChangerRuta());
	        map.getChildren().add(marca);
	        
		}
		
		map.getChildren().add(p);
        return map;
	}

	public List<GPSData> obtenerMarcasPorCerca(Integer idCerca) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from GPSData gps WHERE gps.cerca.id=:idCerca order by id");
		query.setParameter("idCerca", idCerca);
		List<GPSData> datos=query.list();
		session.close();
		return datos;
	}
	

	public List<GPSData> obtenerMarcasPorCerca(Integer idCerca,Double radio,GPSData punto) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("Select new GPSData(gps.latitude,gps.longitude) from "
				+ "GPSData gps WHERE gps.cerca.id=:idCerca "
				+ "AND gps.gpsH(:punto,:punto)<= :radio "
				+ "order by id");
		query.setParameter("idCerca", idCerca);
		query.setParameter("radio", radio);
		query.setParameter("punto", punto);
		List<GPSData> datos=query.list();
		session.close();
		return datos;
	}

	public void deleteCerca(Cerca cerca) {
		cerca.setEstadoRegistro(false);
		updateCerca(cerca);
		
	}

	public void updateCerca(Cerca cerca) {
		Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
			session.saveOrUpdate(cerca);
		session.getTransaction().commit();
		session.close();
		
	}

//	public List<Cerca> obtenerCercaPorVehiculo(String imei){
//		Session session = HibernateUtil.getSessionFactory().openSession();
//		Query query = session.createQuery("from Cerca c WHERE c.vehiculo1.vehImei=:imei "
//				+ " OR c.vehiculo2.vehImei=:imei "
//				+ " OR c.vehiculo3.vehImei=:imei "
//				+ " OR c.vehiculo4.vehImei=:imei "
//				+ " OR c.vehiculo5.vehImei=:imei "
//				
//				+ " order by c.id");
////		query.setMaxResults(1);
//		query.setParameter("imei", imei);
//		List<Cerca> c=query.list();
//		session.close();
//		return c;
//	}

	public List<Cerca> obtenerCercaPorVehiculo(Integer idVehiculo){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from Cerca c WHERE :idVehiculo IN elements(c.vehiculos) "				
				+ " order by c.id");
//		query.setMaxResults(1);
		query.setParameter("idVehiculo", idVehiculo);
		List<Cerca> c=query.list();
		session.close();
		return c;
	}
	
	public List<Cerca> obtenerCercasPorVehiculos(List<Vehiculo> vehiculos){
		List<Cerca> r=new ArrayList<Cerca>();
		for(Vehiculo v:vehiculos){
			List<Cerca> t=this.obtenerCercaPorVehiculo(v.getVehLla());
			for(Cerca c:t){
				c.setGeoNom(" "+c.getGeoNom()+v.getVehMar()+" "+v.getVehMod());
			}
			r.addAll(t);
		}
		return r;
	}
	
	public List<Cerca> obtenerCercas(Usuario usuario){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from Cerca c  where c.usuario.id=:idUsuario AND c.estadoRegistro=true"+
				" order by c.id");
		query.setParameter("idUsuario", usuario.getUsuLla());
		List<Cerca> v=query.list();
		session.close();
		return v;
	}
	
	public HashMap<String,Integer> obtenerCercasString(List<Cerca> cercas){
		HashMap<String, Integer> nombres=new HashMap<String,Integer>();
//		List<Cerca> cercas=obtenerCercas();
		for(Cerca cerca:cercas){
			nombres.put(cerca.getGeoNom(), cerca.getGeoLla());
		}
		return nombres;
	}
}
