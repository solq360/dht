package org.solq.dht.db.config;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.solq.dht.db.HibernateTemplate;
import org.solq.dht.db.model.DbMetadata;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;

public class MulitDataSource {

	/** <使用名称,配置名称> **/
	private Map<String, String> configs;
	/** 配置文件路径 **/
	private String readConfigPath;

	/** 保存构建数据库信息 **/
	private Map<String, DbMetadata> dbMetadatad = new HashMap<>();

	public static MulitDataSource of(String readConfigPath, Map<String, String> configs) {
		MulitDataSource result = new MulitDataSource();
		result.readConfigPath = readConfigPath;
		result.configs = configs;
		return result;
	}

	public void initBuild() throws Exception {
		InputStream is = ClassLoader.getSystemResourceAsStream(readConfigPath);
		Properties properties = new Properties();
		properties.load(is);

		for (Entry<String, String> entry : configs.entrySet()) {
			final String dbName = entry.getValue();
			BasicDataSource dataSource = buildDataSource(dbName, properties);
			Properties hibernateProperties = buildHibernateProperties(dbName, properties);
			SessionFactory sessionFactory = buildSessionFactory(dbName, properties, dataSource, hibernateProperties);
			HibernateTemplate hibernateTemplate = new HibernateTemplate(sessionFactory);

			dbMetadatad.put(entry.getKey(),
					DbMetadata.of(dbName, dataSource, hibernateProperties, sessionFactory, hibernateTemplate));
		}
		is.close();
	}

	private SessionFactory buildSessionFactory(String dbName, Properties properties, BasicDataSource dataSource,
			Properties hibernateProperties) throws Exception {
		AnnotationSessionFactoryBean factoryBean = new AnnotationSessionFactoryBean();
		factoryBean.setDataSource(dataSource);
		factoryBean.setHibernateProperties(hibernateProperties);
		factoryBean.setPackagesToScan(PropertiesUtil.getArrayValue(properties,
				replaceKey("{dbName}.hibernate.scanPackages", "dbName", dbName), true));
		factoryBean.afterPropertiesSet();
		// factoryBean.createDatabaseSchema();

		return factoryBean.getObject();
	}

	private Properties buildHibernateProperties(String dbName, Properties properties) {
		Properties hibernateProperties = new Properties();
		hibernateProperties.put("current_session_context_class", "thread");
		hibernateProperties.put("cache.provider_class", "org.hibernate.cache.NoCacheProvider");
		hibernateProperties.put("hibernate.cache.use_second_level_cache", false);

		hibernateProperties.put("hibernate.dialect",
				PropertiesUtil.getValue(properties, replaceKey("{dbName}.hibernate.dialect", "dbName", dbName), true));
		hibernateProperties.put("hibernate.show_sql",
				PropertiesUtil.getValue(properties, replaceKey("{dbName}.hibernate.show_sql", "dbName", dbName), true));
		hibernateProperties.put("hibernate.hbm2ddl.auto", PropertiesUtil.getValue(properties,
				replaceKey("{dbName}.hibernate.hbm2ddl.auto", "dbName", dbName), true));
		hibernateProperties.put("connection.autoReconnect", PropertiesUtil.getValue(properties,
				replaceKey("{dbName}.hibernate.autoReconnect", "dbName", dbName), true));
		hibernateProperties.put("connection.autoReconnectForPools", PropertiesUtil.getValue(properties,
				replaceKey("{dbName}.hibernate.autoReconnectForPools", "dbName", dbName), true));
		hibernateProperties.put("connection.is-connection-validation-required", PropertiesUtil.getValue(properties,
				replaceKey("{dbName}.hibernate.connection-validation", "dbName", dbName), true));

		String importFile = PropertiesUtil.getValue(properties,
				replaceKey("{dbName}.hibernate.hbm2ddl.import_files", "dbName", dbName), false);
		if (importFile != null && importFile != "") {
			hibernateProperties.put("hibernate.hbm2ddl.import_files", importFile);
		}
		return hibernateProperties;
	}

	private BasicDataSource buildDataSource(String dbName, Properties properties) {
		BasicDataSource dataSource = new BasicDataSource();

		dataSource.setDriverClassName(PropertiesUtil.getValue(properties,
				replaceKey("{dbName}.dataSource.driverClassName", "dbName", dbName), true));
		dataSource.setUrl(
				PropertiesUtil.getValue(properties, replaceKey("{dbName}.dataSource.url", "dbName", dbName), true));
		dataSource.setUsername(PropertiesUtil.getValue(properties,
				replaceKey("{dbName}.dataSource.username", "dbName", dbName), true));
		dataSource.setPassword(PropertiesUtil.getValue(properties,
				replaceKey("{dbName}.dataSource.password", "dbName", dbName), true));

		dataSource.setValidationQuery(PropertiesUtil.getValue(properties,
				replaceKey("{dbName}.dataSource.calidationQuery", "dbName", dbName), true));

		dataSource.setTimeBetweenEvictionRunsMillis(PropertiesUtil.getIntValue(properties,
				replaceKey("{dbName}.dataSource.timeBetweenEvictionRunsMillis", "dbName", dbName), true));

		dataSource.setNumTestsPerEvictionRun(PropertiesUtil.getIntValue(properties,
				replaceKey("{dbName}.dataSource.numTestsPerEvictionRun", "dbName", dbName), true));
		dataSource.setTestOnBorrow(false);
		dataSource.setTestWhileIdle(false);

		dataSource.setInitialSize(PropertiesUtil.getIntValue(properties,
				replaceKey("{dbName}.dataSource.initialSize", "dbName", dbName), true));
		dataSource.setMaxActive(PropertiesUtil.getIntValue(properties,
				replaceKey("{dbName}.dataSource.maxActive", "dbName", dbName), true));
		dataSource.setMaxIdle(PropertiesUtil.getIntValue(properties,
				replaceKey("{dbName}.dataSource.maxIdle", "dbName", dbName), true));
		dataSource.setMinIdle(PropertiesUtil.getIntValue(properties,
				replaceKey("{dbName}.dataSource.minIdle", "dbName", dbName), true));

		dataSource.setMaxWait(PropertiesUtil.getIntValue(properties,
				replaceKey("{dbName}.dataSource.maxWait", "dbName", dbName), true));

		dataSource.setRemoveAbandoned(PropertiesUtil.getBooleanValue(properties,
				replaceKey("{dbName}.dataSource.removeAbandoned", "dbName", dbName), true));

		dataSource.setRemoveAbandonedTimeout(PropertiesUtil.getIntValue(properties,
				replaceKey("{dbName}.dataSource.removeAbandonedTimeout", "dbName", dbName), true));

		return dataSource;
	}

	private static String replaceKey(String text, String key, String value) {
		return text.replaceAll("\\{" + key + "\\}", value);
	}

	public void setConfigs(Map<String, String> configs) {
		this.configs = configs;
	}

	public void setReadConfigPath(String readConfigPath) {
		this.readConfigPath = readConfigPath;
	}

	public Map<String, String> getConfigs() {
		return configs;
	}

	public String getReadConfigPath() {
		return readConfigPath;
	}

	public Map<String, DbMetadata> getDbMetadatad() {
		return dbMetadatad;
	}

}
