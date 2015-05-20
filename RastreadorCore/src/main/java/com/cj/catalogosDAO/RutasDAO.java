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
import com.cj.pojos.Ruta;
import com.cj.pojos.Usuario;
import com.cj.utils.Constantes;
import com.cj.valueChangeListeners.ValueChangerRuta;
import com.googlecode.gmaps4jsf.component.map.Map;
import com.googlecode.gmaps4jsf.component.marker.Marker;
import com.googlecode.gmaps4jsf.component.point.Point;
import com.googlecode.gmaps4jsf.component.polyline.Polyline;

public class RutasDAO {

	public void updateRuta(Ruta ruta){
		Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
		
			session.saveOrUpdate(ruta);
		session.getTransaction().commit();
		session.close();
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
	
	public Ruta obtenerRutaById(Integer rutLla) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from Ruta r where r.rutLla=:idRuta AND r.estadoRegistro=true order by id");
		query.setParameter("idRuta", rutLla);
		Ruta r=(Ruta) query.uniqueResult();
		session.close();
		return r;
	}
	
	public List<Ruta> obtenerRutas() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from Ruta r where r.estadoRegistro=true order by r.id");
		List<Ruta> r=query.list();
		session.close();
		return r;
	}
	
	public void deleteRuta(Ruta ruta){
		Session session = HibernateUtil.getSessionFactory().openSession();
		ruta.setEstadoRegistro(false);
		session.beginTransaction();
		session.saveOrUpdate(ruta);
		session.getTransaction().commit();
		session.close();
	}
	
	
	public List<GPSData> obtenerMarcasPorRuta(Integer idRuta){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from GPSData gps WHERE gps.ruta.id=:idRuta order by id");
		query.setParameter("idRuta", idRuta);
		List<GPSData> datos=query.list();
		session.close();
		return datos;
	}
	
	public Map agregarMarcasEditable(Integer idRuta, Map map){
		List<GPSData> marcas=this.obtenerMarcasPorRuta(idRuta);
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

	public Ruta transToquens(Ruta r) {
		r.setRutNom(r.getRutNom().replace('*', '%'));
		r.setRutDes(r.getRutDes().replace('*','%'));
		return r;
	}

	public List<Ruta> busqueda(Ruta r, Usuario usuario) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from Ruta r where r.usuario.id=:idUsuario AND" +
				"(r.rutLla = :rutLla OR "+
				"r.rutNom like :rutNom OR "+
				"r.rutAnc = :rutAnc OR "+
				"(day(r.rutFAl) = day(:rutFAl) AND month(r.rutFAl)=month(:rutFAl)) OR "+
				"(day(r.rutFBa) = day(:rutFBa) AND MONTH(r.rutFBa)=MONTH(:rutFBa)) OR "+
				"r.rutDes like :rutDes OR "+
				"(day(r.rutFec) =day(:rutFec) AND MONTH(r.rutFec)=MONTH(:rutFec)) OR "+
				"(DAY(r.rutFeI) = DAY(:rutFeI) AND MONTH(r.rutFeI)=MONTH(:rutFeI)) OR "+
				"(DAY(r.rutFeF) = DAY(:rutFeF) AND MONTH(r.rutFeF)=MONTH(:rutFeF))) AND "+
				" r.estadoRegistro=true order by r.id");
		query.setParameter("idUsuario", usuario.getUsuLla());
		query.setParameter("rutLla", r.getRutLla());
		query.setParameter("rutNom", r.getRutNom());
		query.setParameter("rutAnc", r.getRutAnc());
		query.setDate("rutFAl", r.getRutFAl());
		query.setParameter("rutDes", r.getRutDes());
		query.setDate("rutFec", r.getRutFec());
		query.setDate("rutFeI", r.getRutFeI());
		query.setDate("rutFeF", r.getRutFeF());
		query.setDate("rutFBa", r.getRutFBa());
		
		List<Ruta> v=query.list();
		session.close();
		return v;
	}

	public HashMap<String, Integer> obtenerRutasString() {
		HashMap<String, Integer> nombres=new HashMap<String,Integer>();
		List<Ruta> rs=this.obtenerRutas();
		nombres.put("", null);
		for(Ruta r:rs){
			nombres.put(r.getRutNom(), r.getRutLla());
		}
		return nombres;
	}

	public List<Ruta> obtenerRutaPorVehiculo(String imei){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("Select r from Ruta r WHERE r.vehiculo1.vehImei=:imei "
				+ " OR r.vehiculo2.vehImei=:imei "
				+ " OR r.vehiculo3.vehImei=:imei "
				+ " OR r.vehiculo4.vehImei=:imei "
				+ " OR r.vehiculo5.vehImei=:imei "
				
				+ " order by r.id");
//		query.setMaxResults(1);
		query.setParameter("imei", imei);
		List<Ruta> r=query.list();
		session.close();
		return r;
	}
	
}
