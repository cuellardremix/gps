package com.cj.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.cj.configuracion.HibernateUtil;
import com.cj.pojos.GPSData;
import com.cj.utils.Constantes;

public class InterfazMonitoreoGeneralDAO {

	public List<GPSData> deteccionDeFueraRuta(List<GPSData> datos){
		List<GPSData> datosEn0=new ArrayList<GPSData>();
		List<GPSData> nuevo=new ArrayList<GPSData>();
		for(GPSData dato:datos){
			if(dato.getEvento()==null){
				dato.setEvento("");
			}
		}
		for(int i=0; i<datos.size();i++){
			if(datos.get(i).getEvento()!=null)
			if(datos.get(i).getEvento().equals(Constantes.fueraDeRuta)){
				int j=i;
				do{
					datosEn0.add(datos.get(j));
					j++;
					if( j==datos.size()){
						break;
					}
				}while(datos.get(j).getEvento().equals(Constantes.fueraDeRuta));
				i=j-1;
			}
//			else{
//				nuevo.add(datos.get(i));
//			}
			if(!datosEn0.isEmpty()){
				Calendar ini=Calendar.getInstance();
				ini.setTime(datosEn0.get(0).getFecha());
				Calendar fini=Calendar.getInstance();
				if(datosEn0.size()==1){
					fini.setTime(datos.get(i).getFecha());
				}else{
					fini.setTime(datosEn0.get(datosEn0.size()-1).getFecha());	
				}
				long dif=this.cantidadTotalSegundos(fini, ini);
				long minutos=dif/60;
				long segundos=dif-(minutos*60);
				datosEn0.get(0).setIo1("Por "+minutos+" minutos "+segundos+" segundos.");
				nuevo.add(datosEn0.get(0));
				datosEn0.clear();
			}
		}
		return nuevo;
	}
	
	public List<GPSData> deteccionDeFueraCerca(List<GPSData> datos){
		List<GPSData> datosEn0=new ArrayList<GPSData>();
		List<GPSData> nuevo=new ArrayList<GPSData>();
		int jTemp=0;
		for(GPSData dato:datos){
			if(dato.getEvento()==null){
				dato.setEvento("");
			}
		}
		for(int i=0; i<datos.size();i++){
			if(datos.get(i).getEvento()!=null)
			if(datos.get(i).getEvento().equals(Constantes.fueraDeCerca)){
				int j=i;
				do{
					datosEn0.add(datos.get(j));
					j++;
					if( j==datos.size()){
						break;
					}
				}while(datos.get(j).getEvento().equals(Constantes.fueraDeCerca));
				i=j-1;
				jTemp=j;
			}
//			else{
//				nuevo.add(datos.get(i));
//			}
			if(!datosEn0.isEmpty()){
				Calendar ini=Calendar.getInstance();
				ini.setTime(datosEn0.get(0).getFecha());
				Calendar fini=Calendar.getInstance();
				if(datosEn0.size()==1){
					fini.setTime(datos.get(i).getFecha());
				}else{
					fini.setTime(datosEn0.get(datosEn0.size()-1).getFecha());	
				}
				long dif=this.cantidadTotalSegundos(fini, ini);
				long minutos=dif/60;
				long segundos=dif-(minutos*60);
				datosEn0.get(0).setIo1("Por "+minutos+" minutos "+segundos+" segundos.");
				nuevo.add(datosEn0.get(0));
				datosEn0.clear();
			}
		}
		if(!nuevo.isEmpty()){
			GPSData jTmepGPS;
			if(datos.size()>(jTemp+1)){
				jTmepGPS=datos.get(jTemp+1);
			}else{
				jTmepGPS=new GPSData();
				jTmepGPS.setFecha(new Date());
			}
		Calendar tem=Calendar.getInstance();
		tem.setTime(nuevo.get(0).getFecha());
		Calendar tem2=Calendar.getInstance();
		tem2.setTime(jTmepGPS.getFecha());
		long dif=InterfazMonitoreoGeneralDAO.cantidadTotalSegundos(tem, tem2);
		long minutos=dif/60;
		long segundos=dif-(minutos*60);
		nuevo.get(0).setIo1("Por "+minutos+" minutos "+segundos+" segundos.");
		}
		return nuevo;
	}
	
	public List<GPSData> deteccionEncendido(List<GPSData> datos){
		List<GPSData> nuevos=new ArrayList<GPSData>();
		for(GPSData dato:datos){
			if(dato.getAlarm().equals(Constantes.encendidoIc) || dato.getAlarm().equals(Constantes.encendido)){
				dato.setIo1("Encendido");
				nuevos.add(dato);
			}
			if(dato.getAlarm().equals(Constantes.apagadoIc) || dato.getAlarm().equals(Constantes.apagado)){
				dato.setIo1("Apagado");
				nuevos.add(dato);
			}
			if(dato.getAlarm().equals(Constantes.puertaAl) || dato.getAlarm().equals(Constantes.alarmaPuerta)){
				dato.setIo1("Alarma de Puerta");
				nuevos.add(dato);
			}
		}
		return nuevos;
	}
	public List<GPSData> getHistorialReciente(Integer numResultados,Double vehVel) {
		Session session = HibernateUtil.getSessionFactory().openSession();
//		Query query = session.createQuery("from GPSData WHERE noLeido=0 AND fecha=sysdate() order by id");
		Query query = session.createQuery("from GPSData WHERE noLeido=0 order by id");
		query.setMaxResults(numResultados);
		List<GPSData> l=query.list();
		for(GPSData dato:l){
			dato.setAlarm(this.procesarColor(dato,vehVel));
		}
		session.close();
		return l;
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
			if(data.getSpeed()==0.0){
				return Constantes.greenMarker;
			}
			if(data.getSpeed()>0.0 && data.getSpeed()<=velVeh){
				return Constantes.blueMarker;
			}
	return null;
	}
	
	public List<GPSData> procesarMarcasParaMostrar(List<GPSData> datos){
		List<GPSData> datosEn0=new ArrayList<GPSData>();
		List<GPSData> nuevo=new ArrayList<GPSData>();
		for(GPSData dato:datos){
			if(dato.getSpeed()==0.0 && dato.getAlarm().equals(Constantes.normal)){
				datosEn0.add(dato);
//				datos.remove(dato);
			}else{
				if(datosEn0.size()>1){
					nuevo.add(datosEn0.get(0));
					nuevo.add(datosEn0.get(datosEn0.size()-1));
					datosEn0.clear();
				}
				nuevo.add(dato);
			}
			
		}
		if(nuevo.isEmpty() && !datosEn0.isEmpty()){
			nuevo.add(datosEn0.get(0));
			nuevo.add(datosEn0.get(datosEn0.size()-1));
		}
		return nuevo;
	}
	
	public List<GPSData> historialHoy(String imei,Date fecha,Double vehVel){
		Session session = HibernateUtil.getSessionFactory().openSession();
		//TODO:agregar setencia para fecha del dia de hoy
		Query query = session.createQuery("from GPSData WHERE imei=:imei AND fecha<=:fecha order by id DESC");
		query.setMaxResults(100);
		query.setParameter("fecha", fecha);
		query.setParameter("imei", imei);
		List<GPSData> l=query.list();
		for(GPSData dato:l){
			dato.setAlarm(this.procesarColor(dato,vehVel));
		}
//		l=this.procesarMarcasParaMostrar(l);
		session.close();
		return l;
	}
	
	public List<GPSData> deteccionDeTiempo(List<GPSData> datos){
		List<GPSData> datosEn0=new ArrayList<GPSData>();
		List<GPSData> nuevo=new ArrayList<GPSData>();
		int jTemp=0;
		for(int i=0; i<datos.size();i++){
			if(datos.get(i).getSpeed()==0.0){
				int j=i;
				do{
//					datos.get(j).setAlarm(Constantes.greenMarker);
					datosEn0.add(datos.get(j));
					j++;
					if( j==datos.size()){
						break;
					}
				}while(datos.get(j).getSpeed()==0.0);
				i=j-1;
				jTemp=j;
			}
			if(!datosEn0.isEmpty()){
				Calendar ini=Calendar.getInstance();
				ini.setTime(datosEn0.get(0).getFecha());
				Calendar fini=Calendar.getInstance();
				if(datosEn0.size()==1){
					fini.setTime(datos.get(i).getFecha());
				}else{
					fini.setTime(datosEn0.get(datosEn0.size()-1).getFecha());	
				}
				long dif=this.cantidadTotalSegundos(fini, ini);
				long minutos=dif/60;
				long segundos=dif-(minutos*60);
				datosEn0.get(0).setIo1("Por "+minutos+" minutos "+segundos+" segundos.");
				nuevo.add(datosEn0.get(0));
				datosEn0.clear();
			}
		}
		if(!nuevo.isEmpty()){
			GPSData jTmepGPS;
			if(datos.size()>(jTemp+1)){
				jTmepGPS=datos.get(jTemp+1);
			}else{
				jTmepGPS=new GPSData();
				jTmepGPS.setFecha(new Date());
			}
		Calendar tem=Calendar.getInstance();
		tem.setTime(nuevo.get(0).getFecha());
		Calendar tem2=Calendar.getInstance();
		tem2.setTime(jTmepGPS.getFecha());
		long dif=InterfazMonitoreoGeneralDAO.cantidadTotalSegundos(tem, tem2);
		long minutos=dif/60;
		long segundos=dif-(minutos*60);
		nuevo.get(0).setIo1("Por "+minutos+" minutos "+segundos+" segundos.");
		}
		return nuevo;
	}
	
	public List<GPSData> deteccionDeNormal(List<GPSData> datos, Double veM){
		List<GPSData> datosEn0=new ArrayList<GPSData>();
		List<GPSData> nuevo=new ArrayList<GPSData>();
//		int jTemp=0;
		for(int i=0; i<datos.size();i++){
			if(datos.get(i).getAlarm().equals(Constantes.blueMarker)){
				int j=i;
				do{
					datosEn0.add(datos.get(j));
					j++;
					if( j==datos.size()){
						break;
					}
				}while(datos.get(j).getAlarm().equals(Constantes.blueMarker) || 
						(datos.get(j).getSpeed()>0.0 && datos.get(j).getSpeed()<=veM));
				i=j-1;
//				jTemp=j;
			}
//			else{
//				nuevo.add(datos.get(i));
//			}
			if(!datosEn0.isEmpty()){
//				System.out.println(datosEn0.get(0).getAlarm()+" "+datosEn0.get(0).getIdRegistro());
				Calendar ini=Calendar.getInstance();
				ini.setTime(datosEn0.get(0).getFecha());
				Calendar fini=Calendar.getInstance();
				if(datosEn0.size()==1){
					fini.setTime(datos.get(i).getFecha());
				}else{
					fini.setTime(datosEn0.get(datosEn0.size()-1).getFecha());	
				}
				long dif=this.cantidadTotalSegundos(fini, ini);
				long minutos=dif/60;
				long segundos=dif-(minutos*60);
				datosEn0.get(0).setIo1("Por "+minutos+" minutos "+segundos+" segundos.");
				nuevo.add(datosEn0.get(0));
				datosEn0.clear();
			}
		}
		if(!nuevo.isEmpty()){
		Calendar tem=Calendar.getInstance();
		tem.setTime(nuevo.get(0).getFecha());
		Calendar tem2=Calendar.getInstance();
		tem2.setTime(nuevo.get(nuevo.size()-1).getFecha());
		long dif=InterfazMonitoreoGeneralDAO.cantidadTotalSegundos(tem, tem2);
		long minutos=dif/60;
		long segundos=dif-(minutos*60);
		nuevo.get(0).setIo1("Por "+minutos+" minutos "+segundos+" segundos.");
		}
		return nuevo;
	}
	
	public List<GPSData> deteccionDeSOS(List<GPSData> datos){
		List<GPSData> nuevo=new ArrayList<GPSData>();
		for(GPSData dato:datos){
			if(dato.getAlarm().equals(Constantes.help)){
				nuevo.add(dato);
			}
		}
		return nuevo;
	}
	
	public List<GPSData> deteccionDeVelocidadMax(List<GPSData> datos){
		List<GPSData> datosEn0=new ArrayList<GPSData>();
		List<GPSData> nuevo=new ArrayList<GPSData>();
		int jTemp=0;
		for(int i=0; i<datos.size();i++){
			if(datos.get(i).getSpeed()>Constantes.maximaVelocidad){
				int j=i;
				do{
					datosEn0.add(datos.get(j));
					j++;
					if( j==datos.size()){
						break;
					}
				}while(datos.get(j).getSpeed()>Constantes.maximaVelocidad);
				i=j-1;
				jTemp=j;
			}
//			else{
//				nuevo.add(datos.get(i));
//			}
			if(!datosEn0.isEmpty()){
				Calendar ini=Calendar.getInstance();
				ini.setTime(datosEn0.get(0).getFecha());
				Calendar fini=Calendar.getInstance();
				if(datosEn0.size()==1){
					fini.setTime(datos.get(i).getFecha());
				}else{
					fini.setTime(datosEn0.get(datosEn0.size()-1).getFecha());	
				}
				long dif=this.cantidadTotalSegundos(fini, ini);
				long minutos=dif/60;
				long segundos=dif-(minutos*60);
				datosEn0.get(0).setIo1("Por "+minutos+" minutos "+segundos+" segundos.");
				nuevo.add(datosEn0.get(0));
				datosEn0.clear();
			}
		}
		if(!nuevo.isEmpty()){
			GPSData jTmepGPS;
			if(datos.size()>(jTemp+1)){
				jTmepGPS=datos.get(jTemp+1);
			}else{
				jTmepGPS=new GPSData();
				jTmepGPS.setFecha(new Date());
			}
		Calendar tem=Calendar.getInstance();
		tem.setTime(nuevo.get(0).getFecha());
		Calendar tem2=Calendar.getInstance();
		tem2.setTime(jTmepGPS.getFecha());
		long dif=InterfazMonitoreoGeneralDAO.cantidadTotalSegundos(tem, tem2);
		long minutos=dif/60;
		long segundos=dif-(minutos*60);
		nuevo.get(0).setIo1("Por "+minutos+" minutos "+segundos+" segundos.");
		}
		return nuevo;
	}
	
	// Suma o resta las horas recibidos a la fecha  
	 public Date sumarRestarHorasFecha(Date fecha, int horas){
	
	      Calendar calendar = Calendar.getInstance();
	      calendar.setTime(fecha); // Configuramos la fecha que se recibe
	      calendar.add(Calendar.HOUR, horas);  // numero de horas a añadir, o restar en caso de horas<0
		      return calendar.getTime(); // Devuelve el objeto Date con las nuevas horas añadidas
	
	 }
	 
	 /*Metodo que calcula la diferencia de los minutos entre dos fechas */ 
	 public static long diferenciaMinutos(Calendar fechaInicial ,Calendar fechaFinal){  
		 long diferenciaHoras=0; diferenciaHoras=(fechaFinal.get(Calendar.MINUTE)-fechaInicial.get(Calendar.MINUTE)); return diferenciaHoras; 
		 }
	 
	 /*Metodo que devuelve el Numero total de Segundos que hay entre las dos Fechas */ 
	 public static long cantidadTotalSegundos(Calendar fechaInicial ,Calendar fechaFinal){  
		 long totalMinutos=0; 
		 totalMinutos=((fechaFinal.getTimeInMillis()-fechaInicial.getTimeInMillis())/1000); 
		 return totalMinutos; 
		 }
}
