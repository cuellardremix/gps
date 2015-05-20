package com.cj.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.hibernate.Query;
import org.hibernate.Session;

import com.cj.configuracion.HibernateUtil;
import com.cj.pojos.Usuario;
import com.cj.pojos.Vehiculo;
import com.cj.utils.Constantes;

public class InterfazAdminVehicDAO {
	
	public InterfazAdminVehicDAO(){}
	
	public void updateVehiculo(Vehiculo vehiculo){
		Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
		vehiculo.setVehComAct(Constantes.leerPosicion);
			session.saveOrUpdate(vehiculo);
		session.getTransaction().commit();
		session.close();
	}
	
    public Vehiculo transToquens(Vehiculo v){
    	v.setVehDes(v.getVehDes().replace('*', '%'));
    	v.setVehMar(v.getVehMar().replace('*', '%'));
    	v.setVehTip(v.getVehTip().replace('*', '%'));
    	v.setVehMod(v.getVehMod().replace('*', '%'));
    	v.setVehTar(v.getVehTar().replace('*', '%'));
    	v.setVehCap(v.getVehCap().replace('*', '%'));
    	v.setVehCol(v.getVehCol().replace('*', '%'));
    	v.setVehImei(v.getVehImei().replace('*', '%'));
    	v.setVehNoCel(v.getVehNoCel().replace('*','%'));
    	return v;
    }
    
	public List<Vehiculo> busqueda(Vehiculo busqueda){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from Vehiculo v where " +
				"v.estadoRegistro=true AND " +
				"(v.vehFAl=:vehFAl OR " +
				"v.vehFBa=:vehFBa OR " +
				"v.vehLla = :vehLla OR " +
				"v.vehDes like :vehDes OR " +
				"v.vehMar like :vehMar OR " +
				"v.vehTip like :vehTip OR " +
				"v.vehMod like :vehMod OR " +
				"v.vehTar like :vehTar OR " +
				"v.vehCap = :vehCap OR " +
				"v.vehCol like :vehCol OR " +
				"v.vehVeM = :vehVeM OR " +
				"v.vehEsD = :vehEsD OR " +
				"v.vehImei like :vehImei OR " +
				"v.vehNoCel like :vehNoCel OR " +
				"v.vehPuerto = :vehPuerto) " +
				
//				"v.vehRu.id= :idRuta) " +
				"order by v.id");
//		query.setParameter("idRuta", busqueda.getVehRu().getRutLla());
		query.setParameter("vehFAl", busqueda.getVehFAl());
		query.setParameter("vehLla", busqueda.getVehLla());
		query.setParameter("vehFBa", busqueda.getVehFBa());
		query.setParameter("vehDes", busqueda.getVehDes());
		query.setParameter("vehMar", busqueda.getVehMar());
		query.setParameter("vehTip", busqueda.getVehTip());
		query.setParameter("vehMod", busqueda.getVehMod());
		query.setParameter("vehTar", busqueda.getVehTar());
		query.setParameter("vehCap", busqueda.getVehCap());
		query.setParameter("vehCol", busqueda.getVehCol());
		query.setParameter("vehVeM", busqueda.getVehVeM());
		query.setParameter("vehEsD", busqueda.getVehEsD());
		query.setParameter("vehImei", busqueda.getVehImei());
		query.setParameter("vehNoCel", busqueda.getVehNoCel());
		query.setParameter("vehPuerto", busqueda.getVehPuerto());
		List<Vehiculo> v=query.list();
		session.close();
		return v;
	}
	public void deleteVehiculo(Vehiculo vehiculo){
		Session session = HibernateUtil.getSessionFactory().openSession();
		vehiculo.setEstadoRegistro(false);
		session.beginTransaction();
		session.saveOrUpdate(vehiculo);
		session.getTransaction().commit();
		session.close();
	}
	
	protected void refreshPage() {
		FacesContext fc = FacesContext.getCurrentInstance();
		String refreshpage = fc.getViewRoot().getViewId();
		ViewHandler ViewH =fc.getApplication().getViewHandler();
		UIViewRoot UIV = ViewH.createView(fc,refreshpage);
		UIV.setViewId(refreshpage);
		fc.setViewRoot(UIV);
		}
	
	public HashMap<String, Integer> obtenerVehiculosString(Usuario usuario) {
		HashMap<String, Integer> nombres=new HashMap<String,Integer>();
		List<Vehiculo> vs=this.obtenerVehiculos(usuario);
		nombres.put("", null);
		for(Vehiculo v:vs){
			nombres.put(v.getVehMod()+" "+v.getVehMar(), v.getVehLla());
		}
		return nombres;
	}
	
	public HashMap<String, String> obtenerVehiculosSS(Usuario usuario) {
		HashMap<String, String> nombres=new HashMap<String,String>();
		List<Vehiculo> vs=this.obtenerVehiculos(usuario);
		nombres.put("", null);
		for(Vehiculo v:vs){
			nombres.put(v.getVehMod()+" "+v.getVehMar(), v.getVehLla().toString());
		}
		return nombres;
	}
	
	public List<Vehiculo> obtenerVehiculos(Usuario usuario){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from Vehiculo v where v.usuario.id=:idUsuario AND v.estadoRegistro=true order by v.id");
		query.setParameter("idUsuario", usuario.getUsuLla());
		List<Vehiculo> v=query.list();
		session.close();
		List<Vehiculo>  vs;
		if(usuario.getPerfil().getPerLla()==Constantes.perfilCliente){
			vs=usuario.getCliente().getVehiculos();
			
		}else{
			vs=v;
		}
		return vs;
	}
	public List<Vehiculo> obtenerVehiculosList(List<Integer> idVehiculos){
		List<Vehiculo> vs=new ArrayList<Vehiculo>();
		for(Integer idVehiculo:idVehiculos){
			Vehiculo v=this.obtenerVehiculoXId(idVehiculo);
			vs.add(v);
		}
		return vs;
	}
	
	public List<Vehiculo> obtenerVehiculosListString(List<String> idVehiculos){
		List<Integer> ts=new ArrayList<Integer>();
		for(int i=0; i<idVehiculos.size(); i++){
			
			Integer t=Integer.parseInt(idVehiculos.get(i).toString());
			ts.add(t);
		}
		List<Vehiculo> vs=new ArrayList<Vehiculo>();
		for(Integer idVehiculo:ts){
			Vehiculo v=this.obtenerVehiculoXId(idVehiculo);
			vs.add(v);
		}
		return vs;
	}
	
	public Vehiculo obtenerVehiculoXId(Integer idVehiculo){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from Vehiculo v WHERE v.vehLla=:idVehiculo AND v.estadoRegistro=true");
		query.setParameter("idVehiculo", idVehiculo);
		Vehiculo v=(Vehiculo)query.uniqueResult();
		session.close();
		return v;
	}
	
}
