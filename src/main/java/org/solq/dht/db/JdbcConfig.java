package org.solq.dht.db;

import java.util.Properties;

public class JdbcConfig {

	// Fields

	private String driverClassName;

	private String username;

	private String password;

	private String dialect;

	private String show_sql;

	private String hbm2ddl_auto;

	private String targetUrl;

	private String[] scanPackages;

 	JdbcConfig() {
	}

	public static JdbcConfig of(DBDriver dbDriver, String url, String user, String password, String... scanPackages) {
		JdbcConfig result = new JdbcConfig();
		result.driverClassName = dbDriver.getDriver();
		result.dialect = dbDriver.getDialect();

		result.username = user;
		result.password = password;
		result.targetUrl = url;
		result.show_sql = "false";
		result.hbm2ddl_auto = "update";
		result.scanPackages = scanPackages;

		return result;
	}

	public JdbcConfig(Properties baseConfig) {
		this.driverClassName = baseConfig.getProperty("jdbc.driverClassName");
		this.username = baseConfig.getProperty("jdbc.username");
		this.password = baseConfig.getProperty("jdbc.password");
		this.targetUrl = baseConfig.getProperty("jdbc.url");
		this.dialect = baseConfig.getProperty("hibernate.dialect");
		this.show_sql = baseConfig.getProperty("hibernate.show_sql");
		this.hbm2ddl_auto = baseConfig.getProperty("hibernate.hbm2ddl.auto");
	}

	// Getters

	public String getDriverClassName() {
		return driverClassName;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getDialect() {
		return dialect;
	}

	public String getShow_sql() {
		return show_sql;
	}

	public String getHbm2ddl_auto() {
		return hbm2ddl_auto;
	}

	public String getTargetUrl() {
		return targetUrl;
	}

	public String[] getScanPackages() {
		return scanPackages;
	}

}
