package com.cj.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.component.UIComponent;

import org.hibernate.Query;
import org.hibernate.Session;

import com.cj.configuracion.HibernateUtil;
import com.cj.geografia.CalculosGeograficos;
import com.cj.pojos.Cerca;
import com.cj.pojos.GPSData;
import com.cj.pojos.Usuario;
import com.cj.pojos.Vehiculo;
import com.googlecode.gmaps4jsf.component.map.Map;
import com.googlecode.gmaps4jsf.component.point.Point;
import com.googlecode.gmaps4jsf.component.polygon.Polygon;

public class InterfazCercaPolDAO {

	public GPSData ultimaposicionCerca(String imei,Integer idCerca) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from GPSData gps WHERE gps.imei=:imei AND gps.Cerca.id=:idCerca order by id DESC");
		query.setParameter("idCerca", idCerca);
		query.setParameter("imei", imei);
		query.setMaxResults(1);
		GPSData gps=(GPSData) query.uniqueResult();
		session.close();
		return gps;
	}
	
	public List<Cerca> leerCercas(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from Cerca c where c.estadoRegistro=true order by c.id");
		List<Cerca> cercas=query.list();
		session.close();
		return cercas;
	}
	
	public HashMap<String,Integer> obtenerCercasString(){
		HashMap<String, Integer> nombres=new HashMap<String,Integer>();
		List<Cerca> cercas=leerCercas();
		for(Cerca cerca:cercas){
			nombres.put(cerca.getGeoNom(), cerca.getGeoLla());
		}
		return nombres;
	}
	
	public List<GPSData> obtenerMarcasPorCerca(Integer idCerca){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from GPSData gps WHERE gps.cerca.id=:idCerca order by id");
		query.setParameter("idCerca", idCerca);
		List<GPSData> datos=query.list();
		session.close();
		return datos;
	}
	
	public Cerca obtenerCercaPorId(Integer idCerca){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from Cerca c WHERE c.id=:idCerca order by id");
		query.setParameter("idCerca", idCerca);
		query.setMaxResults(1);
		Cerca cerca=(Cerca)query.uniqueResult();
		session.close();
		return cerca;
	}

	public void pintarCercaSimple(List<GPSData> puntos,Map map){
		Polygon pol=new Polygon();
		for(GPSData g:puntos){
			Point p=new Point();
			p.setLatitude(g.getLatitude().toString());
			p.setLongitude(g.getLongitude().toString());
			pol.getChildren().add(p);
		}
		map.getChildren().add(pol);
	}
	
	public List<GPSData> pintarCerca(Integer idCerca, Map map){
		List<GPSData> datosXCerca=obtenerMarcasPorCerca(idCerca);
		List<GPSData> nuevo1=new ArrayList<GPSData>();
		List<GPSData> nuevo2=new ArrayList<GPSData>();
		Polygon pol=new Polygon();
		pol.setLineWidth("1");
		GPSData[] datos=(GPSData[]) datosXCerca.toArray(new GPSData[1]);
		for(int i=0; i<datos.length;i=i+2){
//			if(i!=0){
//			if(datos[i-1].getLatitude()>datos[i].getLatitude()){
//				datos[i].setLatitude(datos[i].getLatitude()-0.00156);
//			}else{
//				datos[i].setLatitude(datos[i].getLatitude()+0.00156);
//			}
//			if(datos[i-1].getLongitude()>datos[i].getLongitude()){
//				datos[i].setLongitude(datos[i].getLongitude()-0.00156);
//			}else{
//				datos[i].setLatitude(datos[i].getLatitude()+0.00156);
//			}
//			}
			Double d=0.000156;
			if(i+2<datos.length){
			GPSData pos1=new GPSData(datos[i].getLatitude()<0?((Math.abs(datos[i].getLatitude())-d)*-1):datos[i].getLatitude()-d,datos[i+1].getLongitude()-d);
			nuevo1.add(pos1);
			GPSData pos2=new GPSData(datos[i+1].getLatitude()<0?((Math.abs(datos[i].getLatitude())+d)*-1):datos[i].getLatitude()+d,datos[i].getLongitude()+d);
			nuevo2.add(pos2);
			}
		}
		datos=(GPSData[]) nuevo2.toArray(new GPSData[1]);
		for(int i=datos.length-1; i>=0; i--){
			nuevo1.add(datos[i]);
		}
		
		for(GPSData gps:nuevo1){
			Point p=new Point();
if(gps!=null){
			p.setId("Punto"+gps.getIdRegistro()+new Double(Math.random()*1000).toString().replace('.', '0'));
			
			p.setLatitude(gps.getLatitude().toString());
			p.setLongitude(gps.getLongitude().toString());
			pol.getChildren().add(p);
}
		}
		map.getChildren().add(pol);
		map.setLatitude(datosXCerca.get(0).getLatitude().toString());
		map.setLongitude(datosXCerca.get(0).getLongitude().toString());
		return nuevo1;
	}
	
//	public void pintarCercaPol(Integer idCerca, Map map){
//		List<GPSData> datosXCerca=obtenerMarcasPorCerca(idCerca);
//		Polygon pol=new Polygon();
//		pol.setLineWidth("1");
//		pol.setId("PoligonoCerca"+idCerca+new Double(Math.random()*1000).toString().replace('.', '0'));
//		Cerca cerca=obtenerCercaPorId(idCerca);
//		Double d=CalculosGeograficos.kmAGrado(cerca.getRadio());
//		for(GPSData gps:datosXCerca){
//
//
//			Point p=new Point();
//
//			p.setId("Punto"+gps.getIdRegistro()+new Double(Math.random()*1000).toString().replace('.', '0'));
//			Double nLatitud=gps.getLatitude()<0?gps.getLatitude()+d:gps.getLatitude()-d;
//			p.setLatitude((nLatitud).toString());
//
//			nLatitud=gps.getLongitude()<0?gps.getLongitude()+d:gps.getLongitude()-d;
//			p.setLongitude(nLatitud.toString());
//			pol.getChildren().add(p);
//
//		}
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
//		map.getChildren().add(pol);
//		map.setLatitude(datosXCerca.get(0).getLatitude().toString());
//		map.setLongitude(datosXCerca.get(0).getLongitude().toString());
//	}
	

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
	
	public void updateCerca(List<GPSData> marcas){
		Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
		for(GPSData marca:marcas){
			session.saveOrUpdate(marca);
		}
		session.getTransaction().commit();
		session.close();
	}
	
	public List<GPSData> generarCercaBase(GPSData inicio,GPSData finali,Integer idCerca,String imei){
		List<GPSData> datos=obtenerDatosEnRango(inicio, finali,imei);
		Cerca Cerca=this.obtenerCercaPorId(idCerca);
		for(GPSData dato:datos){
			dato.setCerca(Cerca);
		}
		updateCerca(datos);
		return datos;
	}
	
	public Cerca nuevaCerca(String nombre, Double ancho,
			List<Vehiculo> vehiculos, 
			List<GPSData> marcas,Usuario usuario){
		Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
		Cerca cerca=new Cerca();
//		cerca.setRadio(ancho);
		cerca.setGeoNom(nombre);
		cerca.setVehiculos(vehiculos);

//		cerca.setMarcas(marcas);
		cerca.setEstadoRegistro(true);
		cerca.setUsuario(usuario);
			session.saveOrUpdate(cerca);
		session.getTransaction().commit();
		session.close();
		for(GPSData g:marcas){
			InterfazDAO intD=new InterfazDAO();
			g.setCerca(cerca);
			intD.updateGPSData(g);
		}
		return cerca;
	}
	
	public Polygon obtenPoligono(Map map){
		List<UIComponent> cs=map.getChildren();
		for(UIComponent c:cs){
			if(c instanceof Polygon){
				return (Polygon)c;
			}
		}
		return null;
	}
	
	public GPSData guardaMarca(GPSData marca){
		Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

			session.saveOrUpdate(marca);
		
		session.getTransaction().commit();
		session.close();
		return marca;
	}

}
