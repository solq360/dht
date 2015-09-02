package org.solq.dht.db;

import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;

public class DbUtils {

	public static SessionFactory buildSessionFactory(JdbcConfig config) throws Exception {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(config.getDriverClassName());
		dataSource.setUrl(config.getTargetUrl());
		dataSource.setUsername(config.getUsername());
		dataSource.setPassword(config.getPassword());
		dataSource.setValidationQuery("select ''");
		dataSource.setTimeBetweenEvictionRunsMillis(5000);
		dataSource.setNumTestsPerEvictionRun(10);
		dataSource.setTestOnBorrow(false);
		dataSource.setTestWhileIdle(false);

		dataSource.setInitialSize(10);
		dataSource.setMaxActive(1000);
		dataSource.setMaxIdle(5);
		dataSource.setMinIdle(1);

		Properties hibernateProperties = new Properties();
		hibernateProperties.put("current_session_context_class", "thread");
		hibernateProperties.put("cache.provider_class", "org.hibernate.cache.NoCacheProvider");
		hibernateProperties.put("hibernate.dialect", config.getDialect());
		hibernateProperties.put("hibernate.cache.use_second_level_cache", "false");
		hibernateProperties.put("hibernate.show_sql", config.getShow_sql());
		hibernateProperties.put("hibernate.hbm2ddl.auto", config.getHbm2ddl_auto());
		hibernateProperties.put("connection.autoReconnect", "true");
		hibernateProperties.put("connection.autoReconnectForPools", "true");
		hibernateProperties.put("connection.is-connection-validation-required", "true");
		hibernateProperties.put("hibernate.hbm2ddl.import_files", "/import.sql");

		AnnotationSessionFactoryBean factoryBean = new AnnotationSessionFactoryBean();
		factoryBean.setDataSource(dataSource);
		factoryBean.setPackagesToScan(new String[] { "org.solq.dht.test.db.model.*" });
		factoryBean.setHibernateProperties(hibernateProperties);
		factoryBean.afterPropertiesSet();

		return factoryBean.getObject();
	}

}