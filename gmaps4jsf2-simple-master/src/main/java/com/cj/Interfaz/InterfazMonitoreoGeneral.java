package com.cj.Interfaz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.faces.context.FacesContext;

import com.cj.dao.InterfazDAO;
import com.cj.dao.InterfazMonitoreoGeneralDAO;
import com.cj.pojos.GPSData;
import com.cj.pojos.Usuario;
import com.cj.pojos.Vehiculo;
import com.cj.utils.Constantes;

public class InterfazMonitoreoGeneral {
	private InterfazDAO interfazDAO=new InterfazDAO();
	private InterfazMonitoreoGeneralDAO interfazMGDAO=new InterfazMonitoreoGeneralDAO();
	
	private Usuario usuario=this.getUsuario();
	private List<GPSData> datos=new ArrayList<GPSData>();
	private List<GPSData> datosfiltrados;
	
//	private String columnTemplate = "ID ESTATUS FECHA IMEI LATITUD LONGITUD VELOCIDAD";
//	private List<ColumnModel> columns=this.createDynamicColumns();
	
public void init(){
	datos=this.detecciones();
}
public List<GPSData>detecciones(){
	List<Vehiculo> vs=new ArrayList<Vehiculo>();
	Usuario u=this.getUsuario();
	if(u!=null){
		vs=interfazDAO.obtenerVehiculos(u);	
	}
	
	List<GPSData> pros=new ArrayList<GPSData>();
	int size=0;
	for(Vehiculo v:vs){
		List<GPSData> hist=interfazMGDAO.historialHoy(v.getVehImei(),new Date(),v.getVehVeM());
		pros.addAll(interfazMGDAO.deteccionDeTiempo(hist));
		pros.addAll(interfazMGDAO.deteccionDeNormal(hist,v.getVehVeM()));
		pros.addAll(interfazMGDAO.deteccionDeVelocidadMax(hist));
//		pros.addAll(interfazMGDAO.deteccionDeSOS(hist));
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
		for(int i=size; i<pros.size(); i++){
			pros.get(i).setImei(v.getVehDes()+" "+v.getVehMar());
			try {
				pros.get(i).setIo4(Interfaz.obtenerDireccion(pros.get(i)));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		size=pros.size();
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
	Comparator cmp=Collections.reverseOrder();
	Collections.sort(prost,cmp);
//	Arrays.sort(prost,Collections.reverseOrder());
	return prost;
}
	public Usuario getUsuario() {
	return this.obtenerUsuario();
}
public void setUsuario(Usuario usuario) {
	this.usuario = usuario;
}
	public List<GPSData> getDatos() {
		return datos;
	}



	public void setDatos(List<GPSData> datos) {
		this.datos = datos;
	}



	public List<GPSData> getDatosfiltrados() {
		return datosfiltrados;
	}



	public void setDatosfiltrados(List<GPSData> datosfiltrados) {
		this.datosfiltrados = datosfiltrados;
	}



	public void refresh(){
		List<GPSData> historial=this.detecciones();//interfazMGDAO.historialHoy(new Date());
		if(datos.isEmpty()){
//			datos=interfazMGDAO.deteccionDeTiempo(historial);
			datos=historial;
		}
		if(!historial.isEmpty() && !datos.isEmpty()){
//		if(historial.get(historial.size()-1).getIdRegistro()>this.datos.get(datos.size()-1).getIdRegistro()){
//			datos=interfazMGDAO.deteccionDeTiempo(historial);
			datos=historial;
			FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("form:eventos");
		}
	}
	
//	public String getColumnTemplate() {
//		return columnTemplate;
//	}



//	public void setColumnTemplate(String columnTemplate) {
//		this.columnTemplate = columnTemplate;
//	}



//	public List<ColumnModel> getColumns() {
//		return columns;
//	}
//
//
//
//	public void setColumns(List<ColumnModel> columns) {
//		this.columns = columns;
//	}
//
//
//
//	private List<ColumnModel> createDynamicColumns() {
//        String[] columnKeys = columnTemplate.split(" ");
//        List<ColumnModel> columns = new ArrayList<ColumnModel>();   
//         
//        for(String columnKey : columnKeys) {
//            String key = columnKey.trim();
//             
//                columns.add(new ColumnModel(columnKey.toUpperCase(), columnKey));
//            
//        }
//        return columns;
//    }
	
	
	public Usuario obtenerUsuario(){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Login login
	    = (Login)facesContext.getApplication()
	      .createValueBinding("#{login}").getValue(facesContext);
		return login.geteUsuario();		
	}	
}
