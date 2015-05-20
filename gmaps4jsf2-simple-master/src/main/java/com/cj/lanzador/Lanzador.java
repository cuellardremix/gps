package com.cj.lanzador;

import java.io.IOException;
import java.util.List;

import com.cj.Interfaz.Interfaz;
import com.cj.pojos.Comando;
import com.cj.pojos.GPSData;
import com.cj.servidor.Servidor;

public class Lanzador implements Runnable {

	
	private Integer puerto;
	
	public Lanzador(Integer puerto){
		this.puerto=puerto;
	}
	
	public void lanzar() {
		
		Servidor server = null;
		try {
			server = new Servidor(puerto);
		} catch (IOException e) {
//			e.printStackTrace(System.err);
			System.err.println("Puerto "+puerto+" utilizado");
		}
		if(server!=null)
		server.waitForConnections();
	}


	public void run() {
			lanzar();
	}

	

}
