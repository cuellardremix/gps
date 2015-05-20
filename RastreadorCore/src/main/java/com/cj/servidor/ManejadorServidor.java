package com.cj.servidor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.hibernate.Query;
import org.hibernate.Session;

import com.cj.catalogosDAO.AdminCercasDAO;
import com.cj.configuracion.HibernateUtil;
import com.cj.dao.InterfazDAO;
import com.cj.decodificadores.Gps103ProtocolDecoder;
import com.cj.geografia.CalculosGeograficos;
import com.cj.pojos.Cerca;
import com.cj.pojos.Configuracion;
import com.cj.pojos.GPSData;
import com.cj.pojos.Vehiculo;
import com.cj.utils.Constantes;

public class ManejadorServidor {

	private Socket sock = null;
	private InputStream sockInput = null;
	private OutputStream sockOutput = null;

	public ManejadorServidor(Socket sock) throws IOException {
		this.sock = sock;
		sockInput = sock.getInputStream();
		sockOutput = sock.getOutputStream();
		
		System.out.println("Handler: nuevo handler creado.");
	}

	

	public void procesar() {
		System.out.println("Handler: Handler run() iniciando.");
		String[] valores=new String[1];
		
		try {
		do{

			byte[] buf = new byte[2048];
			int bytes_read = 0;

		
				bytes_read = sockInput.read(buf, 0, buf.length);
				if (bytes_read < 0) {
					System.err
							.println("Handler: Intento leer el socket, read() devuelve < 0,  cerrando el socket.");
					break;
				}

				String str = new String(buf, "UTF-8");
				// ESCRIBO LO QUE RECIBO
				System.out.println("Paquete recibido: "
						+ new String(buf, 0, buf.length));
				this.guardarGPSData(str);
//				
//				// ABRO LA CONEXIÃ“N CON LA DB
//				LServerDBConn con = new LServerDBConn();
//				// LE PASO EL PAQUETE PARA QUE INSERTE
//				con.connectionText(new String(buf, 0, bytes_read));

				// CREO EL ACKNOWLEDGE
				String paquete = new String(buf, 0, bytes_read);
				valores = paquete.split("[,]");
				if (valores[0].equals("##")) {
					System.out.println("Recibo login.");
					String ack_packet = "LOAD";
					byte[] ack = ack_packet.getBytes();
					sockOutput.write(ack, 0, ack.length);
					sockOutput.flush();

					// ESCRIBO LO QUE ENVIO
					System.out.println("LOGIN correcto.");
					ack = null;
					ack_packet = "";
				} else {
					String IMEI = valores[0];
					String ack_packet = "**," + IMEI + ","+this.obtenerComandoActual(IMEI)+";";

					// ACÃ� SE DEVUELVE EL ACK AL SOCKET
					byte[] ack = ack_packet.getBytes();
					sockOutput.write(ack, 0, ack.length);
					sockOutput.flush();

					// ESCRIBO LO QUE ENVIO
//					System.out.println("ACK enviado: "
//							+ new String(ack, 0, ack.length));
					ack = null;
					ack_packet = "";
					Thread.sleep(1000*5);
				}

			
//			Thread.sleep(1000*this.obtenerRetraso());
		}while(true);

		try {
			System.out.println("Handler: Cerrando el socket."+sock.getPort()+" "+sock.getLocalPort());
			sockInput.close();
			sockOutput.close();
			sock.close();
			
		} catch (Exception e) {
			System.err.println("Handler: Error cerrando el socket, e= " + e);
			e.printStackTrace(System.err);
		}
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}finally{
			System.out.println("Handler: Cerrando el socket."+sock.getPort()+" "+sock.getLocalPort());
			
			try {
				sockOutput.close();
				sock.close();
				sockInput.close();
				HibernateUtil.getSessionFactory().getCurrentSession().close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	public void guardarGPSData(String strGps){
		Gps103ProtocolDecoder decoder=new Gps103ProtocolDecoder();
		 List<GPSData> gpsData=decoder.decodificar(strGps);
		 
		 
		 if(gpsData!=null){
			 
			 if(!gpsData.isEmpty()){
				 for(GPSData gps:gpsData){
					 gps.setNoLeido(true);
					 
					 if(gps.getAlarm().equals(Constantes.help) || gps.getAlarm().equals(Constantes.alarmaPuerta)
							 || gps.getAlarm().equals("oil") || gps.getAlarm().equals("acc alarm")){
						 this.updateComando(gps.getImei(), 'E');
					 }
					 else if(gps.getAlarm().equals("et")){
						 this.updateComando(gps.getImei(), 'B');
					 }
					 
					 GPSData ultimo=this.ultimoInsertado(gps.getImei());

//					 if(!(ultimo.getLatitude()==gps.getLatitude()
//							 && ultimo.getLongitude()==gps.getLongitude())){
					 if(ultimo!=null){
						 if(!(ultimo.getSpeed()==0.0 && gps.getSpeed()==0.0 && (gps.getAlarm().equals(Constantes.normal) 
								 || gps.getAlarm().equals("dt")
								 || gps.getAlarm().equals("et")
								 || gps.getAlarm().equals("acc alarm")
								 || gps.getAlarm().equals("low battery")
								 || gps.getAlarm().equals("oil")))){
							 if(gps.getIo3()!=null){
								 Double gas1=new Double(gps.getIo3().substring(0,gps.getIo3().length()-1));
								 if(gas1!=0.0){
									 gas1=100.0-gas1;
									 gps.setIo3(gas1.toString()+"%");	 
								 }
							 }
							 this.isInCerca(gps);
					         Session session = HibernateUtil.getSessionFactory().openSession();
						        session.beginTransaction();
						        session.save(gps);
						        session.getTransaction().commit();
						        session.close();	 
						 }	 
					 }else{
						 Session session = HibernateUtil.getSessionFactory().openSession();
					        session.beginTransaction();
					        session.save(gps);
					        session.getTransaction().commit();
					        session.close();
					 }
					 
					 

					 
				 }
			 }
		 }		 
	}

	public void updateComando(String imei, Character comando){
		InterfazDAO interfazDAO=new InterfazDAO();
		interfazDAO.updateComandoActual(imei, comando);
	}
	
	public Character obtenerComandoActual(String imei){
		
		if(imei.endsWith(";")){
			imei=imei.substring(0,imei.length()-1);
		}
		if(imei.startsWith(Constantes.cabezaImei)){
			imei=imei.replaceAll(Constantes.cabezaImei, "");
		}
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from Vehiculo WHERE vehImei=:imei");
		query.setParameter("imei", imei);
		query.setMaxResults(1);
		Vehiculo vehic=(Vehiculo)query.uniqueResult();
		session.close();
		if(vehic==null){
			return 'B';
		}else
		return vehic.getVehComAct();
	}

	public Integer obtenerRetraso(){
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from Configuracion");
		
		query.setMaxResults(1);
		Configuracion c=(Configuracion)query.uniqueResult();
		session.close();
		
		if(c==null){
			return 5;
		}else{
		return c.getRetraso();
		}
	}

	public void isInCerca(GPSData punto) {
		AdminCercasDAO cercasDAO=new AdminCercasDAO();
		InterfazDAO interfazDAO=new InterfazDAO();
			Vehiculo vehiculo=interfazDAO.obtenerVehiculo(punto.getImei());
			List<Cerca> cercas = cercasDAO.obtenerCercaPorVehiculo(vehiculo.getVehLla()
					);
			if(cercas!=null){
			for(Cerca cerca:cercas){
			List<GPSData> datosCerca = cercasDAO.obtenerMarcasPorCerca(cerca.getGeoLla());

			if (!CalculosGeograficos.isInPolygon(datosCerca, punto)) {
				
				punto.setEvento(Constantes.fueraDeCerca);
				
				Session session = HibernateUtil.getSessionFactory().openSession();
		        session.beginTransaction();
		        session.saveOrUpdate(punto);
		        session.getTransaction().commit();
		        session.close();
				
				
			}
			}
		}
		
	}
	
	public GPSData ultimoInsertado(String imei){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from GPSData WHERE imei=:imei order by id DESC");
		query.setParameter("imei", imei);
		query.setMaxResults(1);
		GPSData g=(GPSData)query.uniqueResult();
		session.close();
		return g;
	}
	
	
}
