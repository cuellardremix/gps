package com.cj.catalogosDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.cj.configuracion.HibernateUtil;
import com.cj.pojos.Turno;

public class AdminTurnosDAO {

	public List<Turno> obtenerTurnoActual(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from Turno t where t.estadoRegistro=true AND " +
				"HOUR(t.turHI1)<=HOUR(CURRENT_DATE) AND HOUR(t.truHF1)>=HOUR(CURRENT_DATE)");
//		query.setDate("fechaActual", new Date());
		List<Turno> t=query.list();
		return t;
	}
	public Turno transToquens(Turno t) {
		t.setTurDes(t.getTurDes().replace('*', '%'));
		return t;
	}

	public List<Turno> busqueda(Turno t) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from Turno t where " +
				"( t.turLla=:turLla OR " +
				" (DAY(t.turFAl)=DAY(:turFAl) AND MONTH(t.turFAl)=MONTH(:turFAl)) OR " +
				" (DAY(t.turFBa)=DAY(:turFBa) AND MONTH(t.turFBa)=MONTH(:turFBa)) OR " +
				
				" t.turDes LIKE :turDes OR " +
				
				" t.turLun=:turLun OR " +
				" t.turMar=:turMar OR " +
				" t.turMie=:turMie OR " +
				" t.turJue=:turJue OR " +
				" t.turVie=:turVie OR " +
				" t.turSab=:turSab OR " +
				" t.turDom=:turDom OR " +
				
				" (DAY(t.turHI1)=DAY(:turHI1) AND MONTH(t.turHI1)=MONTH(:turHI1)) OR " +
				" (DAY(t.truHF1)=DAY(:truHF1) AND MONTH(t.truHF1) = MONTH(:truHF1))) AND "+

				"t.estadoRegistro=true order by t.id");
		query.setParameter("turLla", t.getTurLla());
		query.setDate("turFAl", t.getTurFAl());
		query.setDate("truHF1", t.getTruHF1());
		query.setDate("turFBa", t.getTurFBa());
		query.setParameter("turDes", t.getTurDes());
		query.setParameter("turLun", t.getTurLun());
		query.setParameter("turMar", t.getTurMar());
		query.setParameter("turMie", t.getTurMie());
		query.setParameter("turJue", t.getTurJue());
		query.setParameter("turVie", t.getTurVie());
		query.setParameter("turSab", t.getTurSab());
		query.setParameter("turDom", t.getTurDom());
		query.setDate("turHI1", t.getTurHI1());

		List<Turno> v=query.list();
		session.close();
		return v;
	}

	public void deleteTurno(Turno turno) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		turno.setEstadoRegistro(false);
        session.beginTransaction();
			session.saveOrUpdate(turno);
		session.getTransaction().commit();
		session.close();
		
	}

	public void updateTurno(Turno turno) {
		Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
			session.saveOrUpdate(turno);
		session.getTransaction().commit();
		session.close();
	}

	public HashMap<String,Integer> obtenerTurnosString(){
		HashMap<String, Integer> nombres=new HashMap<String,Integer>();
		List<Turno> turnos=this.obtenerTurnos();
		for(Turno t:turnos){
			nombres.put(t.getTurDes(), t.getTurLla());
		}
		return nombres;
	}

	public HashMap<String,String> obtenerTurnosSS(){
		HashMap<String, String> nombres=new HashMap<String,String>();
		List<Turno> turnos=this.obtenerTurnos();
		for(Turno t:turnos){
			nombres.put(t.getTurDes(), t.getTurLla().toString());
		}
		return nombres;
	}
	
	public List<Turno> obtenerTurnos(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from Turno t");
//		query.setDate("fechaActual", new Date());
		List<Turno> t=query.list();
		return t;
	}
	
	public List<Turno> obtenerTurnosXIds(List<Integer> ids){
		List<Turno> turnos=new ArrayList<Turno>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		for(Integer id:ids){
			Query query = session.createQuery("from Turno t where t.turLla=:idTurno");
			query.setParameter("idTurno", id);
//			query.setDate("fechaActual", new Date());
			Turno t=(Turno)query.uniqueResult();
			turnos.add(t);
		}
		return turnos;
	}
	
	public List<Turno> obtenerTurnosXIdsS(List<String> ids){
		List<Turno> turnos=new ArrayList<Turno>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		for(String id:ids){
			Query query = session.createQuery("from Turno t where t.turLla=:idTurno");
//			query.setDate("fechaActual", new Date());
			Integer idt=Integer.parseInt(id);
			query.setParameter("idTurno", idt);
			Turno t=(Turno)query.uniqueResult();
			turnos.add(t);
		}
		return turnos;
	}
	
}
