package com.cj.utils;

public class Constantes {
	//Login
	public static String us;
	public static String pa;
	
	//Comandos para detener y activar el auto
	public static Character detener='J';
	public static Character activar='K';
	public static Character leerPosicion='B';
	public static Integer maxResultsMostrar=25;
	public static String cabezaImei="imei:";
	
	//Colores de Lineas
	public static String color1="#00b159";
	public static String color2="#00aff0";
	public static String color3="#ce260b";
	public static String color4="#b23aee";
	public static String color5="#ffff66";
	
	//Marcas del mapa
	public static String dirImagenes="../resources/imagenes/";
	public static String redMarker=dirImagenes+"MarkerRed.png";
	public static String blueMarker=dirImagenes+"markerBlue.png";
	public static String yellowMarker=dirImagenes+"markerYellow.png";
	public static String greenMarker=dirImagenes+"markerGreen.png";
	public static String sos=dirImagenes+"sos.png";
	public static String inicio=dirImagenes+"inicio.png";
	public static String detenidoI=dirImagenes+"detenido.png";
	public static String continuar=dirImagenes+"continuar.png";
	public static String puertaAl=dirImagenes+"doorAlarm.png";
	public static String fueraRutaIc=dirImagenes+"fuera.gif";
	public static String fueraCercaIc=dirImagenes+"fueraCerca.png";
	public static String encendidoIc=dirImagenes+"turnOn.png";
	public static String apagadoIc=dirImagenes+"turnOff.png";
	
	//Colores de las cercas
	public static String cercaRoja="#FF0000";
	public static String cercaAmarilla="#FFFF00";
	public static String cercaVerde="#00CC00";
	public static String colorAzul=" 0000FF";

	//demas
	public static String marca="Marca";
	public static String exito="Operacion Realizada con Exito";
	public static String fallo="Fallo en la operación";
	public static String noParametrosBusqueda="Debe ingresar un parametro de Busqueda";
	public static String noResultadosDeBusqueda="No hay resultados de busqueda";
	public static Integer maxResultsCyR=15;
	
	//Mensajes del gps
	public static String normal="tracker";
	public static String help="help me";
	public static String detenido="jt";
	public static String reActivado="kt";
	public static String alarmaPuerta="door alarm"; 
	public static Double velocidadEstacionado=0.0;
	public static String encendido="acc on";
	public static String apagado="acc off";
	
	//mensaje agregados, eventos especiales no de hardware
	public static String fueraDeRuta="fueraRuta";
	public static String fueraDeCerca="fueraCerca";
	public static String fueraDeTiempoRuta="retTiempoRuta";
	public static String bajaDeGasolina="bajaGasolina";
	public static String llenadoDeGasolina="llenadoGasolina";
	public static int maxResults=75;
	
	//direcciones de brujula
	public static String dir0=dirImagenes+"0.png";
	public static String dir45=dirImagenes+"45.png";
	public static String dir90=dirImagenes+"90.png";
	public static String dir135=dirImagenes+"135.png";
	public static String dir180=dirImagenes+"180.png";
	public static String dir225=dirImagenes+"225.png";
	public static String dir270=dirImagenes+"270.png";
	public static String dir315=dirImagenes+"315.png";
	
	//punto azul
	public static String bluePoint=dirImagenes+"bluePoint.gif";
	public static Double maximaVelocidad=80.0;
	
	//perfiles
	public static Integer perfilAdministrador=1;
	public static Integer perfilCliente=7;
	
}
