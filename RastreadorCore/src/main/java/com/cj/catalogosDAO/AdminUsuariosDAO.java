package com.cj.catalogosDAO;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.cj.configuracion.HibernateUtil;
import com.cj.pojos.Usuario;
import com.cj.pojos.Vehiculo;

public class AdminUsuariosDAO {

	public List<Usuario> obtenerUsuarios() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from Usuario where estadoRegistro=true order by id");
		List<Usuario> v=query.list();
		session.close();
		return v;
	}

	public void deleteUsuario(Usuario usuario){

		usuario.setEstadoRegistro(false);
		updateUsuario(usuario);
		
	}
	
	public void updateUsuario(Usuario usuarioEdit) {
		Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
			session.saveOrUpdate(usuarioEdit);
		session.getTransaction().commit();
		session.close();
	}

	public Usuario transToquens(Usuario u) {
		u.setUsuNom(u.getUsuNom().replace('*','%'));
//		u.setUsuCon(u.getUsuCon().replace('*','%'));
		return u;
	}

	public List<Usuario> busqueda(Usuario u) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from Usuario u where " +
				"(u.usuLla =  :usuLla OR "+
				"(DAY(u.usuFal)=DAY(:usuFal) AND MONTH(u.usuFal)=MONTH(:usuFal)) OR "+
				"(DAY(u.usuFBa)=DAY(:usuFBa) AND MONTH(u.usuFBa)=MONTH(:usuFBa)) OR "+
				"u.usuNom like  :usuNom OR "+
				"u.usuCon like  :usuCon OR " +
				"u.perfil.id=:idPerfil) AND "+
				"u.estadoRegistro=true order by u.id");
		query.setParameter("usuLla", u.getUsuLla());
		query.setDate("usuFal", u.getUsuFal());
		query.setDate("usuFBa", u.getUsuFBa());
		query.setParameter("usuNom", u.getUsuNom());
		query.setParameter("usuCon", u.getUsuCon());
		query.setParameter("idPerfil", u.getPerfil().getPerLla());
		List<Usuario> v=query.list();
		session.close();
		return v;
	}

}
