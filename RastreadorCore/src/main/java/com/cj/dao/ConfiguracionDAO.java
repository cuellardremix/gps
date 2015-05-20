package com.cj.dao;

import org.hibernate.Query;
import org.hibernate.Session;

import com.cj.configuracion.HibernateUtil;
import com.cj.pojos.Configuracion;

public class ConfiguracionDAO {
	public Configuracion obtenerRetraso() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from Configuracion c");
		query.setMaxResults(1);
		Configuracion c =(Configuracion) query.uniqueResult();
		session.close();
		return c;
	}
	
	public void guardarRetraso(Configuracion c){
		Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
		session.saveOrUpdate(c);
		session.getTransaction().commit();
		session.close();
	}
}
