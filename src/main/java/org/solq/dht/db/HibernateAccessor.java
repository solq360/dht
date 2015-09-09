package org.solq.dht.db;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * {@link Accessor} 的 Hibernate 实现
 * 
 * @author frank
 */
public class HibernateAccessor extends HibernateDaoSupport implements Accessor {

	// private Map<String, EntityMetadata> entityMetadataMap = new
	// HashMap<String, EntityMetadata>();

	@PostConstruct
	public void postConstruct() {
		Map<String, ClassMetadata> classMetadataMap = this.getSessionFactory().getAllClassMetadata();
		for (ClassMetadata classMetadata : classMetadataMap.values()) {
			// entityMetadataMap.put(classMetadata.getEntityName(), new
			// HibernateMetadata(classMetadata));
		}
	}

	@Override
	public <PK, T extends IEntity<PK>> T load(Class<T> clz, PK id) {
		return getHibernateTemplate().get(clz, (Serializable) id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <PK, T extends IEntity<PK>> PK save(Class<T> clz, T entity) {
		return (PK) getHibernateTemplate().save(entity);
	}

	@Override
	public <PK, T extends IEntity<PK>> void remove(Class<T> clz, PK id) {
		final Session session = this.getSession();
		try {
			final StringBuilder stringBuilder = new StringBuilder();
			final String name = clz.getSimpleName();
			final String primary = "id";
			final String table = "table";

			stringBuilder.append("DELETE ").append(name).append(" ").append(table.charAt(0));
			stringBuilder.append(" WHERE ");
			stringBuilder.append(table.charAt(0)).append(".").append(primary).append("=:").append(primary);

			final Query query = session.createQuery(stringBuilder.toString());
			query.setParameter(primary, id);
			query.executeUpdate();
		} finally {
			session.close();
		}
	}

	@Override
	public <PK, T extends IEntity<PK>> void remove(Class<T> clz, T entity) {
		getHibernateTemplate().delete(entity);
	}

	@Override
	public <PK, T extends IEntity<PK>> T update(Class<T> clz, T entity) {
		getHibernateTemplate().update(entity);
		return entity;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <PK, T extends IEntity<PK>> void listAll(Class<T> clz, Collection<T> entities, Integer offset,
			Integer size) {
		final Session session = this.getSession();
		try {
			final Criteria entityCriteria = session.createCriteria(clz);
			if (offset != null && size != null) {
				entityCriteria.setFirstResult(offset);
				entityCriteria.setMaxResults(size);
			}
			entities.addAll(entityCriteria.list());
		} finally {
			session.close();
		}

	}
 
	/**
	 * 获取实体记录总数(具体实现扩展方法)
	 * 
	 * @param clz
	 * @return
	 */
	public <PK, T extends IEntity<PK>> long countAll(Class<T> clz) {
		final Session session = this.getSession();
		try {
			final Criteria countCriteria = session.createCriteria(clz);
			countCriteria.setProjection(Projections.rowCount());
			final Object object = countCriteria.uniqueResult();
			return Long.class.cast(object);
		} finally {
			session.close();
		}
	}
 
  
 

	@Override
	public <PK, T extends IEntity<PK>> T load(String table, Class<T> clazz, PK id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <PK, T extends IEntity<PK>> PK save(String table, Class<T> clazz, T entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <PK, T extends IEntity<PK>> void remove(String table, Class<T> clazz, PK id) {
		// TODO Auto-generated method stub

	}

	@Override
	public <PK, T extends IEntity<PK>> void remove(String table, Class<T> clazz, T entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public <PK, T extends IEntity<PK>> T update(String table, Class<T> clazz, T entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <PK, T extends IEntity<PK>> void listAll(String table, Class<T> clazz, Collection<T> entities,
			Integer offset, Integer size) {
		// TODO Auto-generated method stub

	}

}
