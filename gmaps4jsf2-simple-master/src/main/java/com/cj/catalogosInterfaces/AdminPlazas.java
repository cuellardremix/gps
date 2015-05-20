package com.cj.catalogosInterfaces;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.cj.ReportesDAO.ReportesPlazasDAO;
import com.cj.catalogosDAO.AdminPlazasDAO;
import com.cj.dao.InterfazDAO;
import com.cj.pojos.Plaza;
import com.cj.utils.Constantes;

public class AdminPlazas {
	private InterfazDAO interfazDAO = new InterfazDAO();
	private AdminPlazasDAO plazasDAO=new AdminPlazasDAO();
	private ReportesPlazasDAO repo=new ReportesPlazasDAO();
	private Plaza buscar=new Plaza();
	private List<Plaza> plazasDisponibles; //= plazasDAO.obtenerPlazas();
	private Plaza plazaEdit;
	private Boolean banderaSucces = true;
	
	public AdminPlazas(){
		plazasDisponibles= plazasDAO.obtenerPlazas();
	}
	public Plaza getBuscar() {
		return buscar;
	}
	public void setBuscar(Plaza buscar) {
		this.buscar = buscar;
	}
	public List<Plaza> getPlazasDisponibles() {
		return plazasDisponibles;
	}
	public void setPlazasDisponibles(List<Plaza> plazasDisponibles) {
		this.plazasDisponibles = plazasDisponibles;
	}
	public Plaza getPlazaEdit() {
		return plazaEdit;
	}
	public void setPlazaEdit(Plaza plazaEdit) {
		this.plazaEdit = plazaEdit;
	}
	public Boolean getBanderaSucces() {
		return banderaSucces;
	}
	public void setBanderaSucces(Boolean banderaSucces) {
		this.banderaSucces = banderaSucces;
	}
	
	
	public String generaReporte(){
		if(this.plazasDisponibles!=null){
		if(!this.plazasDisponibles.isEmpty()){
		repo.crearReportePlazas(this.plazasDisponibles);
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
	
	public String buscarP(){
		if(		( buscar.getPlaLla()==null || buscar.getPlaLla()==0 ) &&
				( buscar.getPlaFAl()==null || buscar.getPlaFAl().equals("") ) &&
				( buscar.getPlaFBa()==null || buscar.getPlaFBa().equals("") ) &&
				( buscar.getPlaTip()==null || buscar.getPlaTip().equals("") ) &&
				( buscar.getPlaDes()==null || buscar.getPlaDes().equals("") ) &&
				( buscar.getPlaDir()==null || buscar.getPlaDir().equals("") ) &&
				( buscar.getPlaCol()==null || buscar.getPlaCol().equals("") ) &&
				( buscar.getPlaCop()==null || buscar.getPlaCop()==0) &&
				( buscar.getPlaCiu()==null || buscar.getPlaCiu().equals("") ) &&
				( buscar.getPlaEst()==null || buscar.getPlaEst().equals("") ) &&
				( buscar.getPlaNoC()==null || buscar.getPlaNoC().equals("") ) &&
				( buscar.getPlatC1()==null || buscar.getPlatC1().equals("") ) &&
				( buscar.getPlatC2()==null || buscar.getPlatC2().equals("") ) ){
			FacesContext context = FacesContext.getCurrentInstance();
	        context.addMessage(null, new FacesMessage(Constantes.noParametrosBusqueda,"Información"  ) );	
		}else{
			this.plazasDisponibles=plazasDAO.busqueda(plazasDAO.transToquens(buscar));
			FacesContext context = FacesContext.getCurrentInstance();
	        context.addMessage(null, new FacesMessage(Constantes.exito,"Información"  ) );
	        context.getPartialViewContext().getRenderIds().add("perfiles");
		}
		return "";
	}
	
	public String deletePlaza(Plaza plaza) {
		try {

			this.plazasDAO.deletePlaza(plaza);
//			plazasDisponibles = plazasDAO.obtenerPlazas();
			FacesContext context = FacesContext.getCurrentInstance();

			context.addMessage(null, new FacesMessage(
					"Operación Realizada con Exito", "Información"));
			context.getPartialViewContext().getRenderIds().add("plazas");
			plazasDisponibles= plazasDAO.obtenerPlazas();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			setBanderaSucces(false);
		}
		return null;
	}
	
	public String guardaPlazaEditado() {
		try {
			this.getPlazaEdit().setEstadoRegistro(true);
			this.plazasDAO.updatePlaza(this.getPlazaEdit());
			setBanderaSucces(true);
//			this.plazasDisponibles = plazasDAO.obtenerPlazas();
			FacesContext context = FacesContext.getCurrentInstance();

			context.addMessage(null, new FacesMessage(
					"Operación Realizada con Exito", "Información"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			setBanderaSucces(false);
		}
		// TODO: cambiar por acceso a la base de datos
		return "muestraPlazas";
	}
	
	public String regresarMuestraPlazas() {
		this.setPlazaEdit(null);
		this.plazasDisponibles = plazasDAO.obtenerPlazas();
		// TODO: cambiar por acceso a la base de datos
		return "muestraPlazas";
	}
	public String agregarPlaza() {
		this.plazaEdit = new Plaza();
		return "editPlaza";
	}
}
