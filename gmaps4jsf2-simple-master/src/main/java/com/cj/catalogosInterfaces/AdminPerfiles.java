package com.cj.catalogosInterfaces;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.cj.ReportesDAO.ReportesPerfilesDAO;
import com.cj.catalogosDAO.AdminPerfilesDAO;
import com.cj.dao.InterfazDAO;
import com.cj.pojos.Perfil;
import com.cj.pojos.Plaza;
import com.cj.utils.Constantes;

public class AdminPerfiles {
	private InterfazDAO interfazDAO = new InterfazDAO();
	private AdminPerfilesDAO perfilesDAO=new AdminPerfilesDAO();
	private ReportesPerfilesDAO repo=new ReportesPerfilesDAO();
	private Perfil buscar=new Perfil();
	private List<Perfil> perfilesDisponibles; //= perfilesDAO.obtenerPerfiles();
	private Perfil perfilEdit;
	private Boolean banderaSucces = true;

	public AdminPerfiles(){
		perfilesDisponibles= perfilesDAO.obtenerPerfiles();
	}
	
	public Perfil getBuscar() {
		return buscar;
	}
	public void setBuscar(Perfil buscar) {
		this.buscar = buscar;
	}
	public List<Perfil> getPerfilesDisponibles() {
		return perfilesDisponibles;
	}
	public void setPerfilesDisponibles(List<Perfil> perfilesDisponibles) {
		this.perfilesDisponibles = perfilesDisponibles;
	}
	public Perfil getPerfilEdit() {
		return perfilEdit;
	}
	public void setPerfilEdit(Perfil perfilEdit) {
		this.perfilEdit = perfilEdit;
	}
	public Boolean getBanderaSucces() {
		return banderaSucces;
	}
	public void setBanderaSucces(Boolean banderaSucces) {
		this.banderaSucces = banderaSucces;
	}
	
	public String generaReporte(){
		if(this.perfilesDisponibles!=null){
		if(!this.perfilesDisponibles.isEmpty()){
		repo.crearReportePerfilesGeneral(this.perfilesDisponibles);
		}else{
			FacesContext context = FacesContext.getCurrentInstance();
	        context.addMessage(null, new FacesMessage(Constantes.noParametrosBusqueda,"Informaci�n"  ) );
		}
		}else{
			FacesContext context = FacesContext.getCurrentInstance();
	        context.addMessage(null, new FacesMessage(Constantes.noResultadosDeBusqueda,"Informaci�n"  ) );
		}
		return null;
	}
	
	public String buscarP(){
		if(		( buscar.getPerLla()==null  || buscar.getPerLla()==0  ) &&
				( buscar.getPerFAl()==null  || buscar.getPerFAl().equals("")  ) &&
				( buscar.getPerFBa()==null  || buscar.getPerFBa().equals("")  ) &&
				( buscar.getPerNom()==null  || buscar.getPerNom().equals("")  ) &&
				( buscar.getPerTip()==null  || buscar.getPerTip().equals("")  ) ){
			FacesContext context = FacesContext.getCurrentInstance();
	        context.addMessage(null, new FacesMessage(Constantes.noParametrosBusqueda,"Informaci�n"  ) );	
		}else{
			this.perfilesDisponibles=perfilesDAO.busqueda(perfilesDAO.transToquens(buscar));
			FacesContext context = FacesContext.getCurrentInstance();
	        context.addMessage(null, new FacesMessage(Constantes.exito,"Informaci�n"  ) );
	        context.getPartialViewContext().getRenderIds().add("perfiles");
		}
		return "";
	}
	
	public String deletePerfil(Perfil perfil) {
		try {

			this.perfilesDAO.deletePerfil(perfil);
//			perfilesDisponibles = perfilesDAO.obtenerPerfiles();
			FacesContext context = FacesContext.getCurrentInstance();

			context.addMessage(null, new FacesMessage(
					"Operaci�n Realizada con Exito", "Informaci�n"));
			context.getPartialViewContext().getRenderIds().add("pefiles");
			perfilesDisponibles= perfilesDAO.obtenerPerfiles();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			setBanderaSucces(false);
		}
		return null;
	}
	
	public String guardaPerfilEditado() {
		try {
			this.getPerfilEdit().setEstadoRegistro(true);
			this.perfilesDAO.updatePerfil(this.getPerfilEdit());
			setBanderaSucces(true);
//			this.perfilesDisponibles = perfilesDAO.obtenerPerfiles();
			FacesContext context = FacesContext.getCurrentInstance();

			context.addMessage(null, new FacesMessage(
					"Operaci�n Realizada con Exito", "Informaci�n"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			setBanderaSucces(false);
		}
		// TODO: cambiar por acceso a la base de datos
		return "muestraPerfiles";
	}
	
	public String regresarMuestraPerfiles() {
		this.setPerfilEdit(null);
		this.perfilesDisponibles = perfilesDAO.obtenerPerfiles();
		// TODO: cambiar por acceso a la base de datos
		return "muestraPerfiles";
	}
	public String agregarPerfil() {
		this.perfilEdit= new Perfil();
		return "editPerfil";
	}
}
