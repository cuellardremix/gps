package com.cj.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.cj.configuracion.HibernateUtil;
import com.cj.geografia.CalculosGeograficos;
import com.cj.pojos.ControlRuta;
import com.cj.pojos.GPSData;
import com.cj.pojos.Ruta;
import com.cj.pojos.Usuario;
import com.cj.pojos.Vehiculo;
import com.cj.utils.Constantes;
import com.googlecode.gmaps4jsf.component.map.Map;
import com.googlecode.gmaps4jsf.component.point.Point;
import com.googlecode.gmaps4jsf.component.polyline.Polyline;

public class InterfazRutaDAO {

	
	public HashMap<String,Integer> obtenerRutasString(Usuario usuario){
		HashMap<String, Integer> nombres=new HashMap<String,Integer>();
		List<Ruta> rutas=leerRutas(usuario);
		for(Ruta ruta:rutas){
			nombres.put(ruta.getRutNom(), ruta.getRutLla());
		}
		return nombres;
	}
	
	public List<Ruta> leerRutas(Usuario usuario){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from Ruta r WHERE r.usuario.id=:idUsuario order by r.id");
		query.setParameter("idUsuario", usuario.getUsuLla());
		List<Ruta> rutas=query.list();
		session.close();
		return rutas;
	}
	
	public void pintarRutaPol(Integer idRuta, Map map){
		List<GPSData> datosXCerca=obtenerMarcasPorRuta(idRuta);
		Polyline pol=new Polyline();
		pol.setLineWidth("3");
		pol.setHexaColor(Constantes.colorAzul);
		pol.setId("PoligonoCerca"+idRuta+new Double(Math.random()*1000).toString().replace('.', '0'));
		Ruta ruta=obtenerRutaPorId(idRuta);
		Double d=CalculosGeograficos.kmAGrado(ruta.getRutAnc());
		for(GPSData gps:datosXCerca){


			Point p=new Point();

			p.setId("Punto"+gps.getIdRegistro()+new Double(Math.random()*1000).toString().replace('.', '0'));
//			Double nLatitud=gps.getLatitude()<0?gps.getLatitude()+d:gps.getLatitude()-d;
//			p.setLatitude((nLatitud).toString());
			p.setLatitude(gps.getLatitude().toString());
//			nLatitud=gps.getLongitude()<0?gps.getLongitude()+d:gps.getLongitude()-d;
//			p.setLongitude(nLatitud.toString());
			p.setLongitude(gps.getLongitude().toString());
			pol.getChildren().add(p);

		}
//		for(int i=datosXCerca.size()-1; i>=0; i--){
//			Point p=new Point();
//			p.setId("Punto"+datosXCerca.get(i).getIdRegistro()+new Double(Math.random()*1000).toString().replace('.', '0'));
//			Double nLatitud=datosXCerca.get(i).getLatitude()<0?datosXCerca.get(i).getLatitude()-d:datosXCerca.get(i).getLatitude()+d;
//			p.setLatitude(nLatitud.toString());
//			nLatitud=datosXCerca.get(i).getLongitude()<0?datosXCerca.get(i).getLongitude()-d:datosXCerca.get(i).getLongitude()+d;
//			p.setLongitude(nLatitud.toString());
//			pol.getChildren().add(p);
//			System.out.println("Primer ladoRAW: "+datosXCerca.get(i).getLatitude()+" "+datosXCerca.get(i).getLongitude());
//			System.out.println("Primer lado: "+p.getLatitude()+" "+p.getLongitude());
//		}
		map.getChildren().add(pol);
		if(datosXCerca!=null)
		if(!datosXCerca.isEmpty()){
		map.setLatitude(datosXCerca.get(0).getLatitude().toString());
		map.setLongitude(datosXCerca.get(0).getLongitude().toString());
		}
	}

	public Ruta obtenerRutaPorId(Integer idRuta){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from Ruta r WHERE r.id=:idRuta order by id");
		query.setParameter("idRuta", idRuta);
		query.setMaxResults(1);
		Ruta ruta=(Ruta)query.uniqueResult();
		session.close();
		return ruta;
	}

	public List<GPSData> obtenerMarcasPorRuta(Integer idRuta){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from GPSData gps WHERE gps.ruta.id=:idRuta order by id");
		query.setParameter("idRuta", idRuta);
		List<GPSData> datos=query.list();
		session.close();
		return datos;
	}
	
	public GPSData guardaMarca(GPSData marca){
		Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

			session.saveOrUpdate(marca);
		
		session.getTransaction().commit();
		session.close();
		return marca;
	}
	
	public List<GPSData> generarRutaBase(GPSData inicio,GPSData finali,Integer idRuta,String imei){
		List<GPSData> datos=obtenerDatosEnRango(inicio, finali,imei);
		Ruta ruta=this.obtenerRutaPorId(idRuta);
		for(GPSData dato:datos){
			dato.setRuta(ruta);
		}
		updateRuta(datos);
		return datos;
	}
	
	public void updateRuta(List<GPSData> marcas){
		Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
		for(GPSData marca:marcas){
			session.saveOrUpdate(marca);
		}
		session.getTransaction().commit();
		session.close();
	}
	
	public List<GPSData> obtenerDatosEnRango(GPSData inicio,GPSData finali, String imei){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from GPSData gps WHERE gps.id>=:idInicio AND gps.id<=:idFinal AND gps.imei=:imei order by id");
		query.setParameter("idInicio", inicio.getIdRegistro());
		query.setParameter("idFinal", finali.getIdRegistro());
		query.setParameter("imei", imei);
		
		List<GPSData> datos=query.list();
		session.close();
		return datos;		
	}
	
	public Ruta nuevaRuta(String nombre, Double ancho, Usuario usuario){
		Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
		Ruta ruta=new Ruta();
		ruta.setRutAnc(ancho);
		ruta.setRutNom(nombre);
		ruta.setUsuario(usuario);
		ruta.setEstadoRegistro(true);
			session.saveOrUpdate(ruta);
		session.getTransaction().commit();
		session.close();
		return ruta;
	}
	public ControlRuta obtenerControlRuta(String vehImei){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from ControlRuta WHERE vehImei=:vehImei AND activo=true order by id");

		query.setParameter("vehImei", vehImei);
		ControlRuta c=(ControlRuta)query.uniqueResult();
		session.close();
		return c;
	}
	public List<ControlRuta> obtenerRutasActivasControl(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from ControlRuta WHERE activo=true order by id");
		List<ControlRuta> c=query.list();
		session.close();
		return c;
	}
	public void guardaControlRuta(ControlRuta control ){
		Session session = HibernateUtil.getSessionFactory().openSession();
	    session.beginTransaction();
			session.saveOrUpdate(control);
		session.getTransaction().commit();
		session.close();
	}
	
	public List<GPSData> finesRuta(){
		List<ControlRuta> crs=this.obtenerRutasActivasControl();
		List<GPSData> ultimos=new ArrayList<GPSData>();
		for(ControlRuta cr:crs){
			GPSData dato=this.obtenerUltimaMarcaPorRuta(cr.getRutLla());
			ultimos.add(dato);
		}
		return ultimos;
	}
	
	public GPSData obtenerUltimaMarcaPorRuta(Integer idRuta){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from GPSData gps WHERE gps.ruta.id=:idRuta order by id desc");
		query.setParameter("idRuta", idRuta);
		query.setMaxResults(1);
		GPSData dato=(GPSData) query.uniqueResult();
		session.close();
		return dato;
	}
	
	public List<Ruta> rutasXHora(Usuario usuario){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from Ruta r WHERE HOUR(r.rutFeI)=HOUR(CURRENT_DATE)"
				+ " AND r.usuario.id=:idUsuario order by r.id");
//		query.setDate("fecha", new Date());
		query.setParameter("idUsuario", usuario.getUsuLla());
		List<Ruta> ruta=query.list();
		session.close();
		return ruta;		
	}
	
	public List<Vehiculo> obtenerVehiculosXRuta(Integer idRuta){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from Ruta r WHERE r.id=:idRuta");
		
		query.setParameter("idRuta", idRuta);
		query.setMaxResults(1);
		Ruta r=(Ruta)query.uniqueResult();
		session.close();
		List<Vehiculo> v=new ArrayList<Vehiculo>();
		v.add(r.getVehiculo1());
		v.add(r.getVehiculo2());
		v.add(r.getVehiculo3());
		v.add(r.getVehiculo4());
		v.add(r.getVehiculo5());
		return v;
	}
}
