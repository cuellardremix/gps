package com.cj.catalogosInterfaces;

import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.cj.Interfaz.Login;
import com.cj.ReportesDAO.ReportesRutasDAO;
import com.cj.catalogosDAO.AdminCercasDAO;
import com.cj.catalogosDAO.AdminPlazasDAO;
import com.cj.catalogosDAO.AdminTurnosDAO;
import com.cj.catalogosDAO.RutasDAO;
import com.cj.dao.InterfazAdminVehicDAO;
import com.cj.dao.InterfazDAO;
import com.cj.pojos.Ruta;
import com.cj.pojos.Usuario;
import com.cj.pojos.Vehiculo;
import com.cj.utils.Constantes;
import com.googlecode.gmaps4jsf.component.map.Map;

public class AdminRutas {
	private InterfazDAO interfazDAO=new InterfazDAO();
	private RutasDAO rutasDAO=new RutasDAO();
	private List<Ruta> rutasDisponibles;//=rutasDAO.obtenerRutas();
	private Ruta buscar=new Ruta();
	private ReportesRutasDAO repo=new ReportesRutasDAO();
	private Ruta rutaEdit;
	private Ruta deleteRuta;
	private Boolean banderaSucces=true;
	private Map map;
	private Usuario usuario;

	private InterfazAdminVehicDAO adminVehi;
	
	private AdminCercasDAO cercasDAO;
	private AdminTurnosDAO turnosDAO;
	private AdminPlazasDAO plazasDAO;
	
	//geocercas
	private HashMap<String, Integer> geocercasDisponibles;
	private Integer geocercaSeleccionada;
	
	//turnos
	private List<String> turnosSeleccion;
	private HashMap<String, String> turnosDisponibles;
	
	//plaza
	private HashMap<String, Integer> plazasDisponibles;
	private Integer plazaSeleccionada;
	
	private Integer vehiculosSeleccion;
	private HashMap<String, Integer> vehiculosDisponibles;
	
	private Integer vehiculosSeleccion2;
	private HashMap<String, Integer> vehiculosDisponibles2;
	
	private Integer vehiculosSeleccion3;
	private HashMap<String, Integer> vehiculosDisponibles3;
	
	private Integer vehiculosSeleccion4;
	private HashMap<String, Integer> vehiculosDisponibles4;
	
	private Integer vehiculosSeleccion5;
	private HashMap<String, Integer> vehiculosDisponibles5;

	
	public AdminRutas(){
		this.adminVehi=new InterfazAdminVehicDAO();
		
		this.vehiculosDisponibles=adminVehi.obtenerVehiculosString(getUsuario());
		this.vehiculosDisponibles2=adminVehi.obtenerVehiculosString(getUsuario());
		this.vehiculosDisponibles3=adminVehi.obtenerVehiculosString(getUsuario());
		this.vehiculosDisponibles4=adminVehi.obtenerVehiculosString(getUsuario());
		this.vehiculosDisponibles5=adminVehi.obtenerVehiculosString(getUsuario());
		
		this.cercasDAO=new AdminCercasDAO();
//		this.geocercasDisponibles=cercasDAO.obtenerCercasString(getUsuario());
		this.turnosDAO=new AdminTurnosDAO();
		this.turnosDisponibles=turnosDAO.obtenerTurnosSS();
		this.plazasDAO=new AdminPlazasDAO();
		this.plazasDisponibles=plazasDAO.obtenerPlazasString();
		this.map=this.iniciarMapa();
		this.rutasDisponibles=this.rutasDAO.obtenerRutas();
	}

	
	
	public Integer getVehiculosSeleccion() {
		return vehiculosSeleccion;
	}



	public void setVehiculosSeleccion(Integer vehiculosSeleccion) {
		this.vehiculosSeleccion = vehiculosSeleccion;
	}



	public Integer getVehiculosSeleccion2() {
		return vehiculosSeleccion2;
	}



	public void setVehiculosSeleccion2(Integer vehiculosSeleccion2) {
		this.vehiculosSeleccion2 = vehiculosSeleccion2;
	}



	public HashMap<String, Integer> getVehiculosDisponibles2() {
		return vehiculosDisponibles2;
	}



	public void setVehiculosDisponibles2(
			HashMap<String, Integer> vehiculosDisponibles2) {
		this.vehiculosDisponibles2 = vehiculosDisponibles2;
	}



	public Integer getVehiculosSeleccion3() {
		return vehiculosSeleccion3;
	}



	public void setVehiculosSeleccion3(Integer vehiculosSeleccion3) {
		this.vehiculosSeleccion3 = vehiculosSeleccion3;
	}



	public HashMap<String, Integer> getVehiculosDisponibles3() {
		return vehiculosDisponibles3;
	}



	public void setVehiculosDisponibles3(
			HashMap<String, Integer> vehiculosDisponibles3) {
		this.vehiculosDisponibles3 = vehiculosDisponibles3;
	}



	public Integer getVehiculosSeleccion4() {
		return vehiculosSeleccion4;
	}



	public void setVehiculosSeleccion4(Integer vehiculosSeleccion4) {
		this.vehiculosSeleccion4 = vehiculosSeleccion4;
	}



	public HashMap<String, Integer> getVehiculosDisponibles4() {
		return vehiculosDisponibles4;
	}



	public void setVehiculosDisponibles4(
			HashMap<String, Integer> vehiculosDisponibles4) {
		this.vehiculosDisponibles4 = vehiculosDisponibles4;
	}



	public Integer getVehiculosSeleccion5() {
		return vehiculosSeleccion5;
	}



	public void setVehiculosSeleccion5(Integer vehiculosSeleccion5) {
		this.vehiculosSeleccion5 = vehiculosSeleccion5;
	}



	public HashMap<String, Integer> getVehiculosDisponibles5() {
		return vehiculosDisponibles5;
	}



	public void setVehiculosDisponibles5(
			HashMap<String, Integer> vehiculosDisponibles5) {
		this.vehiculosDisponibles5 = vehiculosDisponibles5;
	}



	public HashMap<String, Integer> getPlazasDisponibles() {
		return plazasDisponibles;
	}

	public void setPlazasDisponibles(HashMap<String, Integer> plazasDisponibles) {
		this.plazasDisponibles = plazasDisponibles;
	}

	public Integer getPlazaSeleccionada() {
		return plazaSeleccionada;
	}

	public void setPlazaSeleccionada(Integer plazaSeleccionada) {
		this.plazaSeleccionada = plazaSeleccionada;
	}

	public List<String> getTurnosSeleccion() {
		return turnosSeleccion;
	}

	public void setTurnosSeleccion(List<String> turnosSeleccion) {
		this.turnosSeleccion = turnosSeleccion;
	}

	public HashMap<String, String> getTurnosDisponibles() {
		return turnosDisponibles;
	}

	public void setTurnosDisponibles(HashMap<String, String> turnosDisponibles) {
		this.turnosDisponibles = turnosDisponibles;
	}

	public HashMap<String, Integer> getGeocercasDisponibles() {
		return geocercasDisponibles;
	}

	public void setGeocercasDisponibles(
			HashMap<String, Integer> geocercasDisponibles) {
		this.geocercasDisponibles = geocercasDisponibles;
	}

	public Integer getGeocercaSeleccionada() {
		return geocercaSeleccionada;
	}

	public void setGeocercaSeleccionada(Integer geocercaSeleccionada) {
		this.geocercaSeleccionada = geocercaSeleccionada;
	}

	
	
	public HashMap<String, Integer> getVehiculosDisponibles() {
		return vehiculosDisponibles;
	}
	public void setVehiculosDisponibles(
			HashMap<String, Integer> vehiCulosDisponibles) {
		this.vehiculosDisponibles = vehiCulosDisponibles;
	}
	


	public Usuario getUsuario() {
		usuario=this.obtenerUsuario();
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public Ruta getBuscar() {
		return buscar;
	}
	public void setBuscar(Ruta buscar) {
		this.buscar = buscar;
	}
	public ReportesRutasDAO getRepo() {
		return repo;
	}
	public void setRepo(ReportesRutasDAO repo) {
		this.repo = repo;
	}
	public Map getMap() {
		return map;
	}
	public void setMap(Map map) {
		this.map = map;
	}
	public List<Ruta> getRutasDisponibles() {
		return rutasDisponibles;
	}
	public void setRutasDisponibles(List<Ruta> rutasDisponibles) {
		this.rutasDisponibles = rutasDisponibles;
	}
	public Ruta getRutaEdit() {
		return rutaEdit;
	}
	public void setRutaEdit(Ruta rutaEdit) {
		this.rutaEdit = rutaEdit;
	}
	public Ruta getDeleteRuta() {
		return deleteRuta;
	}
	public void setDeleteRuta(Ruta deleteRuta) {
		this.deleteRuta = deleteRuta;
	}
	public Boolean getBanderaSucces() {
		return banderaSucces;
	}
	public void setBanderaSucces(Boolean banderaSucces) {
		this.banderaSucces = banderaSucces;
	}
	
	public String generaReporte(){
		if(this.rutasDisponibles!=null){
		if(!this.rutasDisponibles.isEmpty()){
		repo.crearReportePlazas(this.rutasDisponibles);
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
	
	public String buscarR(){
		if((buscar.getRutLla()==null || buscar.getRutLla()==0) && 
				(buscar.getRutNom()==null || buscar.getRutNom().equals("")) && 
				(buscar.getRutAnc()==null || buscar.getRutAnc()==0.0) && 
				(buscar.getRutFAl()==null || buscar.getRutFAl().equals("")) && 
				(buscar.getRutFBa()==null || buscar.getRutFBa().equals("")) && 
				(buscar.getRutDes()==null || buscar.getRutDes().equals("")) && 
				(buscar.getRutFec()==null || buscar.getRutFec().equals("")) && 
				(buscar.getRutFeI()==null || buscar.getRutFeI().equals("")) && 
				(buscar.getRutFeF()==null || buscar.getRutFeF().equals(""))){
			FacesContext context = FacesContext.getCurrentInstance();
	        context.addMessage(null, new FacesMessage(Constantes.noParametrosBusqueda,"Información"  ) );	
		}else{
			getMap().getChildren().clear();
			setRutaEdit(new Ruta());
			this.rutasDisponibles=rutasDAO.busqueda(rutasDAO.transToquens(buscar),getUsuario());
			FacesContext context = FacesContext.getCurrentInstance();
	        context.addMessage(null, new FacesMessage(Constantes.exito,"Información"  ) );
	        context.getPartialViewContext().getRenderIds().add("perfiles");
	        context.getPartialViewContext().getRenderIds().add("edit");
		}
		return "";
	}
	
public String deleteRuta(Ruta ruta){
	rutasDAO.deleteRuta(ruta);
	FacesContext context = FacesContext.getCurrentInstance();
    context.addMessage(null, new FacesMessage("Operación Realizada con Exito","Información"  ) );
    context.getPartialViewContext().getRenderIds().add("vehiculos");
    this.rutasDisponibles=rutasDAO.obtenerRutas();
	return null;
}
public String editRuta(Ruta ruta){
	map=this.iniciarMapa();
	setRutaEdit(ruta);
	if(ruta.getVehiculo1()!=null)
		this.vehiculosSeleccion=ruta.getVehiculo1().getVehLla();
		if(ruta.getVehiculo2()!=null)
		this.vehiculosSeleccion2=ruta.getVehiculo2().getVehLla();
		if(ruta.getVehiculo3()!=null)
		this.vehiculosSeleccion3=ruta.getVehiculo3().getVehLla();
		if(ruta.getVehiculo4()!=null)
		this.vehiculosSeleccion4=ruta.getVehiculo4().getVehLla();
		if(ruta.getVehiculo5()!=null)
		this.vehiculosSeleccion5=ruta.getVehiculo5().getVehLla();
	getMap().getChildren().clear();
	rutasDAO.agregarMarcasEditable(getRutaEdit().getRutLla(), getMap());
	FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("edit");
	return null;
}

public String guardaRutaEditada(){
	try{
		this.getRutaEdit().setUsuario(getUsuario());
		Vehiculo v1=this.adminVehi.obtenerVehiculoXId(vehiculosSeleccion);
		Vehiculo v2=this.adminVehi.obtenerVehiculoXId(vehiculosSeleccion2);
		Vehiculo v3=this.adminVehi.obtenerVehiculoXId(vehiculosSeleccion3);
		Vehiculo v4=this.adminVehi.obtenerVehiculoXId(vehiculosSeleccion4);
		Vehiculo v5=this.adminVehi.obtenerVehiculoXId(vehiculosSeleccion5);
		this.getRutaEdit().setVehiculo1(v1);
		this.getRutaEdit().setVehiculo2(v2);
		this.getRutaEdit().setVehiculo3(v3);
		this.getRutaEdit().setVehiculo4(v4);
		this.getRutaEdit().setVehiculo5(v5);
		
		getRutaEdit().setRutGeo(this.cercasDAO.obtenerCercaXId(geocercaSeleccionada));
		getRutaEdit().setTurnos(this.turnosDAO.obtenerTurnosXIdsS(turnosSeleccion));
		getRutaEdit().setPlaza(this.plazasDAO.obtenerPlazaXId(plazaSeleccionada));
	this.rutasDAO.updateRuta(getRutaEdit());
	this.rutasDAO.guardarMarcas(getMap());
	setBanderaSucces(true);
	FacesContext context = FacesContext.getCurrentInstance();
    
    context.addMessage(null, new FacesMessage("Operación Realizada con Exito","Información"  ) );
	}catch(Exception e){
		e.printStackTrace();
	}finally{
		setBanderaSucces(false);
	}
	FacesContext context = FacesContext.getCurrentInstance();
	context.getPartialViewContext().getRenderIds().add("vehiculos");
	this.rutasDisponibles=this.rutasDAO.obtenerRutas();
	return null;
}
public String agregarRuta(){
	return "ruta";
}
public Usuario obtenerUsuario(){
	FacesContext facesContext = FacesContext.getCurrentInstance();
	Login login
    = (Login)facesContext.getApplication()
      .createValueBinding("#{login}").getValue(facesContext);
	return login.geteUsuario();		
}

public Map iniciarMapa(){
	Map map=new Map();
	map.setLatitude("19.055663");
	map.setLongitude("-98.154700");
	return map;
}
}
