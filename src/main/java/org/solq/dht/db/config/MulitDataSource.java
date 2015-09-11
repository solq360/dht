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
		factoryBean.setPackagesToScan(getArrayValue(properties, "{dbName}.hibernate.scanPackages", dbName, true));
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
				getStringValue(properties, "{dbName}.hibernate.dialect", dbName, true));
		hibernateProperties.put("hibernate.show_sql",
				getStringValue(properties, "{dbName}.hibernate.show_sql", dbName, true));
		hibernateProperties.put("hibernate.hbm2ddl.auto",
				getStringValue(properties, "{dbName}.hibernate.hbm2ddl.auto", dbName, true));
		hibernateProperties.put("connection.autoReconnect",
				getStringValue(properties, "{dbName}.hibernate.autoReconnect", dbName, true));
		hibernateProperties.put("connection.autoReconnectForPools",
				getStringValue(properties, "{dbName}.hibernate.autoReconnectForPools", dbName, true));
		hibernateProperties.put("connection.is-connection-validation-required",
				getStringValue(properties, "{dbName}.hibernate.connection-validation", dbName, true));

		String importFile = PropertiesUtil.getValue(properties,
				replaceKey("{dbName}.hibernate.hbm2ddl.import_files", dbName), false);
		if (importFile != null) {
			hibernateProperties.put("hibernate.hbm2ddl.import_files", importFile);
		}
		return hibernateProperties;
	}

	private BasicDataSource buildDataSource(String dbName, Properties properties) {
		BasicDataSource dataSource = new BasicDataSource();

		dataSource.setDriverClassName(getStringValue(properties, "{dbName}.dataSource.driverClassName", dbName, true));
		dataSource.setUrl(getStringValue(properties, "{dbName}.dataSource.url", dbName, false));
		dataSource.setUsername(getStringValue(properties, "{dbName}.dataSource.username", dbName, false));
		dataSource.setPassword(getStringValue(properties, "{dbName}.dataSource.password", dbName, false));

		dataSource.setValidationQuery(getStringValue(properties, "{dbName}.dataSource.calidationQuery", dbName, true));
		dataSource.setTimeBetweenEvictionRunsMillis(
				getIntValue(properties, "{dbName}.dataSource.timeBetweenEvictionRunsMillis", dbName, true));
		dataSource.setNumTestsPerEvictionRun(
				getIntValue(properties, "{dbName}.dataSource.numTestsPerEvictionRun", dbName, true));

		dataSource.setTestOnBorrow(false);
		dataSource.setTestWhileIdle(false);

		dataSource.setInitialSize(getIntValue(properties, "{dbName}.dataSource.initialSize", dbName, true));
		dataSource.setMaxActive(getIntValue(properties, "{dbName}.dataSource.maxActive", dbName, true));
		dataSource.setMaxIdle(getIntValue(properties, "{dbName}.dataSource.maxIdle", dbName, true));
		dataSource.setMinIdle(getIntValue(properties, "{dbName}.dataSource.minIdle", dbName, true));
		dataSource.setMaxWait(getIntValue(properties, "{dbName}.dataSource.maxWait", dbName, true));

		dataSource.setRemoveAbandoned(getBooleanValue(properties, "{dbName}.dataSource.removeAbandoned", dbName, true));
		dataSource.setRemoveAbandonedTimeout(
				getIntValue(properties, "{dbName}.dataSource.removeAbandonedTimeout", dbName, true));

		return dataSource;
	}

	private static String getStringValue(Properties properties, String text, String dbName, boolean isDefault) {
		String result = PropertiesUtil.getValue(properties, text.replaceAll("\\{dbName\\}", dbName), !isDefault);
		if (result == null || result.trim() == "") {
			result = PropertiesUtil.getValue(properties, text.replaceAll("\\{dbName\\}", "global"), true);
		}
		return result;
	}

	private static String[] getArrayValue(Properties properties, String text, String dbName, boolean isDefault) {
		String[] result = PropertiesUtil.getArrayValue(properties, text.replaceAll("\\{dbName\\}", dbName), !isDefault);
		if (result == null || result.length == 0) {
			result = PropertiesUtil.getArrayValue(properties, text.replaceAll("\\{dbName\\}", "global"), true);
		}
		return result;
	}
	
 	private static Integer getIntValue(Properties properties, String text, String dbName, boolean isDefault) {
		Integer result = PropertiesUtil.getIntValue(properties, text.replaceAll("\\{dbName\\}", dbName), !isDefault);
		if (result == null) {
			result = PropertiesUtil.getIntValue(properties, text.replaceAll("\\{dbName\\}", "global"), true);
		}
		return result;
	}

 	private static Boolean getBooleanValue(Properties properties, String text, String dbName, boolean isDefault) {
		Boolean result = PropertiesUtil.getBooleanValue(properties, text.replaceAll("\\{dbName\\}", dbName),
				!isDefault);
		if (result == null) {
			result = PropertiesUtil.getBooleanValue(properties, text.replaceAll("\\{dbName\\}", "global"), true);
		}
		return result;
	}

	private static String replaceKey(String text, String dbName) {
		return text.replaceAll("\\{dbName\\}", dbName);
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
