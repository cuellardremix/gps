package com.cj.lanzador;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.cj.configuracion.HibernateUtil;
import com.cj.dao.InterfazDAO;
import com.cj.pojos.Vehiculo;
import com.cj.utils.Constantes;

public class GPSListener implements Job{

	private Thread procesoLecturaGPS;// = new Thread(new Lanzador(this.comando,historial));
	private List<Vehiculo> vehiculos;
	private InterfazDAO interfazDAO=new InterfazDAO();
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
			System.out.println("iniciando quartz");
			
			vehiculos=this.leerVehiculos();
			if(!vehiculos.isEmpty()){
			this.actualizarStatusVehiculo(vehiculos,true);
			
			for(Vehiculo v:vehiculos){
				interfazDAO.updateComandoActual(v.getVehImei(), Constantes.leerPosicion);
				v.setVehComAct(Constantes.leerPosicion);
				procesoLecturaGPS=new Thread(new Lanzador(v.getVehPuerto()));
				procesoLecturaGPS.start();
				System.out.println("Iniciado Hilo para "+v.getVehImei()+" por puerto: "+v.getVehPuerto());
			}
			}
			
		
	}

//	public Comando obtenerComando(){
//		Session session = HibernateUtil.getSessionFactory().openSession();
//		Query query = session.createQuery("from Comando where charComando='B'");
//		query.setMaxResults(1);
//		Comando c=(Comando)query.uniqueResult();
//		session.close();
//		return c;
//	}
	
	public void actualizarStatusVehiculo(List<Vehiculo> vehiculosLeidos,Boolean status){
		Session session = HibernateUtil.getSessionFactory().openSession();
		for(Vehiculo v:vehiculosLeidos){
//			v.setStatusProceso(status);
			session.update(v);
		}
		session.close();
	}
	
	public List<Vehiculo> leerVehiculos(){
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from Vehiculo v WHERE v.estadoRegistro=true order by v.id");
//		query.setMaxResults(5);
		List<Vehiculo> l=query.list();
		session.close();
		return l;
		
	}
	
}
