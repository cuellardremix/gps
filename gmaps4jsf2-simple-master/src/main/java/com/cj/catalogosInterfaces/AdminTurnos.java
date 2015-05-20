package com.cj.catalogosInterfaces;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.cj.ReportesDAO.ReportesTurnosDAO;
import com.cj.catalogosDAO.AdminTurnosDAO;
import com.cj.dao.InterfazDAO;
import com.cj.pojos.Turno;
import com.cj.utils.Constantes;

public class AdminTurnos {

	private InterfazDAO interfazDAO=new InterfazDAO();
	
	private AdminTurnosDAO turnosDAO=new AdminTurnosDAO();
	private Turno buscar=new Turno();
	private ReportesTurnosDAO repo=new ReportesTurnosDAO();
	private List<Turno> turnosDisponibles;//=usuariosDAO.obtenerUsuarios();
	private Turno turnoEdit;
	private Boolean banderaSucces=true;
	
	public AdminTurnos(){
		turnosDisponibles=turnosDAO.obtenerTurnos();
	}
	
	public Turno getBuscar() {
		return buscar;
	}
	public void setBuscar(Turno buscar) {
		this.buscar = buscar;
	}
	public ReportesTurnosDAO getRepo() {
		return repo;
	}
	public void setRepo(ReportesTurnosDAO repo) {
		this.repo = repo;
	}
	public List<Turno> getTurnosDisponibles() {
		return turnosDisponibles;
	}
	public void setTurnosDisponibles(List<Turno> turnosDisponibles) {
		this.turnosDisponibles = turnosDisponibles;
	}
	public Turno getTurnoEdit() {
		return turnoEdit;
	}
	public void setTurnoEdit(Turno turnoEdit) {
		this.turnoEdit = turnoEdit;
	}
	public Boolean getBanderaSucces() {
		return banderaSucces;
	}
	public void setBanderaSucces(Boolean banderaSucces) {
		this.banderaSucces = banderaSucces;
	}
	
	
	public String editTurno(Turno turno){
		setTurnoEdit(turno);
		return "editTurno";
	}
	
	public String generaReporte(){
		if(this.turnosDisponibles!=null){
		if(!this.turnosDisponibles.isEmpty()){
		repo.crearReporteTurnos(this.turnosDisponibles);
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
		if((buscar.getTurLla()==null || buscar.getTurLla()==0) && 
				(buscar.getTurFAl()==null || buscar.getTurFAl().equals("")) && 
				(buscar.getTurFBa()==null || buscar.getTurFBa().equals("")) && 
				(buscar.getTurDes()==null || buscar.getTurDes().equals("")) && 
				(buscar.getTurLun()==null || buscar.getTurLun().equals("")) && 
				(buscar.getTurMar()==null || buscar.getTurMar().equals("")) && 
				(buscar.getTurMie()==null || buscar.getTurMie().equals("")) && 
				(buscar.getTurJue()==null || buscar.getTurJue().equals("")) && 
				(buscar.getTurVie()==null || buscar.getTurVie().equals("")) && 
				(buscar.getTurSab()==null || buscar.getTurSab().equals("")) && 
				(buscar.getTurDom()==null || buscar.getTurDom().equals("")) && 
				(buscar.getTurHI1()==null || buscar.getTurHI1().equals("")) && 
				(buscar.getTruHF1()==null || buscar.getTruHF1().equals(""))){
			FacesContext context = FacesContext.getCurrentInstance();
	        context.addMessage(null, new FacesMessage(Constantes.noParametrosBusqueda,"Información"  ) );	
		}else{
			this.turnosDisponibles=turnosDAO.busqueda(turnosDAO.transToquens(buscar));
			FacesContext context = FacesContext.getCurrentInstance();
	        context.addMessage(null, new FacesMessage(Constantes.exito,"Información"  ) );
	        context.getPartialViewContext().getRenderIds().add("clientes");
		}
		return "";
	}
	
	public String deleteTurno(Turno turno){
		try{

		this.turnosDAO.deleteTurno(turno);
//		turnosDisponibles=turnosDAO.obtenerTurnos();
		FacesContext context = FacesContext.getCurrentInstance();
        
        context.addMessage(null, new FacesMessage("Operación Realizada con Exito","Información"  ) );
        context.getPartialViewContext().getRenderIds().add("turnos");
        turnosDisponibles=turnosDAO.obtenerTurnos();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			setBanderaSucces(false);
		}
		return null;
	}
	
	public String guardaTurnoEditado(){
		try{
			this.getTurnoEdit().setEstadoRegistro(true);
		this.turnosDAO.updateTurno(this.getTurnoEdit());
		setBanderaSucces(true);
//		turnosDisponibles=turnosDAO.obtenerTurnos();
		FacesContext context = FacesContext.getCurrentInstance();
        
        context.addMessage(null, new FacesMessage("Operación Realizada con Exito","Información"  ) );
		this.turnosDisponibles=turnosDAO.obtenerTurnos();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			setBanderaSucces(false);
		}
		//TODO: cambiar por acceso a la base de datos
		return "muestraTurnos";
	}
	
	public String regresarMuestraTurnos(){
		this.setTurnoEdit(null);
		this.turnosDisponibles=turnosDAO.obtenerTurnos();
		//TODO: cambiar por acceso a la base de datos
		return "muestraTurnos";
	}
	
	public String agregarTurno(){
		this.turnoEdit=new Turno();
		return "editTurno";
	}
	
	
}
