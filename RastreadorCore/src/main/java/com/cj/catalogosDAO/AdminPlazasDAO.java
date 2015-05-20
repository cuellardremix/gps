package com.cj.catalogosDAO;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.cj.configuracion.HibernateUtil;
import com.cj.pojos.Cliente;
import com.cj.pojos.Perfil;
import com.cj.pojos.Plaza;
import com.cj.pojos.Turno;

public class AdminPlazasDAO {

	public List<Plaza> obtenerPlazas() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from Plaza p where p.estadoRegistro=true order by p.id");
		List<Plaza> v=query.list();
		session.close();
		return v;
	}

	public Plaza obtenerPlazaXId(Integer idPlaza) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from Plaza p where p.plaLla=:idPlaza and p.estadoRegistro=true order by p.id");
		query.setParameter("idPlaza", idPlaza);
		Plaza p=(Plaza)query.uniqueResult();
		session.close();
		return p;
	}
	
	public void deletePlaza(Plaza plaza) {
		plaza.setEstadoRegistro(false);
		updatePlaza(plaza);
		
	}

	public void updatePlaza(Plaza plazaEdit) {
		Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
			session.saveOrUpdate(plazaEdit);
		session.getTransaction().commit();
		session.close();
	}

	public Plaza transToquens(Plaza buscar) {
		
		buscar.setPlaTip('*');
		buscar.setPlaDes(buscar.getPlaDes().replace("*","%"));
		if(buscar.getPlaDes()==null){
			buscar.setPlaDes("");
		}
		buscar.setPlaDir(buscar.getPlaDir().replace("*","%"));
		if(buscar.getPlaDir()==null){
			buscar.setPlaDir("");
		}
		buscar.setPlaCol(buscar.getPlaCol().replace("*","%"));
		if(buscar.getPlaCol()==null)
			buscar.setPlaCol("");
		buscar.setPlaCiu(buscar.getPlaCiu().replace("*","%"));
		if(buscar.getPlaCiu()==null)
			buscar.setPlaCiu("");
		buscar.setPlaEst(buscar.getPlaEst().replace("*","%"));
		if(buscar.getPlaEst()==null)
			buscar.setPlaEst("");
		buscar.setPlaNoC(buscar.getPlaNoC().replace("*","%"));
		if(buscar.getPlaNoC()==null)
			buscar.setPlaNoC("");
		buscar.setPlatC1(buscar.getPlatC1().replace("*","%"));
		if(buscar.getPlatC1()==null)
			buscar.setPlatC1("");
		buscar.setPlatC2(buscar.getPlatC2().replace("*","%"));
		if(buscar.getPlatC2()==null)
			buscar.setPlatC2("");
		return buscar;
	}

	public List<Plaza> busqueda(Plaza p) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from Plaza p where " +
				"p.estadoRegistro=true AND " +				
				"(p.plaLla = :plaLla OR "+
//				" (day(p.plaFAl) =day(:plaFAl) AND month(p.plaFAl)=month(:plaFAl)) OR "+
//				" (day(p.plaFBa) =day(:plaFBa) AND month(p.plaFBa)=month(:plaFBa)) OR "+
				"p.plaTip = :plaTip OR "+
				"p.plaDes like :plaDes OR "+
				"p.plaDir like :plaDir OR "+
				"p.plaCol like :plaCol OR "+
				"p.plaCop = :plaCop OR "+
				"p.plaCiu like :plaCiu OR "+
				"p.plaEst like :plaEst OR "+
				"p.plaNoC like :plaNoC OR "+
				"p.platC1 like :platC1 OR "+
				"p.platC2 like :platC2 ) "+
				"order by p.id");
	query.setParameter("plaLla", p.getPlaLla());
	query.setParameter("plaTip", p.getPlaTip());
//	query.setDate("plaFAl", p.getPlaFAl());
//	query.setDate("plaFBa", p.getPlaFBa());
	query.setParameter("plaDes", p.getPlaDes());
	query.setParameter("plaDir", p.getPlaDir());
	query.setParameter("plaCol", p.getPlaCol());
	query.setParameter("plaCop", p.getPlaCop());
	query.setParameter("plaCiu", p.getPlaCiu());
	query.setParameter("plaEst", p.getPlaEst());
	query.setParameter("plaNoC", p.getPlaNoC());
	query.setParameter("platC1", p.getPlatC1());
	query.setParameter("platC2", p.getPlatC2());
	
		List<Plaza> v=query.list();
		session.close();
		return v;
	}

	public HashMap<String,Integer> obtenerPlazasString(){
		HashMap<String, Integer> nombres=new HashMap<String,Integer>();
		List<Plaza> plazas=this.obtenerPlazas();
		for(Plaza p:plazas){
			nombres.put(p.getPlaDes(), p.getPlaLla());
		}
		return nombres;
	}
	
}
