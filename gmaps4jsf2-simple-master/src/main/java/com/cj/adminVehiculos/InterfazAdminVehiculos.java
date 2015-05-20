package com.cj.adminVehiculos;

import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.cj.Interfaz.Login;
import com.cj.ReportesDAO.ReportesVehiculosDAO;
import com.cj.catalogosDAO.AdminCercasDAO;
import com.cj.catalogosDAO.RutasDAO;
import com.cj.dao.InterfazAdminVehicDAO;
import com.cj.dao.InterfazDAO;
import com.cj.pojos.Cerca;
import com.cj.pojos.Ruta;
import com.cj.pojos.Usuario;
import com.cj.pojos.Vehiculo;
import com.cj.utils.Constantes;

public class InterfazAdminVehiculos {
	private InterfazDAO interfazDAO=new InterfazDAO();
	private InterfazAdminVehicDAO interfazAdminDAO=new InterfazAdminVehicDAO();
	private Usuario usuario;
	private List<Vehiculo> vehiculosDisponibles;//=interfazDAO.obtenerVehiculos();
	private Vehiculo vehiculoEdit;
	private Boolean banderaSucces=true;
	private Vehiculo buscar=new Vehiculo();
	private ReportesVehiculosDAO repo=new ReportesVehiculosDAO();
	
	private RutasDAO rutasDAO=new RutasDAO();
	private HashMap<String, Integer> rutasDisponibles;
	private Integer rutaSeleccionado;
	private HashMap<String,Integer> estatus;
//	private List<Integer> estatus;
	private Integer estatuss;

	private AdminCercasDAO cercasDAO=new AdminCercasDAO();
	
	public InterfazAdminVehiculos(){
		this.usuario=this.getUsuario();
		if(usuario!=null)
		this.vehiculosDisponibles=interfazDAO.obtenerVehiculos(usuario);
		this.rutasDisponibles=rutasDAO.obtenerRutasString();
		estatus=new HashMap<String,Integer>();
		estatus.put("Disponible",1);
		estatus.put("No Disponible", 0);
	}
	
	
	
	public HashMap<String,Integer> getEstatus() {
		return estatus;
	}



	public void setEstatus(HashMap<String,Integer> estatus) {
		this.estatus = estatus;
	}



	public Integer getEstatuss() {
		return estatuss;
	}



	public void setEstatuss(Integer estatuss) {
		this.estatuss = estatuss;
	}



	public Usuario getUsuario() {
		usuario=this.obtenerUsuario();
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public HashMap<String, Integer> getRutasDisponibles() {
		return rutasDisponibles;
	}
	public void setRutasDisponibles(HashMap<String, Integer> rutasDisponibles) {
		this.rutasDisponibles = rutasDisponibles;
	}
	public Integer getRutaSeleccionado() {
		return rutaSeleccionado;
	}
	public void setRutaSeleccionado(Integer rutaSeleccionado) {
		this.rutaSeleccionado = rutaSeleccionado;
	}
	public Vehiculo getBuscar() {
		return buscar;
	}
	public void setBuscar(Vehiculo buscar) {
		this.buscar = buscar;
	}
	
	public Boolean getBanderaSucces() {
		return banderaSucces;
	}

	public void setBanderaSucces(Boolean banderaSucces) {
		this.banderaSucces = banderaSucces;
	}

	public Vehiculo getVehiculoEdit() {
		return vehiculoEdit;
	}

	public void setVehiculoEdit(Vehiculo vehiculoEdit) {
		this.vehiculoEdit = vehiculoEdit;
	}

	public List<Vehiculo> getVehiculosDisponibles() {
		return vehiculosDisponibles;
	}

	public void setVehiculosDisponibles(List<Vehiculo> vehiculosDisponibles) {
		this.vehiculosDisponibles = vehiculosDisponibles;
	}

	public String generaReporte(){
		if(this.vehiculosDisponibles!=null){
		if(!this.vehiculosDisponibles.isEmpty()){
		repo.crearReporteVehiculosGeneral(this.vehiculosDisponibles);
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
	
	public String buscarV(){
		if((this.rutaSeleccionado==null || this.rutaSeleccionado==0) &&
				(buscar.getVehFAl()==null)&&
				(buscar.getVehFBa()==null) &&
				(buscar.getVehDes()==null || buscar.getVehDes().equals("")) && 
				(buscar.getVehMar()==null || buscar.getVehMar().equals("")) && 
				(buscar.getVehTip()==null || buscar.getVehTip().equals("")) && 
				(buscar.getVehMod()==null || buscar.getVehMod().equals("")) && 
				(buscar.getVehTar()==null || buscar.getVehTar().equals("")) && 
				(buscar.getVehCap()==null || buscar.getVehCap().equals("")) && 
				(buscar.getVehCol()==null || buscar.getVehCol().equals("")) && 
				(buscar.getVehVeM()==null || buscar.getVehVeM().equals("")) &&
				(buscar.getVehImei()==null || buscar.getVehImei().equals("")) &&
				(buscar.getVehNoCel()==null || buscar.getVehNoCel().equals("")) &&
				(buscar.getVehEsD()==null || buscar.getVehEsD()==0) &&
				(buscar.getVehPuerto()==null || buscar.getVehPuerto()==0)){
			FacesContext context = FacesContext.getCurrentInstance();
	        context.addMessage(null, new FacesMessage(Constantes.noParametrosBusqueda,"Información"  ) );	
		}else{
//			this.getBuscar().setVehRu(new Ruta(this.rutaSeleccionado));
			this.vehiculosDisponibles=interfazAdminDAO.busqueda(interfazAdminDAO.transToquens(getBuscar()));
			FacesContext context = FacesContext.getCurrentInstance();
	        context.addMessage(null, new FacesMessage(Constantes.exito,"Información"  ) );
	        context.getPartialViewContext().getRenderIds().add("vehiculos");
		}
		return null;
	}
	
	public String deleteVehiculo(Vehiculo vehiculo){
		System.out.println(vehiculo.getVehImei());
		List<Cerca> cercas=cercasDAO.obtenerCercaPorVehiculo(vehiculo.getVehLla());
		List<Ruta> rutas=rutasDAO.obtenerRutaPorVehiculo(vehiculo.getVehImei());
		for(Ruta ruta:rutas){
			if(ruta.getVehiculo1().getVehLla()==vehiculo.getVehLla()){
				ruta.setVehiculo1(null);
			}
			if(ruta.getVehiculo2().getVehLla()==vehiculo.getVehLla()){
				ruta.setVehiculo2(null);
			}
			if(ruta.getVehiculo3().getVehLla()==vehiculo.getVehLla()){
				ruta.setVehiculo3(null);
			}
			if(ruta.getVehiculo4().getVehLla()==vehiculo.getVehLla()){
				ruta.setVehiculo4(null);
			}
			if(ruta.getVehiculo5().getVehLla()==vehiculo.getVehLla()){
				ruta.setVehiculo5(null);
			}
			rutasDAO.updateRuta(ruta);
		}
		for(Cerca cerca:cercas){
			if(cerca.getVehiculos()!=null)
				cerca.setVehiculos(null);
			cercasDAO.updateCerca(cerca);
		}
		interfazAdminDAO.deleteVehiculo(vehiculo);
		
FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Operación Realizada con Exito","Información"  ) );
        context.getPartialViewContext().getRenderIds().add("vehiculos");
//        this.vehiculosDisponibles=interfazDAO.obtenerVehiculos();
        this.vehiculosDisponibles=interfazDAO.obtenerVehiculos(getUsuario());
		return null;
	}
	
	public String editVehiculo(Vehiculo vehiculo){
		setVehiculoEdit(vehiculo);
		return "editVehiculo";
	}
	
	public String guardaVehiculoEditado(){
		try{
			this.getVehiculoEdit().setEstadoRegistro(true);
//			this.getVehiculoEdit().setVehRu(this.rutasDAO.obtenerRutaById(rutaSeleccionado));
			this.getVehiculoEdit().setUsuario(getUsuario());
			this.getVehiculoEdit().setVehEsD(estatuss);
		this.interfazAdminDAO.updateVehiculo(this.getVehiculoEdit());
		setBanderaSucces(true);
		FacesContext context = FacesContext.getCurrentInstance();
        
        context.addMessage(null, new FacesMessage("Operación Realizada con Exito","Información"  ) );
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			setBanderaSucces(false);
		}
		this.vehiculosDisponibles=interfazDAO.obtenerVehiculos(getUsuario());
		//TODO: cambiar por acceso a la base de datos
		return "muestraVehiculos";
	}
	
	public String regresarMuestraVehiculos(){
		this.setVehiculoEdit(null);
//		this.vehiculosDisponibles=interfazDAO.obtenerVehiculos();
		//TODO: cambiar por acceso a la base de datos
		return "muestraVehiculos";
	}
	
	public String agregarVehiculo(){
		this.vehiculoEdit=new Vehiculo();
		return "editVehiculo";
	}
	
	public Usuario obtenerUsuario(){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Login login
	    = (Login)facesContext.getApplication()
	      .createValueBinding("#{login}").getValue(facesContext);
		return login.geteUsuario();		
	}
}
