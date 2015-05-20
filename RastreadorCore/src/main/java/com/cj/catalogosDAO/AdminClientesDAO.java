package com.cj.catalogosDAO;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.cj.configuracion.HibernateUtil;
import com.cj.pojos.Cliente;
import com.cj.pojos.Perfil;
import com.cj.pojos.Usuario;

public class AdminClientesDAO {

	
	public HashMap<String,Integer> obtenerClientesString(){
		HashMap<String, Integer> nombres=new HashMap<String,Integer>();
		List<Cliente> ps=obtenerClientes();
			nombres.put("", null);
		for(Cliente p:ps){
			nombres.put(p.getCliNom(), p.getCliLla());
		}
		return nombres;
	}
	
	
	public List<Cliente> obtenerClientes() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from Cliente where estadoRegistro=true order by id");
		List<Cliente> v=query.list();
		session.close();
		return v;
	}

	public Cliente obtenerClienteXId(Integer idCliente) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from Cliente where cliLla=:idCliente order by id");
		query.setParameter("idCliente", idCliente);
		Cliente v=(Cliente)query.uniqueResult();
		session.close();
		return v;
	}
	
	public void deleteCliente(Cliente cliente) {

		cliente.setEstadoRegistro(false);
		updateCliente(cliente);

		
	}

	public void updateCliente(Cliente clienteEdit) {
		Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
			session.saveOrUpdate(clienteEdit);
		session.getTransaction().commit();
		session.close();
		
	}

	public Cliente transToquens(Cliente c) {
		c.setCliNom(c.getCliNom().replace('*','%'));
		c.setCliDom(c.getCliDom().replace('*','%'));
		c.setCliCol(c.getCliCol().replace('*','%'));
		c.setCliCiu(c.getCliCiu().replace('*','%'));
		c.setCliEdo(c.getCliEdo().replace('*','%'));
		c.setCliPai(c.getCliPai().replace('*','%'));
		c.setCliCo1(c.getCliCo1().replace('*','%'));
		c.setCliT11(c.getCliT11().replace('*','%'));
		c.setCliT21(c.getCliT21().replace('*','%'));
		c.setCliC11(c.getCliC11().replace('*','%'));
		c.setCliC21(c.getCliC21().replace('*','%'));
		c.setCliCo2(c.getCliCo2().replace('*','%'));
		c.setCliT12(c.getCliT12().replace('*','%'));
		c.setCliT22(c.getCliT22().replace('*','%'));
		c.setCliC12(c.getCliC12().replace('*','%'));
		c.setCliC22(c.getCliC22().replace('*','%'));
		return c;
	}

	public List<Cliente> busqueda(Cliente c) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("from Cliente c where " +
				"c.estadoRegistro=true AND" +
				"	(c.cliLla = :cliLla OR 	"	+
				"	c.cliFAl =:cliFAl OR 	"	+
				"	c.clifBa =:clifBa OR 	"	+
				"	c.cliNom like 	:cliNom OR 	"	+
				"	c.cliDom like 	:cliDom OR 	"	+
				"	c.cliCol like 	:cliCol OR 	"	+
				"	c.cliCOP = :cliCOP OR 	"	+
				"	c.cliCiu like 	:cliCiu OR 	"	+
				"	c.cliEdo like 	:cliEdo OR 	"	+
				"	c.cliPai like 	:cliPai OR 	"	+
				
				"	c.cliCo1 like 	:cliCo1 OR 	"	+
				"	c.cliT11 like 	:cliT11 OR 	"	+
				"	c.cliT21 like 	:cliT21 OR 	"	+
				"	c.cliC11 like 	:cliC11 OR 	"	+
				"	c.cliC21 like 	:cliC21 OR 	"	+
				"	c.cliCo2 like 	:cliCo2 OR 	"	+
				"	c.cliT12 like 	:cliT12 OR 	"	+
				"	c.cliT22 like 	:cliT22 OR 	"	+
				"	c.cliC12 like 	:cliC12 OR 	"	+
				"	c.cliC22 like 	:cliC22) "	+
				"order by c.id");
		query.setParameter("cliLla", c.getCliLla());
		query.setParameter("cliFAl", c.getCliFAl());
		query.setParameter("clifBa", c.getClifBa());
		query.setParameter("cliNom", c.getCliNom());
		query.setParameter("cliDom", c.getCliDom());
		query.setParameter("cliCol", c.getCliCol());
		query.setParameter("cliCOP", c.getCliCOP());
		query.setParameter("cliCiu", c.getCliCiu());
		query.setParameter("cliEdo", c.getCliEdo());
		query.setParameter("cliPai", c.getCliPai());
		
		query.setParameter("cliCo1", c.getCliCo1());
		query.setParameter("cliT11", c.getCliT11());
		query.setParameter("cliT21", c.getCliT21());
		query.setParameter("cliC11", c.getCliC11());
		query.setParameter("cliC21", c.getCliC21());
		query.setParameter("cliCo2", c.getCliCo2());
		query.setParameter("cliT12", c.getCliT12());	
		query.setParameter("cliT22", c.getCliT22());
		query.setParameter("cliC12", c.getCliC12());
		query.setParameter("cliC22", c.getCliC22());
		List<Cliente> v=query.list();
		session.close();
		return v;
	}

}
