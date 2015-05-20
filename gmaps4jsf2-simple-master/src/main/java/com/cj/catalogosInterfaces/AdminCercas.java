package com.cj.catalogosInterfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.cj.Interfaz.Login;
import com.cj.ReportesDAO.ReportesCercasDAO;
import com.cj.catalogosDAO.AdminCercasDAO;
import com.cj.catalogosDAO.RutasDAO;
import com.cj.dao.InterfazAdminVehicDAO;
import com.cj.pojos.Cerca;
import com.cj.pojos.Usuario;
import com.cj.pojos.Vehiculo;
import com.cj.utils.Constantes;
import com.googlecode.gmaps4jsf.component.map.Map;

public class AdminCercas {
	private AdminCercasDAO cercasDAO=new AdminCercasDAO();
	private Cerca buscar=new Cerca();
	private List<Cerca> cercasDisponibles;
	private Cerca cercaEdit;
	private Boolean banderaSucces = true;
	private ReportesCercasDAO repo=new ReportesCercasDAO();
	private Map map;
	private Usuario usuario=this.getUsuario();
	private List<String> vehiculosSeleccion;

//	private Integer vehiculosSeleccion;
	private HashMap<String, Integer> vehiculosDisponibles;
//	
//	private Integer vehiculosSeleccion2;
//	private HashMap<String, Integer> vehiculosDisponibles2;
//	
//	private Integer vehiculosSeleccion3;
//	private HashMap<String, Integer> vehiculosDisponibles3;
//	
//	private Integer vehiculosSeleccion4;
//	private HashMap<String, Integer> vehiculosDisponibles4;
//	
//	private Integer vehiculosSeleccion5;
//	private HashMap<String, Integer> vehiculosDisponibles5;
	
	private Integer ruta;
	private HashMap<String, Integer> rutasDisponibles;
	
	private InterfazAdminVehicDAO adminVehi;
	private RutasDAO rutasDAO;
	
	public AdminCercas(){
		
		this.adminVehi=new InterfazAdminVehicDAO();
		Usuario u=this.getUsuario();
		this.usuario=u;
		if(u!=null){
			this.vehiculosDisponibles=adminVehi.obtenerVehiculosString(u);
//			this.vehiculosDisponibles2=adminVehi.obtenerVehiculosString(u);
//			this.vehiculosDisponibles3=adminVehi.obtenerVehiculosString(u);
//			this.vehiculosDisponibles4=adminVehi.obtenerVehiculosString(u);
//			this.vehiculosDisponibles5=adminVehi.obtenerVehiculosString(u);
			this.cercasDisponibles=cercasDAO.obtenerCercas(u);
			if(cercasDisponibles!=null){
				for(Cerca c:cercasDisponibles){
					List<Vehiculo> vs=new ArrayList<Vehiculo>();
					for(Vehiculo v:c.getVehiculos()){
						vs.add(v);
					}
					c.setVehiculos(vs);
				}
			}
		}
		
		rutasDAO=new RutasDAO();
		this.rutasDisponibles=rutasDAO.obtenerRutasString();
	}
	
	
	
	public Integer getRuta() {
		return ruta;
	}



	public void setRuta(Integer ruta) {
		this.ruta = ruta;
	}



	public HashMap<String, Integer> getRutasDisponibles() {
		return rutasDisponibles;
	}



	public void setRutasDisponibles5(HashMap<String, Integer> rutasDisponibles) {
		this.rutasDisponibles = rutasDisponibles;
	}









//	public Integer getVehiculosSeleccion2() {
//		return vehiculosSeleccion2;
//	}
//
//
//
//
//
//	public void setVehiculosSeleccion2(Integer vehiculosSeleccion2) {
//		this.vehiculosSeleccion2 = vehiculosSeleccion2;
//	}








	public HashMap<String, Integer> getVehiculosDisponibles2() {
		Usuario u=this.getUsuario();
		if(u!=null)
		return adminVehi.obtenerVehiculosString(u);
		else
			return null;
	}





//	public void setVehiculosDisponibles2(
//			HashMap<String, Integer> vehiculosDisponibles2) {
//		this.vehiculosDisponibles2 = vehiculosDisponibles2;
//	}
//
//
//
//
//
//	public Integer getVehiculosSeleccion3() {
//		return vehiculosSeleccion3;
//	}
//
//
//
//
//
//	public void setVehiculosSeleccion3(Integer vehiculosSeleccion3) {
//		this.vehiculosSeleccion3 = vehiculosSeleccion3;
//	}
//




	public List<String> getVehiculosSeleccion() {
		return vehiculosSeleccion;
	}



	public void setVehiculosSeleccion(List<String> vehiculosSeleccion) {
		this.vehiculosSeleccion = vehiculosSeleccion;
	}



	public HashMap<String, Integer> getVehiculosDisponibles3() {
		Usuario u=this.getUsuario();
		if(u!=null)
		return adminVehi.obtenerVehiculosString(u);
		else
			return null;
	}





//	public void setVehiculosDisponibles3(
//			HashMap<String, Integer> vehiculosDisponibles3) {
//		this.vehiculosDisponibles3 = vehiculosDisponibles3;
//	}
//
//
//
//
//
//	public Integer getVehiculosSeleccion4() {
//		return vehiculosSeleccion4;
//	}




//
//	public void setVehiculosSeleccion4(Integer vehiculosSeleccion4) {
//		this.vehiculosSeleccion4 = vehiculosSeleccion4;
//	}





	public HashMap<String, Integer> getVehiculosDisponibles4() {
		Usuario u=this.getUsuario();
		if(u!=null)
		return adminVehi.obtenerVehiculosString(u);
		else
			return null;
	}





//	public void setVehiculosDisponibles4(
//			HashMap<String, Integer> vehiculosDisponibles4) {
//		this.vehiculosDisponibles4 = vehiculosDisponibles4;
//	}
//
//
//
//
//
//	public Integer getVehiculosSeleccion5() {
//		return vehiculosSeleccion5;
//	}





//	public void setVehiculosSeleccion5(Integer vehiculosSeleccion5) {
//		this.vehiculosSeleccion5 = vehiculosSeleccion5;
//	}





	public HashMap<String, Integer> getVehiculosDisponibles5() {
		Usuario u=this.getUsuario();
		if(u!=null)
		return adminVehi.obtenerVehiculosString(u);
		else
			return null;
	}





//	public void setVehiculosDisponibles5(
//			HashMap<String, Integer> vehiculosDisponibles5) {
//		this.vehiculosDisponibles5 = vehiculosDisponibles5;
//	}





	public HashMap<String, Integer> getVehiculosDisponibles() {
		Usuario u=this.getUsuario();
		if(u!=null)
		return adminVehi.obtenerVehiculosString(getUsuario());
		else
			return null;
	}


	public void setVehiculosDisponibles(
			HashMap<String, Integer> vehiculosDisponibles) {
		this.vehiculosDisponibles = vehiculosDisponibles;
	}


	public Usuario getUsuario() {
		return this.obtenerUsuario();
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public Map getMap() {
		return map;
	}
	public void setMap(Map map) {
		this.map = map;
	}
	public Cerca getBuscar() {
		return buscar;
	}
	public void setBuscar(Cerca buscar) {
		this.buscar = buscar;
	}
	public List<Cerca> getCercasDisponibles() {
		return cercasDisponibles;
	}
	public void setCercasDisponibles(List<Cerca> cercasDisponibles) {
		this.cercasDisponibles = cercasDisponibles;
	}
	public Cerca getCercaEdit() {
		return cercaEdit;
	}
	public void setCercaEdit(Cerca cercaEdit) {
		this.cercaEdit = cercaEdit;
	}
	public Boolean getBanderaSucces() {
		return banderaSucces;
	}
	public void setBanderaSucces(Boolean banderaSucces) {
		this.banderaSucces = banderaSucces;
	}

public String generaReporte(){
	if(this.cercasDisponibles!=null){
		if(!this.cercasDisponibles.isEmpty()){
		repo.crearReportePlazas(this.cercasDisponibles);
		}else{
			FacesContext context = FacesContext.getCurrentInstance();
	        context.addMessage(null, new FacesMessage(Constantes.noParametrosBusqueda,"Información"  ) );
		}
		}else{
			FacesContext context = FacesContext.getCurrentInstance();
	        context.addMessage(null, new FacesMessage(Constantes.noResultadosDeBusqueda,"Información"  ) );
		}
		return null;

}
	
public String buscarC(){
//	if((buscar.getGeoLla().equals("") || buscar.getGeoLla()==null)&&
//			(buscar.getGeoFAl().equals("") || buscar.getGeoFAl()==null)&&
//			(buscar.getGeoFBa().equals("") || buscar.getGeoFBa()==null)&&
//			(buscar.getGeoDes().equals("") || buscar.getGeoDes()==null)&&
//			(buscar.getGeoNom().equals("") || buscar.getGeoNom()==null)&&
//			(buscar.getLatitude().equals("") || buscar.getLatitude()==null)&&
//			(buscar.getLongitude().equals("") || buscar.getLongitude()==null)&&
//			(buscar.getRadio().equals("") || buscar.getRadio()==null)){
	if((buscar.getGeoDes().equals("") || buscar.getGeoDes()==null)&&
			(buscar.getGeoNom().equals("") || buscar.getGeoNom()==null)){
		this.cercasDisponibles=cercasDAO.obtenerCercas(this.getUsuario());
		
		FacesContext context = FacesContext.getCurrentInstance();
		context.getPartialViewContext().getRenderIds().add("cercas");
//        context.addMessage(null, new FacesMessage(Constantes.noParametrosBusqueda,"Información"  ) );	
	}else{
		this.cercasDisponibles=cercasDAO.busqueda(cercasDAO.transToquens(buscar),this.usuario);
		FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(Constantes.exito,"Información"  ) );
        context.getPartialViewContext().getRenderIds().add("perfiles");
	}
return "";
}
public String deleteCerca(Cerca cerca){
	try {

		this.cercasDAO.deleteCerca(cerca);
//		plazasDisponibles = plazasDAO.obtenerPlazas();
		FacesContext context = FacesContext.getCurrentInstance();
		
		context.addMessage(null, new FacesMessage(
				"Operación Realizada con Exito", "Información"));
		this.cercasDisponibles=cercasDAO.obtenerCercas(this.getUsuario());
		context.getPartialViewContext().getRenderIds().add("cercas");

	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		setBanderaSucces(false);
	}
	return null;
}
public String guardaCercaEditado(){
	try {
		this.getCercaEdit().setEstadoRegistro(true);
		this.getCercaEdit().setUsuario(getUsuario());
		
		List<Vehiculo> v1=this.adminVehi.obtenerVehiculosListString(vehiculosSeleccion);
//		Vehiculo v2=this.adminVehi.obtenerVehiculoXId(vehiculosSeleccion2);
//		Vehiculo v3=this.adminVehi.obtenerVehiculoXId(vehiculosSeleccion3);
//		Vehiculo v4=this.adminVehi.obtenerVehiculoXId(vehiculosSeleccion4);
//		Vehiculo v5=this.adminVehi.obtenerVehiculoXId(vehiculosSeleccion5);
		this.getCercaEdit().setVehiculos(v1);
//		this.getCercaEdit().setVehiculo2(v2);
//		this.getCercaEdit().setVehiculo3(v3);
//		this.getCercaEdit().setVehiculo4(v4);
//		this.getCercaEdit().setVehiculo5(v5);
		this.getCercaEdit().setGeoRut(this.rutasDAO.obtenerRutaById(ruta));
		this.cercasDAO.updateCerca(this.getCercaEdit());
		this.cercasDAO.guardarMarcas(getMap());
		setBanderaSucces(true);
		FacesContext context = FacesContext.getCurrentInstance();

		context.addMessage(null, new FacesMessage(
				"Operación Realizada con Exito", "Información"));
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		setBanderaSucces(false);
	}
	// TODO: cambiar por acceso a la base de datos
	return "muestraCercas";
}
public String regresarMuestraCercas(){
	this.setCercaEdit(null);
	this.cercasDisponibles=cercasDAO.obtenerCercas(this.getUsuario());
//	this.plazasDisponibles = plazasDAO.obtenerPlazas();
	// TODO: cambiar por acceso a la base de datos
	return "muestraCercas";
}
public String editCerca(Cerca cerca){
	this.vehiculosSeleccion=new ArrayList<String>();
		
	
	setCercaEdit(cerca);
	if(cerca.getVehiculos()!=null){
		if(!cerca.getVehiculos().isEmpty()){
			for(Vehiculo v:cerca.getVehiculos()){
				if(this.vehiculosSeleccion!=null)
				this.vehiculosSeleccion.add(v.getVehLla()+"");
			}
		}
	}
//	if(cerca.getVehiculos()!=null)
//	this.vehiculosSeleccion=cerca.getVehiculos().getVehLla();
//	if(cerca.getVehiculo2()!=null)
//	this.vehiculosSeleccion2=cerca.getVehiculo2().getVehLla();
//	if(cerca.getVehiculo3()!=null)
//	this.vehiculosSeleccion3=cerca.getVehiculo3().getVehLla();
//	if(cerca.getVehiculo4()!=null)
//	this.vehiculosSeleccion4=cerca.getVehiculo4().getVehLla();
//	if(cerca.getVehiculo5()!=null)
//	this.vehiculosSeleccion5=cerca.getVehiculo5().getVehLla();
	
	if(cerca.getGeoRut()!=null){
		this.ruta=cerca.getGeoRut().getRutLla();
	}
	getMap().getChildren().clear();
	cercasDAO.agregarMarcasEditable(getCercaEdit().getGeoLla(), getMap());
	FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("edit");
	return null;
}
public String agregarCerca(){
	this.cercaEdit = new Cerca();
	return "cerca";
}

public Usuario obtenerUsuario(){
	FacesContext facesContext = FacesContext.getCurrentInstance();
	Login login
    = (Login)facesContext.getApplication()
      .createValueBinding("#{login}").getValue(facesContext);
	return login.geteUsuario();		
}

}
