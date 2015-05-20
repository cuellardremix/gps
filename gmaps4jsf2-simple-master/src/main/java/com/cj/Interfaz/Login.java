package com.cj.Interfaz;

import java.io.IOException;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.cj.dao.InterfazLoginDAO;
import com.cj.pojos.Menu;
import com.cj.pojos.Usuario;
import com.cj.utils.Constantes;

public class Login {
	private String usuario;
	private String contraseña;
	private Boolean valid=false;
	private List<Menu> menus;
	private InterfazLoginDAO loginDAO=new InterfazLoginDAO();
	private Usuario eUsuario;
	private Integer idCliente;
	private final FacesContext faceContext;
	private FacesMessage facesMessage;
	private final HttpServletRequest httpServletRequest;
	
	public Login(){
		faceContext=FacesContext.getCurrentInstance();
        httpServletRequest=(HttpServletRequest)faceContext.getExternalContext().getRequest();
	}
	
	
	
	public Integer getIdCliente() {
		return idCliente;
	}
	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}
	public Usuario geteUsuario() {
		return eUsuario;
	}
	public void seteUsuario(Usuario eUsuario) {
		this.eUsuario = eUsuario;
	}
	public List<Menu> getMenus() {
		return menus;
	}
	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}
	public Boolean getValid() {
		return valid;
	}
	public void setValid(Boolean valid) {
		this.valid = valid;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getContraseña() {
		return contraseña;
	}
	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}
	
	

	
	public String ingresar(){
		Usuario tIngresar=new Usuario(this.usuario,this.contraseña);
		eUsuario=loginDAO.leerContraseña(tIngresar.getUsuNom());
		if(eUsuario!=null)
		if(tIngresar.getUsuCon().equals(eUsuario.getUsuCon())){
//			httpServletRequest.getSession().setAttribute("sessionUsuario", this.usuario);
			try {
				if(eUsuario.getPerfil().getPerLla()==Constantes.perfilCliente){
					this.idCliente=eUsuario.getCliente().getCliLla();
				}
				this.menus=loginDAO.obtenerMenus(eUsuario);
				for(Menu menu:this.menus)
				System.out.println("logueado: "+menu.getPagina());
				return "main?faces-redirect=true";
			} catch (	Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			valid=true;
			facesMessage=new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario o contraseña incorrecto", null);
            faceContext.addMessage(null, facesMessage);
		}
		return "realizado";
	}
	
	
//	public MbSession() 
//    {
//        faceContext=FacesContext.getCurrentInstance();
//        httpServletRequest=(HttpServletRequest)faceContext.getExternalContext().getRequest();
//        if(httpServletRequest.getSession().getAttribute("sessionUsuario")!=null)
//        {
//            usuario=httpServletRequest.getSession().getAttribute("sessionUsuario").toString();
//        }
//    }
     
    public String cerrarSession()
    {
//        httpServletRequest.getSession().removeAttribute("sessionUsuario");
//        facesMessage=new FacesMessage(FacesMessage.SEVERITY_INFO, "Session cerrada correctamente", null);
//        faceContext.addMessage(null, facesMessage);
//        return "index";
    	
    	FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/index.xhtml?faces-redirect=true";
    }
	
	
	
	
}
