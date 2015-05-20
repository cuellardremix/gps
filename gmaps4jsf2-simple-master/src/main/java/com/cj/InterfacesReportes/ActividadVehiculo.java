package com.cj.InterfacesReportes;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import com.cj.Interfaz.Login;
import com.cj.ReportesDAO.ReportesVehiculoXActividadDAO;
import com.cj.dao.InterfazDAO;
import com.cj.dao.InterfazMonitoreoGeneralDAO;
import com.cj.pojos.GPSData;
import com.cj.pojos.Usuario;
import com.cj.pojos.Vehiculo;
import com.cj.utils.Constantes;
import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.GeocoderResultType;
import com.google.code.geocoder.model.GeocoderStatus;
import com.google.code.geocoder.model.LatLng;
import com.googlecode.gmaps4jsf.component.map.Map;
import com.googlecode.gmaps4jsf.services.GMaps4JSFServiceFactory;
import com.googlecode.gmaps4jsf.services.data.PlaceMark;

public class ActividadVehiculo {
	private List<GPSData> datos;
	private HashMap<String,String> vehiculos;
	private String vehiculoImei;
	private Date fechaIni;
	private Date fechaFin;
	private Boolean banderaSucces=true;
	private ReportesVehiculoXActividadDAO reportVADAO=new ReportesVehiculoXActividadDAO(); 
	private InterfazDAO interfazDAO=new InterfazDAO();
	private InterfazMonitoreoGeneralDAO interfazMGDAO=new InterfazMonitoreoGeneralDAO();
	
	private Usuario usuario=this.obtenerUsuario();
	
	private BarChartModel hModel1;
	
	//checbox para seleccion de reporte
	private String[] reporteElec=getReporteElecValue();
	private String reporte;
	private Double gasolina;
	private Double kilometraje;
	
	private Map map;
	 private static Geocoder geocoder;
	
	@PostConstruct
	public void init(){
		this.map=this.iniciarMapa();
		this.hModel1=new BarChartModel();
		geocoder = new Geocoder();
	}

	public BarChartModel gethModel1() {
		return hModel1;
	}






	public void sethModel1(BarChartModel hModel1) {
		this.hModel1 = hModel1;
	}






	public String getReporte() {
		return reporte;
	}
	public void setReporte(String reporte) {
		this.reporte = reporte;
	}
	public String[] getReporteElecValue(){
		reporteElec=new String[9];
		reporteElec[0] = "Por Actividad";
		reporteElec[1] = "Por sitios Visitados";
		reporteElec[2] = "Exceso de Velocidad";
		reporteElec[3] = "Paradas realizadas";
		reporteElec[4] = "Vel promedio";
		reporteElec[5] = "Geocercas";
		reporteElec[6] = "Inactividad";
		reporteElec[7] = "Ubicacion Actual";
		reporteElec[8] = "Kilometraje recorrido";
		return reporteElec;
	}
	public String getReporteElecValueInString(){
		return Arrays.toString(reporteElec);
	}
	
	
	public Double getKilometraje() {
		return kilometraje;
	}

	public void setKilometraje(Double kilometraje) {
		this.kilometraje = kilometraje;
	}

	public Double getGasolina() {
		return gasolina;
	}

	public void setGasolina(Double gasolina) {
		this.gasolina = gasolina;
	}

	public Map getMap() {
		return map;
	}
	public void setMap(Map map) {
		this.map = map;
	}
	public String[] getReporteElec() {
		return reporteElec;
	}
	public void setReporteElec(String[] reporteElec) {
		this.reporteElec = reporteElec;
	}
	public List<GPSData> getDatos() {
		return datos;
	}
	public String getVehiculoImei() {
		return vehiculoImei;
	}
	public void setVehiculoImei(String vehiculoImei) {
		this.vehiculoImei = vehiculoImei;
	}
	public Boolean getBanderaSucces() {
		return banderaSucces;
	}
	public void setBanderaSucces(Boolean banderaSucces) {
		this.banderaSucces = banderaSucces;
	}
	public void setDatos(List<GPSData> datos) {
		this.datos = datos;
	}
	public HashMap<String, String> getVehiculos() {
		Usuario u=this.getUsuario();
		if(u!=null)
		this.vehiculos=this.reportVADAO.obtenerVehiculosString(interfazDAO.obtenerVehiculos(u));
		return vehiculos;
	}
	public void setVehiculos(HashMap<String, String> vehiculos) {
		this.vehiculos = vehiculos;
	}
	public Date getFechaIni() {
		return fechaIni;
	}
	public void setFechaIni(Date fechaIni) {
		this.fechaIni = fechaIni;
	}
	public Date getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	
	/*
	 * reporteElec[0] = "Por Actividad";
		reporteElec[1] = "Por sitios Visitados";
		reporteElec[2] = "Exceso de Velocidad";
		reporteElec[3] = "Paradas realizadas";
		reporteElec[4] = "Vel promedio";
		reporteElec[5] = "Geocercas";
		reporteElec[6] = "Inactividad";
		reporteElec[7] = "Ubicacion Actual";
	 */
	
	public Usuario getUsuario() {
		return this.obtenerUsuario();
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	
	
	public List<GPSData>detecciones(List<GPSData> datos){
		List<Vehiculo> vs=interfazDAO.obtenerVehiculos(this.getUsuario());
		List<GPSData> pros=new ArrayList<GPSData>();
		for(Vehiculo v:vs){
			List<GPSData> hist=datos;
			pros.addAll(interfazMGDAO.deteccionDeTiempo(hist));
			pros.addAll(interfazMGDAO.deteccionDeNormal(hist,v.getVehVeM()));
			pros.addAll(interfazMGDAO.deteccionDeVelocidadMax(hist));
//			pros.addAll(interfazMGDAO.deteccionDeSOS(hist));
			if(hist!=null)
				if(!hist.isEmpty()){
					GPSData ultimaSOS=interfazDAO.ultimaSOS(hist.get(0).getImei());
					if(ultimaSOS!=null){
					ultimaSOS.setAlarm(Constantes.sos);
					ultimaSOS.setIo1("Ayuda");
					pros.add(ultimaSOS);
					}
				}
			
			pros.addAll(interfazMGDAO.deteccionEncendido(hist));
		}
		Set<GPSData> s=new LinkedHashSet<GPSData>(pros);
		pros=new ArrayList<GPSData>(s);
		List<GPSData> prost=new ArrayList<GPSData>();
		if(pros!=null)
		for(GPSData g:pros){
			if(g!=null){
				prost.add(g);
			}
		}
		Collections.sort(prost);
//		Arrays.sort(pros,Collections.reverseOrder());
		return prost;
	}
	
	public void generarReporte(){
	
		if(this.reporte.equals("Por Actividad")){
			this.setDatos(reportVADAO.buscarActividad(getFechaIni(), fechaFin, vehiculoImei));
			this.reportVADAO.crearReporte(getDatos(),"ActXVehiculo.jasper",new HashMap());
			
		}else if(this.reporte.equals("Por sitios Visitados")){
			this.reportVADAO.crearReporte(getDatos(),"SitiosVisitados.jasper",new HashMap());
		}else if(this.reporte.equals("Exceso de Velocidad")){
			this.reportVADAO.crearReporte(getDatos(),"SitiosVisitados.jasper",new HashMap());
		}else if(this.reporte.equals("Paradas realizadas")){
			this.reportVADAO.crearReporte(getDatos(),"SitiosVisitados.jasper",new HashMap());
		}else if(this.reporte.equals("Vel promedio")){
			HashMap<String,Object> parameters=new HashMap<String,Object>();
			if(getDatos()!=null)
				if(!getDatos().isEmpty()){
			parameters.put("promedio", this.reportVADAO.calculaPromedioVelocidad(getDatos()));
			parameters.put("fechaIni",fechaIni);
			parameters.put("fechaFin", fechaFin);
			List<GPSData> g=new ArrayList<GPSData>();
			g.add(getDatos().get(0));
			this.reportVADAO.crearReporte(g,"promedioVelocidad.jasper",parameters);
				}
		}else if(this.reporte.equals("Geocercas")){
			this.reportVADAO.crearReporte(getDatos(),"SitiosVisitados.jasper",new HashMap());
		}else if(this.reporte.equals("Inactividad")){
			this.reportVADAO.crearReporte(getDatos(),"SitiosVisitados.jasper",new HashMap());
		}else if(this.reporte.equals("Ubicacion Actual")){
			this.reportVADAO.crearReporte(getDatos(),"SitiosVisitados.jasper",new HashMap());
		}else if(this.reporte.equals("Kilometraje recorrido")){
			
			HashMap<String,Object> parameters=new HashMap<String,Object>();
			parameters.put("promedio", this.getDatos().get(getDatos().size()-1).getSpeed());
			parameters.put("fechaIni",fechaIni);
			parameters.put("fechaFin", fechaFin);
			this.reportVADAO.crearReporte(getDatos(),"kilometraje.jasper",parameters);
		}
		
		
	}
	
	public static Double porcentajeDouble(String porcentaje){
		if(porcentaje!=null){
			if(!porcentaje.equals("")){
				String r=porcentaje.substring(0,porcentaje.length()-1);
				return new Double(r);		
			}
		}
		return null;
	}
	
	public List<GPSData> obtenerExtremosOff(List<GPSData> datos){
		List<GPSData> extremos=new ArrayList<GPSData>();
		for(int i=0; i<datos.size(); i++){
			if(datos.get(i).getAlarm().equals(Constantes.apagado)){
				extremos.add(datos.get(i));
				break;
			}
		}
		for(int i=datos.size()-1; i==0; i--){
			if(datos.get(i).getAlarm().equals(Constantes.apagado)){
				extremos.add(datos.get(i));
				break;
			}
		}
		return extremos;
	}
	

	public List<GPSData> formatearDecimales(List<GPSData> datos){
		String pattern="####.##";
		DecimalFormat df=new DecimalFormat(pattern);
		for(GPSData dato:datos){
			dato.setSpeed(new Double(dato.getSpeed().toString().replace(',','.')));
        	dato.setSpeed(new Double(df.format(dato.getSpeed())));
        	Double gas=ActividadVehiculo.porcentajeDouble(dato.getIo3());
        	dato.setIo3(df.format(gas));
		}
		return datos;
	}
	
	public String buscarMarcas(){
		this.gasolina=null;
		this.kilometraje=null;
			if(!this.getMap().getChildren().isEmpty())
			this.getMap().getChildren().clear();
			List<GPSData> datos=reportVADAO.buscarSitiosVisitados(fechaIni, fechaFin, vehiculoImei);
			Vehiculo v=interfazDAO.obtenerVehiculo(vehiculoImei);
			this.createLineModels(datos, v);
			this.interfazDAO.agregarMarcas(datos, getMap(), vehiculoImei, true, Constantes.color3,true);
			this.setDatos(this.obtenerDirecciones(reportVADAO.eliminarIntranscendentes(datos,v.getVehVeM()),v));
			FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("mapa");
			if(!datos.isEmpty()){
				datos=this.formatearDecimales(datos);
				List<GPSData> extremos=this.obtenerExtremosOff(datos);
				if(!extremos.isEmpty()){
					Double pR=ActividadVehiculo.porcentajeDouble(extremos.get(1).getIo3());
					Double pF=ActividadVehiculo.porcentajeDouble(extremos.get(0).getIo3());
					if(pR==null || pF==null){
						gasolina=null;
					}else{
						gasolina=pR-pF;	
					}
					if(pR.equals(pF)){
						gasolina=null;
					}
				}
				String pattern="####.##";
				DecimalFormat df=new DecimalFormat(pattern);
				Double k=reportVADAO.kilometraje(datos);
				if(k!=null)
				this.kilometraje=new Double(df.format(k));
			}
			

//			FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("gas");
			FacesContext context = FacesContext.getCurrentInstance();
	        context.addMessage(null, new FacesMessage(Constantes.exito,"Información"  ) );
	        context.getPartialViewContext().getRenderIds().add("datosGPS");
	        return null;
	}
	
	private void createLineModels(List<GPSData> datos,Vehiculo v) {
		if(!datos.isEmpty()){
        hModel1 = initCategoryModel(datos,v);
        hModel1.setTitle("Consumo de Gasolina");
        hModel1.setLegendPosition("ne");
        Axis yAxis = hModel1.getAxis(AxisType.Y);
        yAxis.setMin(0.0);
        yAxis.setMax(new Double(v.getVehTanGas()));
        yAxis.setTickAngle(-90);

        
        Axis xAxis = hModel1.getAxis(AxisType.X);
        xAxis.setTickAngle(-90);
        
        hModel1.setSeriesColors("0000FF,01DF01,F74A4A,F52F2F,A30303");
        hModel1.setShowPointLabels(true);
		}else{
			hModel1 = new BarChartModel();
		}
//        xAxis.setMin(0);
//        xAxis.setMax(24.0);
    }
	
	private BarChartModel initCategoryModel(List<GPSData> datos,Vehiculo v) {
		BarChartModel model = new BarChartModel();
        ChartSeries gas = new  ChartSeries();
        gas.setLabel("Gasolina");
        
        ChartSeries gasEx = new  ChartSeries();
        gasEx.setLabel("Aumento");
        ChartSeries gasDs = new  ChartSeries();
        gasDs.setLabel("Disminucion");
        
        Collections.sort(datos);
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss"); 
//        datos=datos.subList(0, 50);
        
        
        List<GPSData> datosR=new ArrayList<GPSData>();
        GPSData datoP=datos.get(0);
        datosR.add(datoP);
        for(int i=1; i<datos.size(); i++){
        	
        	GPSData dato2=datos.get(i);
        	
        	 Calendar fecha1=Calendar.getInstance();
             fecha1.setTime(datoP.getFecha());
             Calendar fecha2=Calendar.getInstance();
             fecha2.setTime(dato2.getFecha());
             long diferencia=InterfazMonitoreoGeneralDAO.cantidadTotalSegundos(fecha1, fecha2)/60;
//             System.out.println(fecha1.get(Calendar.MINUTE)+" as "+diferencia+" "+fecha2.get(Calendar.MINUTE));
             if(diferencia>=5){
            	 datosR.add(datos.get(i));
            	 datoP=datos.get(i);
             }
        }
        
        List<GPSData> datosT=new ArrayList<GPSData>();
        for(int i=0; i<datosR.size(); i++){
        	if(datosR.get(i).getIo3()!=null){
        	Double gas1=new Double(datosR.get(i).getIo3().substring(0,datosR.get(i).getIo3().length()-1));
        	if(gas1!=0.0){
        		datosT.add(datosR.get(i));
        	}
        	}
        }
        datosR=datosT;
        
        for(int i=1; i<datosR.size(); i++){
        	GPSData dato1=datosR.get(i-1);
        	GPSData dato2=datosR.get(i);

        	//datos de gasolina(subidas, bajadas)
        	Double gas1=new Double(dato1.getIo3().substring(0,dato1.getIo3().length()-1));
        	gas1=(v.getVehTanGas()*gas1)/100;
        	Double gas2=new Double(dato2.getIo3().substring(0,dato2.getIo3().length()-1));
        	gas2=(v.getVehTanGas()*gas2)/100;
        	String fe=dateFormat.format(dato1.getFecha());
        	
//        	gasEx.set(fe, (gas2-gas1));
        	
        	
        	if(gas2>gas1){
        		double dif=(gas2-gas1);
        		gasEx.set(fe, dif);
        		gasDs.set(fe, 0);
        		gas.set(fe, gas2-dif);
    			dato2.setEvento(Constantes.llenadoDeGasolina);
    			dato2.setIo1(dif+"");
        	}
        	else if(gas2<gas1){
        		double dif=(gas1-gas2);
        		if(dif>=2.0){
        			gasDs.set(fe, dif);
        			dato2.setEvento(Constantes.bajaDeGasolina);
        			dato2.setIo1(dif+"");
        		}else{
        			gasDs.set(fe, 0);
        		}
        		gasEx.set(fe, 0);
        		gas.set(fe, (gas1-dif));
        	}else {
        		gasEx.set(fe, 0);
        		gasDs.set(fe, 0);
        		gas.set(fe,gas1);
        	}
        	java.util.Map<Object, Number> m=gas.getData();
//        	System.out.println("obte: "+m.get(fe));
        }
 
        
        
        model.addSeries(gas);
        model.addSeries(gasEx);
        model.addSeries(gasDs);
         model.setStacked(true);
        return model;
    }
	
	public List<GPSData> obtenerDirecciones(List<GPSData> datos,Vehiculo v){
		if(!datos.isEmpty())
		for(GPSData dato:datos){
			try{
			dato.setIo4(this.obtenerDireccion(dato));
			if(dato.getIo3()!=null){
			Double gas1=new Double(dato.getIo3().substring(0,dato.getIo3().length()-1));
			gas1=(v.getVehTanGas()*gas1)/100;
			dato.setIo3(gas1.toString());
			}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return datos;
	}
	
    public String obtenerDireccion(GPSData gps) throws Exception {
        GeocoderRequest geocoderRequest;
        GeocodeResponse geocoderResponse;

        //http://code.google.com/intl/uk/apis/maps/documentation/geocoding/#ReverseGeocoding
        geocoderRequest = new GeocoderRequestBuilder().setLocation(new LatLng(gps.getLatitude().toString(), gps.getLongitude().toString())).setLanguage("en").getGeocoderRequest();
        geocoderResponse = geocoder.geocode(geocoderRequest);
//        assertNotNull(geocoderResponse);
//        assertEquals(geocoderResponse.getStatus(), GeocoderStatus.OK);
//        assertFalse(geocoderResponse.getResults().isEmpty());

        final GeocoderResult geocoderResult = geocoderResponse.getResults().iterator().next();
        if(geocoderResult!=null){
        return geocoderResult.getFormattedAddress();
        }else{
        	return "No disponible.";
        }
//        assertTrue(geocoderResult.getTypes().contains(GeocoderResultType.STREET_ADDRESS.value()));
    }
	
	public Map iniciarMapa(){
		Map map=new Map();
		
		map.setLatitude("19.055663");
		map.setLongitude("-98.154700");
	
		return map;
	}
	
	public Usuario obtenerUsuario(){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Login login
	    = (Login)facesContext.getApplication()
	      .createValueBinding("#{login}").getValue(facesContext);
		return login.geteUsuario();		
	}	
}
