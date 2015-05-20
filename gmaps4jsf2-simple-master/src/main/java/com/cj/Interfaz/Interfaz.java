package com.cj.Interfaz;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.primefaces.component.carousel.Carousel;
import org.primefaces.model.chart.PieChartModel;

import com.cj.InterfacesReportes.ActividadVehiculo;
import com.cj.catalogosDAO.AdminCercasDAO;
import com.cj.catalogosDAO.AdminTurnosDAO;
import com.cj.dao.InterfazCercaPolDAO;
import com.cj.dao.InterfazDAO;
import com.cj.dao.InterfazLoginDAO;
import com.cj.dao.InterfazRutaDAO;
import com.cj.geografia.CalculosGeograficos;
import com.cj.pojos.ControlRuta;
import com.cj.pojos.GPSData;
import com.cj.pojos.Ruta;
import com.cj.pojos.Turno;
import com.cj.pojos.Usuario;
import com.cj.pojos.Vehiculo;
import com.cj.utils.Constantes;
import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.LatLng;
import com.googlecode.gmaps4jsf.component.eventlistener.EventListener;
import com.googlecode.gmaps4jsf.component.map.Map;
public class Interfaz {



	private boolean initial = true;

	private InterfazDAO interfazDAO=new InterfazDAO();
	public Character comando = 'B';
	private Integer banderaCambios;
	public static Boolean refrescar=false;
	public static Integer conteoActualizados=0;
	private Map map=this.iniciarMapa();
	InterfazRutaDAO interfazRutaDAO = new InterfazRutaDAO();
	private AdminCercasDAO cercasDAO=new AdminCercasDAO();
	private AdminTurnosDAO turnosDAO=new AdminTurnosDAO();
	private List<Turno> turnosActuales;
	private String turnos;
	private InterfazCercaPolDAO interfazCercaDAO = new InterfazCercaPolDAO();

	
	private Usuario usuario;
	private String usuarioA;
	private String contraA;
	
	//Mapa A
//	private List<String> vehiculos;
	private HashMap<String, String> vehiculos;
	private List<String> vehiculoSeleccionado;
	private String vehiculoComando;
	private List<Vehiculo> vehiculosAct;
	
	private SelectItem[] comandos=interfazDAO.comandosToItems();
	private Character comandoSeleccionado;
	private Boolean stop=false;
	private Date fechaA=new Date();
	
	private HashMap<String, Integer> rutas;
	private Integer rutaSeleccionada;
	private GPSData inicioRuta;
	private List<GPSData> finesRuta=new ArrayList<GPSData>();
	private Boolean controlRuta=false;
	private String vehiculosActivos;

	
	private HashMap<String, Integer> cercas;
	private Integer cercaSeleccionada;

	private HashMap<String, Integer> ultimoIdA;
	private HashMap<String, Boolean> corA;
	private HashMap<String,Integer> corAInt;
	//Mapa B
	private HashMap<String, String> vehiculosB;
	private String vehiculoSeleccionadoB;

	private SelectItem[] comandosB=interfazDAO.comandosToItems();
	private Character comandoSeleccionadoB;
	private Date fechaB=new Date();
	
	private HashMap<String, Integer> rutasB;
	private Integer rutaSeleccionadaB;
	private GPSData inicioRutaB;
	private List<GPSData> finesRutaB;
	private Boolean controlRutaB=false;
	private String vehiculosActivosB;
	
	private HashMap<String, Integer> cercasB;
	private Integer cercaSeleccionadaB;
	
	
	private List<GPSData> historial;
	InterfazCercas icer=new InterfazCercas();


	private GPSData gpsDataF;// = interfazDAO.ultimaposicion(vehiculoSeleccionado);
	private String zoom;

	private List<String> colores;
	private HashMap<String,String> coloresAsignados;
	
	private EventListener el;
	List<GPSData> gps=null;
	HashMap<String,GPSData> gpsEA=null;
	List<GPSData> vehiculosSe;
	private PieChartModel pieModel1;
	private Carousel mostrar;
	private static Geocoder geocoder;
	private boolean mostrarCerca;
	
	@PostConstruct
	public String init(){
		mostrarCerca=false;
		if(this.usuario!=null){
		zoom="11";
		
		this.vehiculosSe=new ArrayList<GPSData>();
		
		el=new EventListener();
		el.setEventName("center_changed");
		el.setJsFunction("changeMapZoom");
		colores=new ArrayList<String>();
		colores.add(Constantes.color1);
		colores.add(Constantes.color2);
		colores.add(Constantes.color3);
		colores.add(Constantes.color4);
		colores.add(Constantes.color5);
		
		this.coloresAsignados=new HashMap<String,String>();
		
		gpsDataF=new GPSData(19.055663,-98.154700);
		List<GPSData> gps=null;
		setMap(this.iniciarMapa());
		if(vehiculoSeleccionado!=null){
			gps=interfazDAO.ultimasPosiciones(vehiculoSeleccionado);
			this.vehiculosAct=interfazDAO.obtenerVehiculos(vehiculoSeleccionado);
			setCercas(this.cercasDAO.obtenerCercasString(cercasDAO.obtenerCercasPorVehiculos(vehiculosAct)));
			this.gpsEA=interfazDAO.ultimoEncendidoApagado(vehiculoSeleccionado);
			FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("panelA");
			}
		
		this.ultimoIdA=new HashMap<String,Integer>();
		this.corA=new HashMap<String,Boolean>();
		this.corAInt=new HashMap<String, Integer>();
		//para borrado
		if(gps!=null)
			for(GPSData g:gps){
				if(g!=null){
					this.ultimoIdA.put(g.getImei(), g.getIdRegistro());	
					this.corA.put(g.getImei(), false);
					this.corAInt.put(g.getImei(), 0);	
					 Random  rnd = new Random();
					 int c=rnd.nextInt(5);
					 this.coloresAsignados.put(g.getImei(), colores.get(c));
					 if(gpsEA.get(g.getImei())!=null)
					 g.setAlarm(gpsEA.get(g.getImei()).getAlarm());
					 g.setIo4("background-color: "+this.coloresAsignados.get(g.getImei())+"; height: 3px;");
					 g.setIo1(Interfaz.getKeyByValue(vehiculos, g.getImei()));
					 try {
						g.setIo2(Interfaz.obtenerDireccion(g).replace(",", "</br>"));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 
					 	String pattern="####.##";
					 	g.setSpeed(new Double(g.getSpeed().toString().replace(',','.')));
						DecimalFormat df=new DecimalFormat(pattern);
						g.setSpeed(new Double(df.format(g.getSpeed())));					 
					 String lE="";
					 Double gas=null;
					 if(g.getIo3()!=null){
					 	lE=ActividadVehiculo.porcentajeDouble(g.getIo3()).toString();
					 	gas=ActividadVehiculo.porcentajeDouble(g.getIo3());
					 	g.setIo3(df.format(gas)+", "+lE+"(Lt.)");
					 }

					 	
						this.vehiculosSe.add(g);
//						this.createPieModel1(g);
						FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("mostrar");
				}
			}
			
		//para brrado
		if(gps!=null)
		for(GPSData g:gps){
			List<GPSData> noLeidos = new ArrayList<GPSData>();
			if(g!=null){
				//en lugar de g.getFecha un new Date()
				noLeidos=interfazDAO.addMarksUnreaded(g.getImei(),g.getFecha(),Constantes.maxResults);	
			}
//			else{
//				noLeidos=interfazDAO.addMarksUnreaded(g.getImei(),new Date(),Constantes.maxResults);
//			}	
		
			this.turnosActuales=this.turnosDAO.obtenerTurnoActual();
			
		
			if(!noLeidos.isEmpty()){
//				if(!this.getMap().getChildren().isEmpty())
//				setMap(this.iniciarMapa());

				if(this.controlRuta){
					this.finRuta(noLeidos);
				}

				 Random  rnd = new Random();
				 int c=rnd.nextInt(5);
				
				setMap(interfazDAO.agregarMarcas(noLeidos, this.getMap(),g.getImei(),false,coloresAsignados.get(g.getImei()),false));
					FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("gmap");
		
//					if(noLeidos.size()!=0)
//					this.gpsData=this.interfazDAO.ultimaposicion(vehiculoSeleccionado);
			}
		}//for
		return "";
		}else{
			return "/index.xhtml?faces-redirect=true";
		}
	}
	
	public static <T, E> T getKeyByValue(java.util.Map<T, E> map, E value) {
	    for (Entry<T, E> entry : map.entrySet()) {
	        if (value.equals(entry.getValue())) {
	            return entry.getKey();
	        }
	    }
	    return null;
	}
	
	public Interfaz(){
		
		this.usuario=this.obtenerUsuario();
		Usuario u=this.getUsuario();
		if(u!=null)
		this.vehiculos=interfazDAO.obtenerVehiculosString(u);
		//this.vehiculoSeleccionado=this.primerVehiculo();
		
		
//		setCercas(this.cercasDAO.obtenerCercasString());
		if(u!=null)
		setRutas(interfazRutaDAO.obtenerRutasString(getUsuario()));
		
	}
	


	public Carousel getMostrar() {
		return mostrar;
	}

	public void setMostrar(Carousel mostrar) {
		this.mostrar = mostrar;
	}

	public List<GPSData> getVehiculosSe() {
		return vehiculosSe;
	}

	public void setVehiculosSe(List<GPSData> vehiculosSe) {
		this.vehiculosSe = vehiculosSe;
	}

	public List<GPSData> getGps() {
		return gps;
	}

	public void setGps(List<GPSData> gps) {
		this.gps = gps;
	}

	public String getVehiculoComando() {
		return vehiculoComando;
	}

	public void setVehiculoComando(String vehiculoComando) {
		this.vehiculoComando = vehiculoComando;
	}

	public String getZoom() {
		return zoom;
	}

	public void setZoom(String zoom) {
		this.zoom = zoom;
	}

	public String getUsuarioA() {
		return usuarioA;
	}
	public void setUsuarioA(String usuarioA) {
		this.usuarioA = usuarioA;
	}

	public String getContraA() {
		return contraA;
	}

	public void setContraA(String contraA) {
		this.contraA = contraA;
	}

	public Boolean getStop() {
		return stop;
	}

	public void setStop(Boolean stop) {
		this.stop = stop;
	}

	public HashMap<String, Integer> getCercas() {
		
		return cercas;
	}

	public HashMap<String, Integer> getCercasB() {
		return cercasB;
	}

	public void setCercasB(HashMap<String, Integer> cercasB) {
		this.cercasB = cercasB;
	}

	public Integer getCercaSeleccionadaB() {
		return cercaSeleccionadaB;
	}

	public void setCercaSeleccionadaB(Integer cercaSeleccionadaB) {
		this.cercaSeleccionadaB = cercaSeleccionadaB;
	}

	public void setCercas(HashMap<String, Integer> cercas) {
		this.cercas = cercas;
	}

	public Integer getCercaSeleccionada() {
		return cercaSeleccionada;
	}

	public void setCercaSeleccionada(Integer cercaSeleccionada) {
		this.cercaSeleccionada = cercaSeleccionada;
	}

	public GPSData getInicioRuta() {
		return inicioRuta;
	}

	public void setInicioRuta(GPSData inicioRuta) {
		this.inicioRuta = inicioRuta;
	}

	public HashMap<String, Integer> getRutasB() {
		return rutasB;
	}

	public void setRutasB(HashMap<String, Integer> rutasB) {
		this.rutasB = rutasB;
	}

	public Integer getRutaSeleccionadaB() {
		return rutaSeleccionadaB;
	}

	public void setRutaSeleccionadaB(Integer rutaSeleccionadaB) {
		this.rutaSeleccionadaB = rutaSeleccionadaB;
	}

	public GPSData getInicioRutaB() {
		return inicioRutaB;
	}

	public void setInicioRutaB(GPSData inicioRutaB) {
		this.inicioRutaB = inicioRutaB;
	}

	public List<GPSData> getFinesRutaB() {
		return finesRutaB;
	}

	public void setFinesRutaB(List<GPSData> finesRutaB) {
		this.finesRutaB = finesRutaB;
	}

	public Boolean getControlRutaB() {
		return controlRutaB;
	}

	public void setControlRutaB(Boolean controlRutaB) {
		this.controlRutaB = controlRutaB;
	}

	public String getVehiculosActivosB() {
		return vehiculosActivosB;
	}

	public void setVehiculosActivosB(String vehiculosActivosB) {
		this.vehiculosActivosB = vehiculosActivosB;
	}

	public String getTurnos() {
		return turnos;
	}

	public void setTurnos(String turnos) {
		this.turnos = turnos;
	}

	public List<Turno> getTurnosActuales() {
		return turnosDAO.obtenerTurnoActual();
	}

	public void setTurnosActuales(List<Turno> turnosActuales) {
		this.turnosActuales = turnosActuales;
	}

	public Usuario getUsuario() {
		return this.obtenerUsuario();
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getVehiculosActivos() {
		return vehiculosActivos;
	}

	public void setVehiculosActivos(String vehiculosActivos) {
		this.vehiculosActivos = vehiculosActivos;
	}

	public HashMap<String, Integer> getRutas() {
		return rutas;
	}

	public void setRutas(HashMap<String, Integer> rutas) {
		this.rutas = rutas;
	}

	public Integer getRutaSeleccionada() {
		return rutaSeleccionada;
	}

	public void setRutaSeleccionada(Integer rutaSeleccionada) {
		this.rutaSeleccionada = rutaSeleccionada;
	}

	public Date getFechaB() {
		return fechaB;
	}

	public void setFechaB(Date fechaB) {
		this.fechaB = fechaB;
	}

	public Date getFechaA() {
		return fechaA;
	}

	public void setFechaA(Date fechaA) {
		this.fechaA = fechaA;
	}

	public String primerVehiculo(){
		if(vehiculos!=null)
		if(!vehiculos.isEmpty()){
			String t=this.interfazDAO.obtenerVehiculos(getUsuario()).get(0).getVehImei();
			return t;
		}
		return null;
	}
	
	public String primerVehiculoB(){
		if(!vehiculosB.isEmpty())
			return vehiculosB.get(0);
		return null;
	}


	public GPSData getGpsDataF() {
		return gpsDataF;
	}

	public void setGpsDataF(GPSData gpsDataF) {
		this.gpsDataF = gpsDataF;
	}

	public SelectItem[] getComandosB() {
		return comandosB;
	}

	public void setComandosB(SelectItem[] comandosB) {
		this.comandosB = comandosB;
	}

	public Character getComandoSeleccionadoB() {
		return comandoSeleccionadoB;
	}

	public void setComandoSeleccionadoB(Character comandoSeleccionadoB) {
		this.comandoSeleccionadoB = comandoSeleccionadoB;
	}

	public HashMap<String,String> getVehiculosB() {
		return interfazDAO.obtenerVehiculosString(this.getUsuario());
	}

	public void setVehiculosB(HashMap<String,String> vehiculosB) {
		this.vehiculosB = vehiculosB;
	}

	public String getVehiculoSeleccionadoB() {
		return vehiculoSeleccionadoB;
	}

	public void setVehiculoSeleccionadoB(String vehiculoSeleccionadoB) {
		this.vehiculoSeleccionadoB = vehiculoSeleccionadoB;
	}

	public Character getComandoSeleccionado() {
		return comandoSeleccionado;
	}

	public void setComandoSeleccionado(Character comandoSeleccionado) {
		this.comandoSeleccionado = comandoSeleccionado;
	}

	public SelectItem[] getComandos() {
		return comandos;
	}

	public void setComandos(SelectItem[] comandos) {
		this.comandos = comandos;
	}

	public HashMap<String,String> getVehiculos() {
		Usuario u=this.getUsuario();
		if(u!=null)
		return interfazDAO.obtenerVehiculosString(u);
		else
			return null;
//		return this.vehiculos;
	}

	public void setVehiculos(HashMap<String,String> vehiculos) {
		this.vehiculos = vehiculos;
	}

	public List<String> getVehiculoSeleccionado() {
		return vehiculoSeleccionado;
	}

	public void setVehiculoSeleccionado(List<String> vehiculoSeleccionado) {
		this.vehiculoSeleccionado = vehiculoSeleccionado;
	}

	public Integer getBanderaCambios() {
		return banderaCambios;
	}

	public void setBanderaCambios(Integer banderaCambios) {
		this.banderaCambios = banderaCambios;
	}

	public List<GPSData> getHistorial() {
		return historial;
	}

	public void setHistorial(List<GPSData> historial) {
		this.historial= historial;
	}

	public boolean getInitial() {
		return initial;
	}


	
	public String resetA(){
		return this.cambiarVehiculo();
//		setMap(this.iniciarMapa());
//		if(this.vehiculoSeleccionado.equals(this.vehiculoSeleccionadoB)){
//			this.map.getChildren().clear();
//			this.mapB.getChildren().clear();
//		}else{
//			this.map.getChildren().clear();
//		}
//		if(this.fechaA==null){
//			GPSData gps=interfazDAO.ultimaposicion(vehiculoSeleccionado);
//			if(gps!=null)
//			fechaA=gps.getFecha();
//			else
//				fechaA=new Date();
//		}
//		List<GPSData> historialReciente=interfazDAO.addMarksUnreaded(vehiculoSeleccionado);//.historialReciente(Constantes.maxResults,vehiculoSeleccionado,fechaA);
		
//		interfazDAO.agregarCercas(cercas, this.map,historialReciente);
//		if(!historialReciente.isEmpty()){
//			List<GPSData> en0=monitoreoGeneral.deteccionDeTiempo(historialReciente);
//			historialReciente=interfazDAO.eliminarVelocidad0(historialReciente);
//			historialReciente.addAll(en0);
//			interfazDAO.agregarMarcas(historialReciente, map,vehiculoSeleccionado);	
//		}
    	
//		interfazDAO.eliminarIcon(map);
//		FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("gmap");
	}

    
    public void listen(){
        FacesContext context = FacesContext.getCurrentInstance();
        java.util.Map<String,String> params = context.getExternalContext().getRequestParameterMap();
String latitude=params.get("latitud");
String longitude=params.get("longitud");
String zoom=params.get("zoom");
if(latitude!=null && longitude!=null && zoom!=null){
        this.gpsDataF.setLatitude(Double.parseDouble(latitude));
        this.gpsDataF.setLongitude(Double.parseDouble(longitude));
        this.setZoom(zoom);
}
        
//        System.out.println(params.get("latitud")+" "+params.get("longitud")+ " "+params.get("zoom"));
    }
    
    public void refresh(){
    	if(this.vehiculosSe!=null)
    	this.vehiculosSe.clear();
    	if(this.gpsDataF!=null){
    		this.map.setLatitude(this.gpsDataF.getLatitude().toString());
        	this.map.setLongitude(this.gpsDataF.getLongitude().toString());	
    	}
    	this.map.setZoom(this.zoom);
    	int pos=this.mostrar.getFirstVisible();
    	
    	//		interfazDAO.isInRuta();
    	//		this.controlTiempoRuta();
    	
    	if(vehiculoSeleccionado!=null){
    		gps=interfazDAO.ultimasPosiciones(vehiculoSeleccionado);
    		this.gpsEA=interfazDAO.ultimoEncendidoApagado(vehiculoSeleccionado);
    	}
    	List<GPSData> noLeidos=new ArrayList<GPSData>();
    	if(gps!=null){
    		if(!this.getMap().getChildren().isEmpty()){
    			
    			if(el!=null){
    			getMap().getChildren().clear();
    			this.el.setId("pp");
    			getMap().getChildren().add(this.el);
    			}
    		}
//    		if(this.vehiculosAct!=null)
//    		interfazDAO.isInCerca(gps,this.vehiculosAct);
    	}
    	if(gps!=null)
    		for(GPSData g:gps){
    			if(g!=null){
    				if(this.ultimoIdA!=null){
    					Integer ultimoId=this.ultimoIdA.get(g.getImei());
    					if(ultimoId!=null)
    						if(ultimoId.equals(g.getIdRegistro())){
    							noLeidos=interfazDAO.addMarksUnreaded(g.getImei(),g.getFecha(),1);	
    							corA.put(g.getImei(), true);
    							corAInt.put(g.getImei(),0);
    						}else{
    							Integer t=corAInt.get(g.getImei())+1;
    							corAInt.put(g.getImei(), t);
    							boolean cor=corA.get(g.getImei());
    							if(cor){
    								noLeidos=interfazDAO.addMarksUnreaded(g.getImei(),g.getFecha(),corAInt.get(g.getImei()));	
    							}else{
    								noLeidos=interfazDAO.addMarksUnreaded(g.getImei(),g.getFecha(),Constantes.maxResults);
    							}


    							if(corAInt.get(g.getImei()).equals(Constantes.maxResults)){
    								corA.put(g.getImei(),false);
    							}
    						}					
    				}
    				if(g!=null){
    				g.setIo1(Interfaz.getKeyByValue(vehiculos, g.getImei()));
    				try {
						g.setIo2(Interfaz.obtenerDireccion(g).replace(",", "</br>"));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    				g.setIo4("background-color: "+this.coloresAsignados.get(g.getImei())+"; height: 3px;");
    				
    				String pattern="####.##";
				 	g.setSpeed(new Double(g.getSpeed().toString().replace(',','.')));
					DecimalFormat df=new DecimalFormat(pattern);
					g.setSpeed(new Double(df.format(g.getSpeed())));
    				String lE="";
    				Double gas=null;
    				if(g.getIo3()!=null){
    				lE=ActividadVehiculo.porcentajeDouble(g.getIo3()).toString();
				 	gas=ActividadVehiculo.porcentajeDouble(g.getIo3());
				 	g.setIo3(df.format(gas)+", "+lE+"(Lt.)");
    				}
				 	
	
					this.vehiculosSe.add(g);
//					this.createPieModel1(g);
    				}
    			}

    			if(!noLeidos.isEmpty()){

//    				if(this.controlRuta){
//    					this.finRuta(noLeidos);
//    				}

    				if(noLeidos.size()>2){
    					interfazDAO.agregarMarcas(noLeidos, this.getMap(),g.getImei(),false,coloresAsignados.get(g.getImei()),false);
    					interfazDAO.agregarAlertas(noLeidos, getMap(), g.getImei(),false);
    					this.ultimoIdA.put(g.getImei(), interfazDAO.ultimaposicion(g.getImei()).getIdRegistro());
    				}
    				else{
    					if(g!=null){
    					Integer imeiT=this.ultimoIdA.get(g.getImei());
    					if(imeiT!=null)
    						if(imeiT.equals(noLeidos.get(0).getIdRegistro()))
    							interfazDAO.agregarMarcas(noLeidos, this.getMap(),g.getImei(),false,coloresAsignados.get(g.getImei()),false);
    						else{
    							interfazDAO.agregarMarcas(noLeidos, this.getMap(),g.getImei(),true,coloresAsignados.get(g.getImei()),false);
    							this.ultimoIdA.put(g.getImei(), interfazDAO.ultimaposicion(g.getImei()).getIdRegistro());
    						}
    					}
    				}
    				//					FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("gmap");
    			}
    		}//for gps
    	if(mostrarCerca){
    		this.pintarCerca();
    	}
    	this.mostrar.setFirst(pos);
    }
	
	
	

	
	public void setMap(Map map) {
	this.map=map;
    }
	
	public void vehiculosEnRuta(){
		this.vehiculosActivos=" ";
		List<ControlRuta> controles=interfazRutaDAO.obtenerRutasActivasControl();
		for(ControlRuta control:controles){
			Vehiculo v=interfazDAO.obtenerVehiculo(control.getVehImei());
			
			this.vehiculosActivos+=v.getVehMar()+" "+v.getVehMod()+", ";
			
			if(control.getVehImei().equals(this.vehiculoSeleccionado)){
				Ruta ruta=interfazRutaDAO.obtenerRutaPorId(control.getRutLla());
				List<GPSData> datosRuta=this.interfazRutaDAO.obtenerMarcasPorRuta(ruta.getRutLla());
				if(!datosRuta.isEmpty()){
					this.inicioRuta=datosRuta.get(0);	
				}
			}
		}
	}
	
	public String cambiarVehiculo(){
		try{
			setMap(this.iniciarMapa());
			for(String vs:vehiculoSeleccionado){
				interfazDAO.actualizarMapa(vs, getMap(),fechaA,coloresAsignados.get(vs));
			}
			FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("gmap");
			FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("mostrar");
		}catch(Exception e){
			e.printStackTrace();
		}
//		System.out.println("Dia " +fechaA.toGMTString());
//		List<GPSData> historialReciente=interfazDAO.historialReciente(InterfazDAO.resultMaxperConsult,vehiculoSeleccionado,fechaA);
//		interfazDAO.agregarCercas(cercas, getMap(),historialReciente);
		
		return "rastreo";
	}
	
	

	public String cambiarVehiculoEx(){
		try{
			this.init();
		}catch(Exception e){
			e.printStackTrace();
		}		
		return "rastreo";
	}
	

	
	
	public String activarComando(){
		InterfazLoginDAO login=new InterfazLoginDAO();
		Usuario usuarioA=new Usuario(this.usuarioA,this.contraA);
		Usuario eUsuario=this.getUsuario();//login.leerContraseña(usuarioA.getUsuNom());
		if(eUsuario!=null){
		if(usuarioA.getUsuCon().equals(eUsuario.getUsuCon())){
			interfazDAO.updateComandoActual(vehiculoComando, comandoSeleccionado);
			addMessage("Comando Ejecutado");
		}else{
	        addMessage("No tiene permisos");
		}
		}else{
			addMessage("No tiene permisos");
		}
		return "Activado";
	}
	
	
	public String clear(){
		this.getMap().getChildren().clear();
		List<GPSData> historialReciente=interfazDAO.historialReciente(Constantes.maxResults,vehiculoSeleccionadoB,fechaB);
//		if(!historialReciente.isEmpty())
//		interfazDAO.agregarCercas(cercas, this.map,historialReciente);
		return null;
	}
	
	public Map getMap(){
		return map;
	}
	


	
	public String pintarRuta() {
		
		List<GPSData> datosRuta=this.interfazRutaDAO.obtenerMarcasPorRuta(rutaSeleccionada);
		if(datosRuta!=null)
		if(!datosRuta.isEmpty()){
			setMap(this.iniciarMapa());
			this.inicioRuta=datosRuta.get(0);
			interfazRutaDAO.pintarRutaPol(rutaSeleccionada, getMap());
			this.finesRuta.add(datosRuta.get(datosRuta.size()-1));
		}
		return null;
	}
	

	
	public void controlTiempoRuta(){
		List<Ruta> rutasAIniciar=interfazRutaDAO.rutasXHora(this.getUsuario());
		if(rutasAIniciar!=null){
			for(Ruta ruta:rutasAIniciar){
				List<Vehiculo> vs=this.interfazRutaDAO.obtenerVehiculosXRuta(ruta.getRutLla());
				if(vs!=null)
				for(Vehiculo v:vs){
					if(v!=null){
					ControlRuta cr=this.interfazRutaDAO.obtenerControlRuta(v.getVehImei());
					if(cr==null){
						GPSData evento=	this.interfazDAO.ultimaposicion(v.getVehImei());
						evento.setEvento(Constantes.fueraDeTiempoRuta);
						interfazDAO.updateGPSData(evento);
						FacesContext context = FacesContext.getCurrentInstance();
				        context.addMessage(null, new FacesMessage("Debe Iniciar Ruta:"+ v.getVehMar()+ " "+v.getVehMod(),"Información"  ) );
					}
					}
				}
			}
		}
	}
	
	public void iniciarRuta(){
//		ControlRuta cr=this.interfazRutaDAO.obtenerControlRuta(vehiculoSeleccionado);
//		if(cr==null){
//		if(this.inicioRuta!=null){
//			GPSData rutaValorInicial=	this.interfazDAO.ultimaposicion(vehiculoSeleccionado);
//		
//		System.out.println(CalculosGeograficos.haversine(inicioRuta, rutaValorInicial));
//		if(CalculosGeograficos.haversine(inicioRuta, rutaValorInicial)<0.1){
//			ControlRuta controlRuta=new ControlRuta();
//			controlRuta.setActivo(true);
//			controlRuta.setRutLla(rutaSeleccionada);
//			controlRuta.setVehImei(vehiculoSeleccionado);
//			controlRuta.setIdRegistroI(rutaValorInicial.getIdRegistro());
//			controlRuta.setFechaIni(new Date());
//			interfazRutaDAO.guardaControlRuta(controlRuta);	
//			this.controlRuta=true;
//			}else{
//				FacesContext context = FacesContext.getCurrentInstance();
//		        context.addMessage(null, new FacesMessage("no se encuentra en el punto de inicio","Información"  ) );
//				System.out.println("no se encuentra en el punto de inicio");
//			}
//		}
//		}else{
//			FacesContext context = FacesContext.getCurrentInstance();
//	        context.addMessage(null, new FacesMessage("Vehiculo en Ruta","Información"  ) );
//		}
	}
	
	public void finRuta(List<GPSData> datos){
		List<ControlRuta> activos=interfazRutaDAO.obtenerRutasActivasControl();
		finesRuta=interfazRutaDAO.finesRuta();
		if(finesRuta!=null){
		if(!activos.isEmpty())
		for(GPSData dato:datos){
			GPSData finRuta=null;
			for(GPSData d:finesRuta){
				for(ControlRuta c:activos){
					if(d.getImei().equals(c.getVehImei())){
						finRuta=d;
						System.out.println("sad"+finesRuta.size());
						finesRuta.remove(finRuta);
						System.out.println("sad2"+finesRuta.size());
						break;
					}	
				}
			}
		
			if(finRuta!=null)
			if(CalculosGeograficos.haversine(finRuta, dato)<0.1){
				ControlRuta controlRuta=this.interfazRutaDAO.obtenerControlRuta(finRuta.getImei());
				controlRuta.setActivo(false);
				controlRuta.setFechaFin(new Date());
				controlRuta.setIdRegistroF(dato.getIdRegistro());
				interfazRutaDAO.guardaControlRuta(controlRuta);
				System.out.println("Ha llegado a su Destino");
				this.controlRuta=false;
				FacesContext context = FacesContext.getCurrentInstance();
		        context.addMessage(null, new FacesMessage("Ruta Finalizada","Información"  ) );
			}
		}
		}
	}
	public Usuario obtenerUsuario(){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Login login
	    = (Login)facesContext.getApplication()
	      .createValueBinding("#{login}").getValue(facesContext);
		return login.geteUsuario();		
	}
	
	
	
	
	public String turnosString(){
		String r="";
		for(Turno t:this.turnosActuales){
			r+=t.getTurDes();
		}
		return r;
	}
	
	public String visualizarCerca(){
		this.mostrarCerca=true;
		this.pintarCerca();
		return "";
	}
	
	public String desaparecerCerca(){
		this.mostrarCerca=false;
		return "";
	}
	
	public String pintarCerca(){
		List<GPSData> datosCerca=this.cercasDAO.obtenerMarcasPorCerca(cercaSeleccionada);
		if(datosCerca!=null)
		if(!datosCerca.isEmpty()){
			interfazCercaDAO.pintarCercaSimple(datosCerca, getMap());
		}
		FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("gmap:mapa");
		return null;
	}

	
	public Map iniciarMapa(){
		Map map=new Map();
		if(this.gpsDataF==null){
		map.setLatitude("19.055663");
		map.setLongitude("-98.154700");
		}else{
			map.setLatitude(this.gpsDataF.getLatitude().toString());
			map.setLongitude(this.gpsDataF.getLongitude().toString());
		}
		return map;
	}
	
	public void addMessage(String mensaje){
		FacesMessage mes=new FacesMessage(FacesMessage.SEVERITY_INFO,mensaje,null);
		FacesContext.getCurrentInstance().addMessage(null, mes);
	}
	
	private void createPieModel1(GPSData gps) {
        pieModel1 = new PieChartModel();
         Double t=new Double(gps.getIo3().substring(0, gps.getIo3().length()-1));
        pieModel1.set("Gas", t);
        pieModel1.set("Total", (100.00-t));
        pieModel1.setTitle("Uso");
        pieModel1.setLegendPosition("w");
    }

	public static List<GPSData> obtenerDirecciones(List<GPSData> datos){
		if(!datos.isEmpty())
		for(GPSData dato:datos){
			try{
			dato.setIo4(Interfaz.obtenerDireccion(dato));
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return datos;
	}
	
    public static String obtenerDireccion(GPSData gps) throws Exception {
        GeocoderRequest geocoderRequest;
        GeocodeResponse geocoderResponse;
        geocoder=new Geocoder();
        //http://code.google.com/intl/uk/apis/maps/documentation/geocoding/#ReverseGeocoding
        geocoderRequest = new GeocoderRequestBuilder().setLocation(new LatLng(gps.getLatitude().toString(), gps.getLongitude().toString())).setLanguage("en").getGeocoderRequest();
        geocoderResponse = geocoder.geocode(geocoderRequest);
//        assertNotNull(geocoderResponse);
//        assertEquals(geocoderResponse.getStatus(), GeocoderStatus.OK);
//        assertFalse(geocoderResponse.getResults().isEmpty());

        Iterator<GeocoderResult> iterator=geocoderResponse.getResults().iterator();
        GeocoderResult geocoderResult=null;
        if(iterator.hasNext()){
        	geocoderResult = iterator.next();	
        }else{
        	return "No disponible";
        }
        
        if(geocoderResult!=null){
        return geocoderResult.getFormattedAddress();
        }else{
        	return "No disponible.";
        }
//        assertTrue(geocoderResult.getTypes().contains(GeocoderResultType.STREET_ADDRESS.value()));
    }
	
}
