package com.cj.catalogosInterfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.cj.Interfaz.Login;
import com.cj.ReportesDAO.ReportesClientesDAO;
import com.cj.catalogosDAO.AdminClientesDAO;
import com.cj.dao.InterfazAdminVehicDAO;
import com.cj.dao.InterfazDAO;
import com.cj.pojos.Cliente;
import com.cj.pojos.Usuario;
import com.cj.pojos.Vehiculo;
import com.cj.utils.Constantes;

public class AdminClientes {
	private InterfazDAO interfazDAO = new InterfazDAO();
	private AdminClientesDAO clientesDAO = new AdminClientesDAO();
	private Cliente buscar=new Cliente();
	private List<Cliente> clientesDisponibles;// = clientesDAO.obtenerClientes();
	private Cliente clienteEdit;
	private Boolean banderaSucces = true;
	private ReportesClientesDAO repo=new ReportesClientesDAO();
	private InterfazAdminVehicDAO vehicDAO;
	
	private List<String> vehiculosSeleccion;
	private HashMap<String, Integer> vehiculosDisponibles;
	

	private Usuario usuario;
	
	public AdminClientes(){
		
		this.usuario=this.obtenerUsuario();
		this.vehicDAO=new InterfazAdminVehicDAO();
		Usuario u=this.getUsuario();
		if(u!=null){
			clientesDisponibles=clientesDAO.obtenerClientes();
			this.vehiculosDisponibles=this.vehicDAO.obtenerVehiculosString(u);
		}
	
	}
	
	
	









	public List<String> getVehiculosSeleccion() {
		return vehiculosSeleccion;
	}












	public void setVehiculosSeleccion(List<String> vehiculosSeleccion) {
		this.vehiculosSeleccion = vehiculosSeleccion;
	}












	public HashMap<String, Integer> getVehiculosDisponibles() {
		return this.vehicDAO.obtenerVehiculosString(getUsuario());
	}




	public void setVehiculosDisponibles(
			HashMap<String, Integer> vehiculosDisponibles) {
		this.vehiculosDisponibles = vehiculosDisponibles;
	}








	public HashMap<String, Integer> getVehiculosDisponibles2() {
		return this.vehicDAO.obtenerVehiculosString(getUsuario());
	}











	public HashMap<String, Integer> getVehiculosDisponibles3() {
		return this.vehicDAO.obtenerVehiculosString(getUsuario());
	}









	public HashMap<String, Integer> getVehiculosDisponibles4() {
		return this.vehicDAO.obtenerVehiculosString(getUsuario());
	}






	public HashMap<String, Integer> getVehiculosDisponibles5() {
		return this.vehicDAO.obtenerVehiculosString(getUsuario());
	}






	public HashMap<String, String> getVehiculos() {
		return this.vehicDAO.obtenerVehiculosSS(getUsuario());
	}






	public Usuario getUsuario(){
		return this.obtenerUsuario();
	}
	public void setUsuario(Usuario usuario){
		this.usuario=usuario;
	}
	


	public Cliente getBuscar() {
		return buscar;
	}

	public void setBuscar(Cliente buscar) {
		this.buscar = buscar;
	}

	public List<Cliente> getClientesDisponibles() {
		return clientesDisponibles;
	}

	public void setClientesDisponibles(List<Cliente> clientesDisponibles) {
		this.clientesDisponibles = clientesDisponibles;
	}

	public Cliente getClienteEdit() {
		return clienteEdit;
	}

	public void setClienteEdit(Cliente clienteEdit) {
		this.clienteEdit = clienteEdit;
	}

	public Boolean getBanderaSucces() {
		return banderaSucces;
	}

	public void setBanderaSucces(Boolean banderaSucces) {
		this.banderaSucces = banderaSucces;
	}

	public String generaReporte(){
		if(this.clientesDisponibles!=null){
		if(!this.clientesDisponibles.isEmpty()){
		repo.crearReporteClientesGeneral(this.clientesDisponibles);
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
		if(		(buscar.getCliLla()==null  || buscar.getCliLla()==0) &&
				(buscar.getCliFAl()==null  || buscar.getCliFAl().equals("")) &&
				(buscar.getClifBa()==null  || buscar.getClifBa().equals("")) &&
				(buscar.getCliNom()==null  || buscar.getCliNom().equals("")) &&
				(buscar.getCliDom()==null  || buscar.getCliDom().equals("")) &&
				(buscar.getCliCol()==null  || buscar.getCliCol().equals("")) &&
				(buscar.getCliCOP()==null  || buscar.getCliCOP()==0) &&
				(buscar.getCliCiu()==null  || buscar.getCliCiu().equals("")) &&
				(buscar.getCliEdo()==null  || buscar.getCliEdo().equals("")) &&
				(buscar.getCliPai()==null  || buscar.getCliPai().equals("")) &&
				(buscar.getCliCo1()==null  || buscar.getCliCo1().equals("")) &&
				(buscar.getCliT11()==null  || buscar.getCliT11().equals("")) &&
				(buscar.getCliT21()==null  || buscar.getCliT21().equals("")) &&
				(buscar.getCliC11()==null  || buscar.getCliC11().equals("")) &&
				(buscar.getCliC21()==null  || buscar.getCliC21().equals("")) &&
				(buscar.getCliCo2()==null  || buscar.getCliCo2().equals("")) &&
				(buscar.getCliT12()==null  || buscar.getCliT12().equals("")) &&
				(buscar.getCliT22()==null  || buscar.getCliT22().equals("")) &&
				(buscar.getCliC12()==null  || buscar.getCliC12().equals("")) &&
				(buscar.getCliC22()==null  || buscar.getCliC22().equals(""))){
			FacesContext context = FacesContext.getCurrentInstance();
	        context.addMessage(null, new FacesMessage(Constantes.noParametrosBusqueda,"Información"  ) );	
		}else{
			this.clientesDisponibles=clientesDAO.busqueda(clientesDAO.transToquens(buscar));
			FacesContext context = FacesContext.getCurrentInstance();
	        context.addMessage(null, new FacesMessage(Constantes.exito,"Información"  ) );
	        context.getPartialViewContext().getRenderIds().add("clientes");
		}
	return "";	
	}
	
	public String deleteCliente(Cliente cliente) {
		try {

			this.clientesDAO.deleteCliente(cliente);
//			clientesDisponibles = clientesDAO.obtenerClientes();
			FacesContext context = FacesContext.getCurrentInstance();

			context.addMessage(null, new FacesMessage(
					"Operación Realizada con Exito", "Información"));
			context.getPartialViewContext().getRenderIds().add("clientes");
			clientesDisponibles=clientesDAO.obtenerClientes();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			setBanderaSucces(false);
		}
		return null;
	}

	public String guardaClienteEditado() {
		try {
			this.getClienteEdit().setEstadoRegistro(true);
			this.getClienteEdit().setVehiculos(this.vehicDAO.obtenerVehiculosListString(vehiculosSeleccion));
			
			this.clientesDAO.updateCliente(this.getClienteEdit());
			setBanderaSucces(true);
			
//			this.clientesDisponibles = clientesDAO.obtenerClientes();
			FacesContext context = FacesContext.getCurrentInstance();

			context.addMessage(null, new FacesMessage(
					"Operación Realizada con Exito", "Información"));
			clientesDisponibles=clientesDAO.obtenerClientes();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			setBanderaSucces(false);
		}
		// TODO: cambiar por acceso a la base de datos
		return "muestraClientes";
	}

	public String regresarMuestraClientes() {
		this.setClienteEdit(null);
		clientesDisponibles=clientesDAO.obtenerClientes();
		// TODO: cambiar por acceso a la base de datos
		return "muestraClientes";
	}

	public String editCliente(Cliente c) {
		this.clienteEdit = c;
		this.vehiculosSeleccion=new ArrayList<String>();
		List<Vehiculo> vs=c.getVehiculos();
		if(vs!=null)
		for(Vehiculo v:vs){
			this.vehiculosSeleccion.add(v.getVehLla()+"");
		}
		return "editCliente";
	}
	public String agregarCliente() {
		this.clienteEdit = new Cliente();
		return "editCliente";
	}
	
	public Usuario obtenerUsuario(){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Login login
	    = (Login)facesContext.getApplication()
	      .createValueBinding("#{login}").getValue(facesContext);
		return login.geteUsuario();		
	}
}
