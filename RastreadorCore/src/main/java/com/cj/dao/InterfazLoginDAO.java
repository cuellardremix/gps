package com.cj.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.cj.configuracion.HibernateUtil;
import com.cj.pojos.Menu;
import com.cj.pojos.Usuario;

public class InterfazLoginDAO {

	public Usuario leerContraseña(String usuario){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from Usuario u WHERE u.usuNom=:nombreUsuario AND u.estadoRegistro=true");
		query.setParameter("nombreUsuario", usuario);
		query.setMaxResults(1);
		Usuario u=(Usuario)query.uniqueResult();
		session.close();
		return u;
	}
	public List<Menu> obtenerMenus(Usuario u) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("Select m from Menu m JOIN m.perfiles ps WHERE ps.id=:idPerfil AND m.estadoRegistro=true order by idMenu");
		query.setParameter("idPerfil", u.getPerfil().getPerLla());
//		query.setMaxResults(5);
		List<Menu> l=query.list();
		session.close();
		return l;
	}

}
