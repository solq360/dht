package org.solq.dht.db;

public enum DBDriver {
	MYSQL_INNODB("com.mysql.jdbc.Driver","org.hibernate.dialect.MySQL5InnoDBDialect"),
	H2("org.h2.Driver","org.hibernate.dialect.H2Dialect"),;
	private DBDriver(String driver,String dialect){
		this.driver=driver;
		this.dialect=dialect;
	}
	
	private String dialect;
	private String driver;

	
	//getter
	
	public String getDialect() {
		return dialect;
	}
	public String getDriver() {
		return driver;
	}
}
