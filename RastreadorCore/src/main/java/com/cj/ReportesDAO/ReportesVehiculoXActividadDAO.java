package com.cj.ReportesDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;

import org.hibernate.Query;
import org.hibernate.Session;

import com.cj.configuracion.HibernateUtil;
import com.cj.geografia.CalculosGeograficos;
import com.cj.pojos.GPSData;
import com.cj.pojos.Vehiculo;
import com.cj.utils.Constantes;
import com.googlecode.gmaps4jsf.services.GMaps4JSFServiceFactory;
import com.googlecode.gmaps4jsf.services.data.PlaceMark;

public class ReportesVehiculoXActividadDAO {

	   private JasperPrint jasperPrint;

	public void crearReporte(List<GPSData> datos, String jasper,HashMap<String,Object> parameters){
	    	try {
	    		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

	    		

	    		
	    		ClassLoader classLoader = getClass().getClassLoader();
	    		

				jasperPrint= JasperFillManager.fillReport( classLoader.getResource(jasper).getPath(), parameters, new JRBeanCollectionDataSource(datos));
 
				
				HttpServletResponse httpServletResponse=(HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
				httpServletResponse.addHeader("Content-disposition", "attachment; filename=report.xls");
				ServletOutputStream servletOutputStream;
				try {
					servletOutputStream = httpServletResponse.getOutputStream();
				       JRXlsxExporter docxExporter=new JRXlsxExporter();  
				       docxExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);  
				       docxExporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);  
				       docxExporter.exportReport();  
					FacesContext.getCurrentInstance().responseComplete();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				  
			} catch (JRException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    }
	
	public List<GPSData> buscarActividad(Date fechaIni, Date fechaFin,
			String vehiculoImei) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from GPSData g where " +
				" g.imei=:vehiculoImei " +
				" AND g.fecha>=:fechaIni" +
				" AND g.fecha<=:fechaFin " +
				" order by g.id");
		query.setParameter("fechaIni", fechaIni);
		query.setParameter("fechaFin", fechaFin);
		query.setParameter("vehiculoImei", vehiculoImei);
		List<GPSData> v=query.list();
		session.close();
		return v;
	}

	public List<GPSData> eliminarIntranscendentes(List<GPSData> marcas, Double maxSpeed){
		List<GPSData> r=new ArrayList<GPSData>();
		List<GPSData> os=new ArrayList<GPSData>();
		String fueraCerca="";
		boolean vel=true;
		for(GPSData marca:marcas){
			if(marca.getEvento()!=null)
			if(marca.getEvento().equals(Constantes.fueraDeCerca)){
				fueraCerca=", Fuera de Cerca";
			}else{
				fueraCerca="";
			}

			if(marca.getSpeed()>maxSpeed){
				os.add(marca);
				vel=true;
			}else{
				if(vel){
					os.add(new GPSData(0.0,0.0));
				}
				vel=false;
			}
			if(!marca.getAlarm().equals(Constantes.normal) && !marca.getAlarm().equals("oil") 
					&& !marca.getAlarm().equals("acc alarm")){
				if(marca.getAlarm().equals(Constantes.apagado)){
					marca.setAlarm("Apagado"+fueraCerca);
					if(marca.getEvento()!=null){
					if(marca.getEvento().equals(Constantes.bajaDeGasolina))
						marca.setAlarm(marca.getAlarm()+", Baja de Gasolina de: "+marca.getIo1()+"L");
					if(marca.getEvento().equals(Constantes.llenadoDeGasolina))
						marca.setAlarm(marca.getAlarm()+", Llenado de Gasolina de: "+marca.getIo1()+"L");
					}
				}
				if(marca.getAlarm().equals(Constantes.encendido)){
					marca.setAlarm("Encendido"+fueraCerca);
					if(marca.getEvento()!=null){
						if(marca.getEvento().equals(Constantes.bajaDeGasolina))
							marca.setAlarm(marca.getAlarm()+", Baja de Gasolina de: "+marca.getIo1()+"L");
						if(marca.getEvento().equals(Constantes.llenadoDeGasolina))
							marca.setAlarm(marca.getAlarm()+", Llenado de Gasolina de: "+marca.getIo1()+"L");
					}
				}
				if(marca.getAlarm().equals(Constantes.alarmaPuerta)){
					marca.setAlarm("Puerta Abierta"+fueraCerca);
					if(marca.getEvento()!=null){
						if(marca.getEvento().equals(Constantes.bajaDeGasolina))
							marca.setAlarm(marca.getAlarm()+", Baja de Gasolina de: "+marca.getIo1()+"L");
					if(marca.getEvento().equals(Constantes.llenadoDeGasolina))
						marca.setAlarm(marca.getAlarm()+", Llenado de Gasolina de: "+marca.getIo1()+"L");
					}
				}
				if(marca.getAlarm().equals(Constantes.help)){
					marca.setAlarm("Solicitud de ayuda"+fueraCerca);
					if(marca.getEvento()!=null){
						if(marca.getEvento().equals(Constantes.bajaDeGasolina))
							marca.setAlarm(marca.getAlarm()+", Baja de Gasolina de: "+marca.getIo1()+"L");
						if(marca.getEvento().equals(Constantes.llenadoDeGasolina))
							marca.setAlarm(marca.getAlarm()+", Llenado de Gasolina de: "+marca.getIo1()+"L");
					}
				}
				if(marca.getAlarm().equals(Constantes.detenido)){
					marca.setAlarm("Motor Detenido"+fueraCerca);
					if(marca.getEvento()!=null){
						if(marca.getEvento().equals(Constantes.bajaDeGasolina))
							marca.setAlarm(marca.getAlarm()+", Baja de Gasolina de: "+marca.getIo1()+"L");
						if(marca.getEvento().equals(Constantes.llenadoDeGasolina))
							marca.setAlarm(marca.getAlarm()+", Llenado de Gasolina de: "+marca.getIo1()+"L");
					}
				}
				if(marca.getAlarm().equals(Constantes.reActivado)){
					marca.setAlarm("Motor Activado"+fueraCerca);
					if(marca.getEvento()!=null){
						if(marca.getEvento().equals(Constantes.bajaDeGasolina))
							marca.setAlarm(marca.getAlarm()+", Baja de Gasolina de: "+marca.getIo1()+"L");
						if(marca.getEvento().equals(Constantes.llenadoDeGasolina))
							marca.setAlarm(marca.getAlarm()+", Llenado de Gasolina de: "+marca.getIo1()+"L");
					}
				}
				if(marca.getAlarm().equals(Constantes.alarmaPuerta)){
					marca.setAlarm("Puerta Abierta"+fueraCerca);
					if(marca.getEvento()!=null){
						if(marca.getEvento().equals(Constantes.bajaDeGasolina))
							marca.setAlarm(marca.getAlarm()+", Baja de Gasolina de: "+marca.getIo1()+"L");
						if(marca.getEvento().equals(Constantes.llenadoDeGasolina))
							marca.setAlarm(marca.getAlarm()+", Llenado de Gasolina de: "+marca.getIo1()+"L");
					}
				}

				r.add(marca);
			}
			if(marca.getEvento()!=null){
				if(marca.getEvento().equals(Constantes.bajaDeGasolina)){
					if(!marca.getAlarm().contains("Baja de Gasolina de:")){
						marca.setAlarm("Baja de Gasolina de: "+marca.getIo1()+"L");
						r.add(marca);
					}
				}
				if(marca.getEvento().equals(Constantes.llenadoDeGasolina)){
					if(!marca.getAlarm().contains("Llenado de Gasolina de:")){
						marca.setAlarm("Llenado de Gasolina de: "+marca.getIo1()+"L");
						r.add(marca);
					}
				}
			}
		}
		if(!os.isEmpty()){
		os.remove(os.size()-1);
		for(int i=0; i<os.size(); i++){
			GPSData o=os.get(i);
			if(o.getLatitude().equals(0.0) && o.getLongitude().equals(0.0)){
				i++;
				os.get(i).setAlarm("Exceso de Velocidad");
				r.add(os.get(i));
			}
			System.out.println(o.getLatitude() +" "+ o.getLongitude());
		}
		}
//		for(GPSData marca:r){
//			try {
//				PlaceMark placeMark = GMaps4JSFServiceFactory.getReverseGeocoderService().getPlaceMark(marca.getLatitude().toString(), marca.getLongitude().toString());
//				marca.setIo4(placeMark.getAddress());
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		return r;
	}
	
	public List<GPSData> buscarSitiosVisitados(Date fechaIni, Date fechaFin,
			String vehiculoImei) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from GPSData g where " +
				" g.imei=:vehiculoImei " +
				" AND g.fecha>=:fechaIni" +
				" AND g.fecha<=:fechaFin " +
				" order by g.id desc");
		query.setParameter("fechaIni", fechaIni);
		query.setParameter("fechaFin", fechaFin);
		query.setParameter("vehiculoImei", vehiculoImei);
		List<GPSData> v=query.list();
		session.close();
		return v;
	}
	
	public List<GPSData> excesoVelocidad(Date fechaIni, Date fechaFin,
			String vehiculoImei) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from GPSData g where " +
				" g.imei=:vehiculoImei " +
				" AND g.fecha>=:fechaIni" +
				" AND g.fecha<=:fechaFin " +
				" AND g.speed>=:velocidad" +
				" order by g.id");
		query.setParameter("fechaIni", fechaIni);
		query.setParameter("fechaFin", fechaFin);
		query.setParameter("vehiculoImei", vehiculoImei);
		query.setParameter("velocidad", Constantes.maximaVelocidad);
		List<GPSData> v=query.list();
		session.close();
		return v;
	}
	public List<GPSData> paradasVehiculo(Date fechaIni, Date fechaFin,
			String vehiculoImei) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from GPSData g where " +
				" g.imei=:vehiculoImei " +
				" AND g.fecha>=:fechaIni" +
				" AND g.fecha<=:fechaFin " +
				" AND g.speed=0.0" +
				" order by g.id");
		query.setParameter("fechaIni", fechaIni);
		query.setParameter("fechaFin", fechaFin);
		query.setParameter("vehiculoImei", vehiculoImei);
		List<GPSData> v=query.list();
		session.close();
		return v;
	}
	
	public List<GPSData> velocidadPromedio(Date fechaIni, Date fechaFin,
			String vehiculoImei) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from GPSData g where " +
				" g.imei=:vehiculoImei " +
				" AND g.fecha>=:fechaIni" +
				" AND g.fecha<=:fechaFin " +
				" order by g.id");
		query.setParameter("fechaIni", fechaIni);
		query.setParameter("fechaFin", fechaFin);
		query.setParameter("vehiculoImei", vehiculoImei);
		List<GPSData> v=query.list();
		session.close();
		return v;
	}
	public Double calculaPromedioVelocidad(List<GPSData> datos){
		Double promedio=0.0;
		for(GPSData dato:datos){
			promedio+=dato.getSpeed();
		}
		return (promedio/datos.size());
	}
	
	public List<GPSData> geoCercas(Date fechaIni, Date fechaFin,
			String vehiculoImei) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from GPSData g where " +
				" g.imei=:vehiculoImei " +
				" AND g.fecha>=:fechaIni" +
				" AND g.fecha<=:fechaFin " +
				" order by g.cerca.id");
		query.setParameter("fechaIni", fechaIni);
		query.setParameter("fechaFin", fechaFin);
		query.setParameter("vehiculoImei", vehiculoImei);
		List<GPSData> v=query.list();
		session.close();
		return v;
	}
	
	public Date sumarRestarHorasFecha(Date fecha, int horas){
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(Calendar.MINUTE, horas);
		return calendar.getTime();
	}
	
	public List<GPSData> ubicacionActual(String vehiculoImei) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from GPSData g WHERE g.imei=:imei order by g.id DESC");
		query.setParameter("imei", vehiculoImei);
		query.setMaxResults(1);
		GPSData gps=(GPSData) query.uniqueResult();
		session.close();
		List<GPSData> r=new ArrayList<GPSData>();
		r.add(gps);
		return r;
	}
	
	public HashMap<String, String> obtenerVehiculosString(List<Vehiculo> vs) {
		HashMap<String, String> nombres=new HashMap<String,String>();
		
		nombres.put("", null);
		for(Vehiculo v:vs){
			nombres.put(v.getVehMar()+" "+v.getVehMod(), v.getVehImei());
		}
		return nombres;
	}
	
	
	public Double kilometraje(List<GPSData> v){
		List<GPSData> r=new ArrayList<GPSData>();
		GPSData g=v.get(v.size()-1);
		g.setSpeed(0.0);
		for(int i=0; i<v.size(); i+=2){
			if((i+1)>=v.size()){
				break;
			}else{
			Double km=CalculosGeograficos.haversine(v.get(i), v.get(i+1));
			g.setSpeed(g.getSpeed()+km);
			}
		}
		v.get(0).setSpeed(null);
		r.add(v.get(0));
		r.add(g);
		return g.getSpeed();
	}
	
	public GPSData kilometraje(Date fechaIni, Date fechaFin,
			String vehiculoImei) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from GPSData g where " +
				" g.imei=:vehiculoImei " +
				" AND g.fecha>=:fechaIni" +
				" AND g.fecha<=:fechaFin " +
				" order by g.id");
		query.setParameter("fechaIni", fechaIni);
		query.setParameter("fechaFin", fechaFin);
		query.setParameter("vehiculoImei", vehiculoImei);
		List<GPSData> v=query.list();
		session.close();
		if(v!=null)
		if(!v.isEmpty()){
		List<GPSData> r=new ArrayList<GPSData>();
		GPSData g=v.get(v.size()-1);
		g.setSpeed(0.0);
		for(int i=0; i<v.size(); i+=2){
			if((i+1)>=v.size()){
				break;
			}else{
			Double km=CalculosGeograficos.haversine(v.get(i), v.get(i+1));
			g.setSpeed(g.getSpeed()+km);
			}
		}
		v.get(0).setSpeed(null);
		r.add(v.get(0));
		r.add(g);
		return g;
		}
		return null;
	}
}
