package org.solq.dht.db;

import java.io.Serializable;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class HibernateTemplate extends HibernateDaoSupport {

	public HibernateTemplate(SessionFactory sessionFactory) {
		this.setSessionFactory(sessionFactory);
	}

	public void saveOrUpdate(Object entity) {
		this.getHibernateTemplate().saveOrUpdate(entity);
	}

	public <T> T get(Class<T> entityClass, Serializable id) {

		return this.getHibernateTemplate().get(entityClass, id);
	}

	public Session getCurrentSession() {
		return this.getSession();
	}
}
