package com.cj.servidor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import com.cj.pojos.Comando;



public class Servidor {

	
	private int serverPort = 0;
	private ServerSocket serverSock = null;
	private Socket sock = null;

	public Servidor(int serverPort) throws IOException {
		this.serverPort = serverPort;
		serverSock = new ServerSocket();
		serverSock.setReuseAddress(true);
		serverSock.bind(new InetSocketAddress(serverPort));
	}

	public void waitForConnections() {
		System.out.println("esperando... puerto: "+this.serverPort);
//		while (true) {
			try {
				sock = serverSock.accept();
				System.out
						.println("Server: Acepto nuevo socket, creando handler."+" "+this.serverPort);
				ManejadorServidor handler = new ManejadorServidor(sock);
				serverSock.close();
				handler.procesar();
				System.out
						.println("Server: termino con el socket, espero nueva conexión.");
			} catch (IOException e) {
				e.printStackTrace(System.err);
			}
//		}
	}


	
}
