package org.solq.dht.db.model;

import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.solq.dht.db.HibernateTemplate;

public class DbMetadata {

	private String dbName;
	private HibernateTemplate hibernateTemplate;
	private SessionFactory sessionFactory;
	private Properties hibernateProperties;
	private BasicDataSource dataSource;

	public static DbMetadata of(String dbName, BasicDataSource dataSource, Properties hibernateProperties,
			SessionFactory sessionFactory, HibernateTemplate hibernateTemplate) {
		DbMetadata result = new DbMetadata();
		result.dbName = dbName;
		result.dataSource = dataSource;
		result.hibernateProperties = hibernateProperties;
		result.sessionFactory = sessionFactory;
		result.hibernateTemplate = hibernateTemplate;
		return result;
	}
	// getter

	public String getDbName() {
		return dbName;
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public Properties getHibernateProperties() {
		return hibernateProperties;
	}

	public BasicDataSource getDataSource() {
		return dataSource;
	}

}
