package org.solq.dht.db;

import org.hibernate.cfg.DefaultNamingStrategy;

public class MyNamingStrategy extends DefaultNamingStrategy {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8995429608912391186L;
	public static final MyNamingStrategy INSTANCE = new MyNamingStrategy();

	public String classToTableName(String className) {
		return "SSE_" + className.toUpperCase() + "_" + System.currentTimeMillis();
	}

}
