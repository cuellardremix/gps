package com.cj.catalogosInterfaces;

import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import com.cj.Interfaz.Login;
import com.cj.ReportesDAO.ReportesUsuariosDAO;
import com.cj.catalogosDAO.AdminClientesDAO;
import com.cj.catalogosDAO.AdminPerfilesDAO;
import com.cj.catalogosDAO.AdminUsuariosDAO;
import com.cj.dao.InterfazDAO;
import com.cj.pojos.Perfil;
import com.cj.pojos.Usuario;
import com.cj.utils.Constantes;

public class AdminUsuarios {

	private InterfazDAO interfazDAO=new InterfazDAO();
	private AdminUsuariosDAO usuariosDAO=new AdminUsuariosDAO();
	private Usuario buscar=new Usuario();
	private ReportesUsuariosDAO repo=new ReportesUsuariosDAO();
	private List<Usuario> usuariosDisponibles;//=usuariosDAO.obtenerUsuarios();
	private Usuario usuarioEdit;
	private Boolean banderaSucces=true;
	private AdminPerfilesDAO perfilesDAO=new AdminPerfilesDAO();
	private HashMap<String, Integer> perfilesDisponibles=perfilesDAO.obtenerPerfilesString();
	private Integer perfilSeleccionado;
	private HashMap<String, Integer> clientesDisponibles;
	private Integer clienteSeleccionado;
	private AdminClientesDAO clientesDAO;
	private Boolean isCliente=false;
	private Usuario usuario;
	
	public AdminUsuarios(){
		usuario=this.obtenerUsuario();
		if(usuario!=null){
			usuariosDisponibles=usuariosDAO.obtenerUsuarios();
			clientesDAO=new AdminClientesDAO();
			clientesDisponibles=clientesDAO.obtenerClientesString();	
		}
	}
	

	public Usuario getUsuario() {
		return usuario;
	}



	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}



	public Boolean getIsCliente() {
		return isCliente;
	}


	public void setIsCliente(Boolean isCliente) {
		this.isCliente = isCliente;
	}


	public HashMap<String, Integer> getClientesDisponibles() {
		return clientesDAO.obtenerClientesString();
	}


	public void setClientesDisponibles(HashMap<String, Integer> clientesDisponibles) {
		this.clientesDisponibles = clientesDisponibles;
	}


	public Integer getClienteSeleccionado() {
		return clienteSeleccionado;
	}


	public void setClienteSeleccionado(Integer clienteSeleccionado) {
		this.clienteSeleccionado = clienteSeleccionado;
	}


	public Integer getPerfilSeleccionado() {
		return perfilSeleccionado;
	}
	public void setPerfilSeleccionado(Integer perfilSeleccionado) {
		this.perfilSeleccionado = perfilSeleccionado;
	}
	public HashMap<String, Integer> getPerfilesDisponibles() {
		return perfilesDisponibles;
	}
	public void setPerfilesDisponibles(HashMap<String, Integer> perfilesDisponibles) {
		this.perfilesDisponibles = perfilesDisponibles;
	}
	public Usuario getBuscar() {
		return buscar;
	}
	public void setBuscar(Usuario buscar) {
		this.buscar = buscar;
	}
	public List<Usuario> getUsuariosDisponibles() {
		return usuariosDisponibles;
	}
	public void setUsuariosDisponibles(List<Usuario> usuariosDisponibles) {
		this.usuariosDisponibles = usuariosDisponibles;
	}
	public Usuario getUsuarioEdit() {
		return usuarioEdit;
	}
	public void setUsuarioEdit(Usuario usuarioEdit) {
		this.usuarioEdit = usuarioEdit;
	}
	public Boolean getBanderaSucces() {
		return banderaSucces;
	}
	public void setBanderaSucces(Boolean banderaSucces) {
		this.banderaSucces = banderaSucces;
	}

	public String editUsuario(Usuario usuario){
		setUsuarioEdit(usuario);
		return "editUsuario";
	}
	
	public String generaReporte(){
		if(this.usuariosDisponibles!=null){
		if(!this.usuariosDisponibles.isEmpty()){
		repo.crearReporteUsuarios(this.usuariosDisponibles);
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
	
	public String buscarU(){
		if((this.perfilSeleccionado==null || this.perfilSeleccionado==0) &&
				( buscar. getUsuLla()==null || buscar. getUsuLla()==0) &&
				( buscar. getUsuFal()==null || buscar.getUsuFal().equals("")  ) &&
				( buscar. getUsuFBa()==null || buscar. getUsuFBa().equals("")  ) &&
				( buscar. getUsuNom()==null || buscar. getUsuNom().equals("")  ) &&
				( buscar. getUsuCon()==null || buscar. getUsuCon().equals("")  )){
			FacesContext context = FacesContext.getCurrentInstance();
	        context.addMessage(null, new FacesMessage(Constantes.noParametrosBusqueda,"Información"  ) );	
		}else{
			buscar.setPerfil(new Perfil(this.perfilSeleccionado));
			this.usuariosDisponibles=usuariosDAO.busqueda(usuariosDAO.transToquens(buscar));
			FacesContext context = FacesContext.getCurrentInstance();
	        context.addMessage(null, new FacesMessage(Constantes.exito,"Información"  ) );
	        context.getPartialViewContext().getRenderIds().add("clientes");
		}
		return "";
	}
	
	public String deleteUsuario(Usuario usuario){
		try{

		this.usuariosDAO.deleteUsuario(usuario);
//		usuariosDisponibles=usuariosDAO.obtenerUsuarios();
		FacesContext context = FacesContext.getCurrentInstance();
        
        context.addMessage(null, new FacesMessage("Operación Realizada con Exito","Información"  ) );
        context.getPartialViewContext().getRenderIds().add("usuarios");
        usuariosDisponibles=usuariosDAO.obtenerUsuarios();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			setBanderaSucces(false);
		}
		return null;
	}
	
	public String guardaUsuarioEditado(){
		try{
			this.getUsuarioEdit().setEstadoRegistro(true);
			this.getUsuarioEdit().setPerfil(this.perfilesDAO.obtenerPerfilById(perfilSeleccionado));
			this.getUsuarioEdit().setCliente(clientesDAO.obtenerClienteXId(clienteSeleccionado));
		this.usuariosDAO.updateUsuario(this.getUsuarioEdit());
		setBanderaSucces(true);
//		usuariosDisponibles=usuariosDAO.obtenerUsuarios();
		FacesContext context = FacesContext.getCurrentInstance();
		this.usuariosDisponibles=usuariosDAO.obtenerUsuarios();
        context.addMessage(null, new FacesMessage("Operación Realizada con Exito","Información"  ) );
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			setBanderaSucces(false);
		}
		//TODO: cambiar por acceso a la base de datos
		return "muestraUsuarios";
	}
	
	public String regresarMuestraUsuarios(){
		this.setUsuarioEdit(null);
		this.usuariosDisponibles=usuariosDAO.obtenerUsuarios();
		//TODO: cambiar por acceso a la base de datos
		return "muestraUsuarios";
	}
	
	public String agregarUsuario(){
		this.usuarioEdit=new Usuario();
		return "editUsuario";
	}
	public void addCliente(){
		System.out.println("asdas"+this.perfilSeleccionado);
		if(this.perfilSeleccionado==Constantes.perfilCliente){
			this.isCliente=true;
			FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("cliente");
		}else
			isCliente=false;
		FacesContext.getCurrentInstance().getPartialViewContext().getRenderIds().add("cliente");
	}
	
	public Usuario obtenerUsuario(){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Login login
	    = (Login)facesContext.getApplication()
	      .createValueBinding("#{login}").getValue(facesContext);
		return login.geteUsuario();		
	}
}
