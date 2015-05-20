package com.cj.catalogosDAO;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.cj.configuracion.HibernateUtil;
import com.cj.pojos.Perfil;
import com.cj.pojos.Plaza;
import com.cj.pojos.Ruta;

public class AdminPerfilesDAO {

	public Perfil obtenerPerfilById(Integer id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from Perfil p where p.perLla=:idPerfil AND p.estadoRegistro=true order by p.id");
		query.setParameter("idPerfil", id);
		Perfil v=(Perfil) query.uniqueResult();
		session.close();
		return v;
	}
	
	public List<Perfil> obtenerPerfiles() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from Perfil p where p.estadoRegistro=true order by p.id");
		List<Perfil> v=query.list();
		session.close();
		return v;
	}
	
	public HashMap<String,Integer> obtenerPerfilesString(){
		HashMap<String, Integer> nombres=new HashMap<String,Integer>();
		List<Perfil> ps=obtenerPerfiles();
		nombres.put("", null);
		for(Perfil p:ps){
			nombres.put(p.getPerNom(), p.getPerLla());
		}
		return nombres;
	}
	

	public void deletePerfil(Perfil perfil) {
		perfil.setEstadoRegistro(false);
		updatePerfil(perfil);
		
	}

	public void updatePerfil(Perfil perfilEdit) {
		Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
			session.saveOrUpdate(perfilEdit);
		session.getTransaction().commit();
		session.close();
		
	}

	public Perfil transToquens(Perfil buscar) {
		buscar.setPerNom(buscar.getPerNom().replace('*','%'));
		buscar.setPerTip(buscar.getPerTip().replace('*','%'));
		return buscar;
	}

	public List<Perfil> busqueda(Perfil p) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from Perfil p where " +
				"p.estadoRegistro=true AND " +
				" (p.perLla=:perLla OR "+
				" (day(p.perFAl) =day(:perFAl) AND month(p.perFAl)=month(:perFAl)) OR "+
				" (day(p.perFBa) =day(:perFBa) AND month(p.perFBa)=month(:perFBa)) OR "+
				" p.perNom like :perNom OR "+
				" p.perTip like :perTip) "+
				"order by p.id");
	query.setParameter("perLla", p.getPerLla());
	query.setDate("perFAl", p.getPerFAl());
	query.setDate("perFBa", p.getPerFBa());
	query.setParameter("perNom", p.getPerNom());
	query.setParameter("perTip", p.getPerTip());
	
		List<Perfil> v=query.list();
		session.close();
		return v;
	}

}
